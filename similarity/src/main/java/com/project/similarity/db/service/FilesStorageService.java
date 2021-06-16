package com.project.similarity.db.service;

import com.project.similarity.db.entity.Aufgabe;
import com.project.similarity.db.entity.File;
import com.project.similarity.db.repository.FilesStorageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
@Service
public class FilesStorageService implements FilesStorageRepository {

    private final Path root = Paths.get("src/main/resources/uploads");

    @Autowired
    FileService fileService;
    @Autowired
    AufgabeService aufgabeService;
    @Autowired
    FilesStorageService filesStorageService;
    @Autowired
    SimilarityService similarityService;

    @Override
    public void init() {
        try {
            Files.createDirectory(root);
        } catch (FileAlreadyExistsException e) {
            log.info("All good, ignore this :)");
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void load(Path path) {
        try {
            Resource resource = new UrlResource(path.toUri());
            if (!(resource.exists() || resource.isReadable())) {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    public void deleteFolder(Path folder) {
        FileSystemUtils.deleteRecursively(folder.toFile());
    }

    public void deleteZip(String zip) {
        FileSystemUtils.deleteRecursively(Paths.get(root.toString() + "/" + zip).toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    @Transactional
    public void unzip(MultipartFile file) throws IOException {
        deleteZip(file.getOriginalFilename());
        save(file);
        unzip(file.getOriginalFilename());
    }

    public void unzip(String file) throws IOException {
        Aufgabe aufgabe = new Aufgabe();

        String fileZip = String.format("src/main/resources/uploads/%s", file);
        java.io.File destDir = new java.io.File("src/main/resources/unzipped");
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            java.io.File newFile = newFile(destDir, zipEntry);
            if (zipEntry.isDirectory()) {
                aufgabe = aufgabeService.getByNameAndZipName(newFile.getName(), file);
                aufgabe.setName(newFile.getName());
                aufgabe.setPath(newFile.getPath());
                aufgabe.setZipName(file);
                aufgabeService.save(aufgabe);
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                File dbFile = fileService.getByNameAndAufgabe(newFile.getName(), aufgabe);
                dbFile.setName(newFile.getName());
                dbFile.setPath(newFile.getPath());
                dbFile.setAufgabe(aufgabe);
                fileService.save(dbFile);
                java.io.File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

    private java.io.File newFile(java.io.File destinationDir, ZipEntry zipEntry) throws IOException {
        java.io.File destFile = new java.io.File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + java.io.File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
}