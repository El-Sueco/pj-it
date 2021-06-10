package com.project.similarity.utils;

import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import com.project.similarity.utils.models.FileDiff;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class ComparisonUtils {

    public static FileDiff showDiff(File f1, File f2) {
        DiffRowGenerator generator = DiffRowGenerator.create()
                .showInlineDiffs(true)
                .inlineDiffByWord(true)
                //.oldTag(f -> "---")
                //.newTag(f -> "+++")
                .build();
        List<DiffRow> rows = generator.generateDiffRows(
                Arrays.asList("".split("\\r?\\n")),
                //Arrays.asList(f1.getContent().split("\\r?\\n")),
                Arrays.asList("".split("\\r?\\n")));
                //Arrays.asList(f2.getContent().split("\\r?\\n")));

        List<String> fileOne = new ArrayList<>();
        List<String> fileTwo = new ArrayList<>();
        for (DiffRow row : rows) {
            fileOne.add(row.getOldLine());
            fileTwo.add(row.getNewLine());
        }
        return new FileDiff(fileOne, fileTwo);
    }

    public static Double cosineSimilarity(Path f1, Path f2) throws IOException {
        String f1Content = StringUtils.join(Files.readAllLines(f1, Charset.defaultCharset()), "");
        String f2Content = StringUtils.join(Files.readAllLines(f2, Charset.defaultCharset()), "");
        Map<CharSequence, Integer> leftVector = new HashMap<>();
        Map<CharSequence, Integer> rightVector = new HashMap<>();
        //leftVector.put(f1Content, f1Content.length());
        //rightVector.put(f2Content, f2Content.length());
        leftVector =  Files.readAllLines(f1, Charset.defaultCharset()).stream().distinct()
                .collect(Collectors.toMap(Function.identity(), String::length));
        rightVector =  Files.readAllLines(f2, Charset.defaultCharset()).stream().distinct()
                .collect(Collectors.toMap(Function.identity(), String::length));


        CosineSimilarity score = new CosineSimilarity();
        return score.cosineSimilarity(leftVector, rightVector);
    }

    @Deprecated
    private static Integer fuzzyScore(File f1, File f2){
        FuzzyScore score = new FuzzyScore(Locale.GERMAN);
        return score.fuzzyScore("f1.getContent()", "f2.getContent()");
    }

    @Deprecated
    private static Integer longestCommonSubsequenceDistance(File f1, File f2){
        LongestCommonSubsequenceDistance score = new LongestCommonSubsequenceDistance();
        return score.apply("f1.getContent()", "f2.getContent()");
    }

    @Deprecated
    private static Integer hammingDistance(File f1, File f2){
        HammingDistance score = new HammingDistance();
        return score.apply("f1.getContent()", "f2.getContent()");
    }

    @Deprecated
    private static Double cosineDistance(File f1, File f2){
        CosineDistance score = new CosineDistance();
        return score.apply("f1.getContent()", "f2.getContent()");
    }

    @Deprecated
    private static Double jaroWinklerDistance(File f1, File f2){
        JaroWinklerDistance score = new JaroWinklerDistance();
        return score.apply("f1.getContent()", "f2.getContent()");
    }

    @Deprecated
    private static Double jaroWinklerSimilarity(File f1, File f2){
        JaroWinklerSimilarity score = new JaroWinklerSimilarity();
        return score.apply("f1.getContent()", "f2.getContent()");
    }

    @Deprecated
    private static Integer levenshteinDistance(File f1, File f2){
        LevenshteinDistance score = new LevenshteinDistance();
        return score.apply("f1.getContent()", "f2.getContent()");
    }

    /*
    public static Number compareTwoModels(File f1, File f2) {
        switch (algo.getName()) {
            case "FuzzyScore":
                return fuzzyScore(f1, f2);
            case "LongestCommonSubsequenceDistance":
                return longestCommonSubsequenceDistance(f1, f2);
            case "HammingDistance":
                return hammingDistance(f1, f2);
            case "CosineDistance":
                return cosineDistance(f1, f2);
            case "CosineSimilarity":
                return cosineSimilarity(f1, f2);
            case "JaroWinklerDistance":
                return jaroWinklerDistance(f1, f2);
            case "JaroWinklerSimilarity":
                return jaroWinklerSimilarity(f1, f2);
            case "LevenshteinDistance":
                return levenshteinDistance(f1, f2);
            default:
                throw new MethodNotFoundException();
        }
    }
*/
}
