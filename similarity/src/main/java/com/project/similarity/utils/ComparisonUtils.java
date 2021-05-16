package com.project.similarity.utils;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import com.project.similarity.db.entity.Algo;
import com.project.similarity.db.entity.File;
import com.project.similarity.utils.models.FileDiff;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.diff.EditScript;
import org.apache.commons.text.diff.StringsComparator;
import org.apache.commons.text.similarity.*;

import javax.el.MethodNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Slf4j
public class ComparisonUtils {

    public static Number compareTwoModels(Algo algo, File f1, File f2) {
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

    // TODO delete log lines, where not necessary
    public static FileDiff showDiff(File f1, File f2) {
        DiffRowGenerator generator = DiffRowGenerator.create()
                .showInlineDiffs(true)
                .inlineDiffByWord(true)
                .oldTag(f -> "---")
                .newTag(f -> "+++")
                .build();
        List<DiffRow> rows = generator.generateDiffRows(
                Arrays.asList(f1.getContent().split("\\r?\\n")),
                Arrays.asList(f2.getContent().split("\\r?\\n")));

        log.info("|original|new|");
        log.info("|--------|---|");
        List<String> fileOne = new ArrayList<>();
        List<String> fileTwo = new ArrayList<>();
        for (DiffRow row : rows) {
            fileOne.add(row.getOldLine().replace("&lt;", "<").replace("&gt;", ">"));
            fileTwo.add(row.getNewLine().replace("&lt;", "<").replace("&gt;", ">"));
            log.info("|" + row.getOldLine().replace("&lt;", "<").replace("&gt;", ">")
                    + "|" + row.getNewLine().replace("&lt;", "<").replace("&gt;", ">") + "|");
        }
        return new FileDiff(fileOne, fileTwo);
    }

    private static Integer fuzzyScore(File f1, File f2){
        FuzzyScore score = new FuzzyScore(Locale.GERMAN);
        return score.fuzzyScore(f1.getContent(), f2.getContent());
    }

    private static Integer longestCommonSubsequenceDistance(File f1, File f2){
        LongestCommonSubsequenceDistance score = new LongestCommonSubsequenceDistance();
        return score.apply(f1.getContent(), f2.getContent());
    }

    private static Integer hammingDistance(File f1, File f2){
        HammingDistance score = new HammingDistance();
        return score.apply(f1.getContent(), f2.getContent());
    }

    private static Double cosineDistance(File f1, File f2){
        CosineDistance score = new CosineDistance();
        return score.apply(f1.getContent(), f2.getContent());
    }

    private static Double cosineSimilarity(File f1, File f2){
        /*CosineSimilarity score = new CosineSimilarity();
        return score.cosineSimilarity(f1.getContent(), f2.getContent());
        */
        return null;
    }

    private static Double jaroWinklerDistance(File f1, File f2){
        JaroWinklerDistance score = new JaroWinklerDistance();
        return score.apply(f1.getContent(), f2.getContent());
    }

    private static Double jaroWinklerSimilarity(File f1, File f2){
        JaroWinklerSimilarity score = new JaroWinklerSimilarity();
        return score.apply(f1.getContent(), f2.getContent());
    }

    private static Integer levenshteinDistance(File f1, File f2){
        LevenshteinDistance score = new LevenshteinDistance();
        return score.apply(f1.getContent(), f2.getContent());
    }
}
