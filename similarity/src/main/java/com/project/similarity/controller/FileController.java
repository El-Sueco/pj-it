package com.project.similarity.controller;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import com.project.similarity.db.entity.File;
import com.project.similarity.db.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.diff.EditScript;
import org.apache.commons.text.diff.StringsComparator;
import org.apache.commons.text.similarity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@RestController
@Slf4j
@RequestMapping("/files")
public class FileController {

    @Autowired
    FileService fileService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<File>> getAll(){
        List<File> types = fileService.getAll();
        return new ResponseEntity<>(types, HttpStatus.OK);
    }

    @RequestMapping(value = "/test-fuzzy/{id1}/{id2}", method = RequestMethod.GET)
    public ResponseEntity<Integer> testFuzzy(@PathVariable Long id1, @PathVariable Long id2){
        FuzzyScore score = new FuzzyScore(Locale.GERMAN);
        Integer scoreInt = score.fuzzyScore(fileService.getById(id1).getContent(), fileService.getById(id2).getContent());
        return new ResponseEntity<>(scoreInt, HttpStatus.OK);
    }

    @RequestMapping(value = "/test-lcsd/{id1}/{id2}", method = RequestMethod.GET)
    public ResponseEntity<Integer> testLcsd(@PathVariable Long id1, @PathVariable Long id2){
        LongestCommonSubsequenceDistance score = new LongestCommonSubsequenceDistance();
        Integer scoreInt = score.apply(fileService.getById(id1).getContent(), fileService.getById(id2).getContent());

        //test show diff
        StringsComparator comparator = new StringsComparator(fileService.getById(id1).getContent(), fileService.getById(id2).getContent());
        EditScript<Character> script = comparator.getScript();

        log.info(StringUtils.difference(fileService.getById(id1).getContent(), fileService.getById(id2).getContent()));
        log.warn("===other===");

        List<String> original = Arrays.asList(fileService.getById(id1).getContent().split("\\r?\\n"));
        List<String> revised = Arrays.asList(fileService.getById(id2).getContent().split("\\r?\\n"));
        log.warn(original.toString());
        Patch<String> patch = DiffUtils.diff(original, revised);

        for (AbstractDelta<String> delta : patch.getDeltas()) {
            log.info(delta.toString());
        }
        // test show diff

        return new ResponseEntity<>(scoreInt, HttpStatus.OK);
    }

    @RequestMapping(value = "/test-hamming/{id1}/{id2}", method = RequestMethod.GET)
    public ResponseEntity<Integer> testHamming(@PathVariable Long id1, @PathVariable Long id2){
        HammingDistance score = new HammingDistance();
        Integer scoreInt = score.apply(fileService.getById(id1).getContent(), fileService.getById(id2).getContent());
        return new ResponseEntity<>(scoreInt, HttpStatus.OK);
    }

    @RequestMapping(value = "/test-cosine-distance/{id1}/{id2}", method = RequestMethod.GET)
    public ResponseEntity<Double> testCosineDistance(@PathVariable Long id1, @PathVariable Long id2){
        CosineDistance score = new CosineDistance();
        Double scoreDouble = score.apply(fileService.getById(id1).getContent(), fileService.getById(id2).getContent());
        return new ResponseEntity<>(scoreDouble, HttpStatus.OK);
    }

    @RequestMapping(value = "/test-cosine-similarity/{id1}/{id2}", method = RequestMethod.GET)
    public ResponseEntity<Double> testCosineSimilarity(@PathVariable Long id1, @PathVariable Long id2){
        /*CosineSimilarity score = new CosineSimilarity();
        Double scoreDouble = score.cosineSimilarity(fileService.getById(id1).getContent(), fileService.getById(id2).getContent());
        return new ResponseEntity<>(scoreDouble, HttpStatus.OK);*/
        return null;
    }

    @RequestMapping(value = "/test-jaro-distance/{id1}/{id2}", method = RequestMethod.GET)
    public ResponseEntity<Double> testJaroDist(@PathVariable Long id1, @PathVariable Long id2){
        JaroWinklerDistance score = new JaroWinklerDistance();
        Double scoreDouble = score.apply(fileService.getById(id1).getContent(), fileService.getById(id2).getContent());
        return new ResponseEntity<>(scoreDouble, HttpStatus.OK);
    }

    @RequestMapping(value = "/test-jaro-similarity/{id1}/{id2}", method = RequestMethod.GET)
    public ResponseEntity<Double> testJaroSim(@PathVariable Long id1, @PathVariable Long id2){
        JaroWinklerDistance score = new JaroWinklerDistance();
        Double scoreDouble = score.apply(fileService.getById(id1).getContent(), fileService.getById(id2).getContent());
        return new ResponseEntity<>(scoreDouble, HttpStatus.OK);
    }

    @RequestMapping(value = "/test-levenstein/{id1}/{id2}", method = RequestMethod.GET)
    public ResponseEntity<Integer> testLeve(@PathVariable Long id1, @PathVariable Long id2){
        LevenshteinDistance score = new LevenshteinDistance();
        Integer scoreInt = score.apply(fileService.getById(id1).getContent(), fileService.getById(id2).getContent());
        return new ResponseEntity<>(scoreInt, HttpStatus.OK);
    }
}
