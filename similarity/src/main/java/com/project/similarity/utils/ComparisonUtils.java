package com.project.similarity.utils;

import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import com.project.similarity.utils.models.FileDiff;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.CosineSimilarity;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class ComparisonUtils {

    public static FileDiff showDiff(Path f1, Path f2) throws IOException {
        DiffRowGenerator generator = DiffRowGenerator.create()
                .showInlineDiffs(true)
                .inlineDiffByWord(true)
                .oldTag(f -> "+++")
                .newTag(f -> "+++")
                .build();
        List<DiffRow> rows = generator.generateDiffRows(
                Files.readAllLines(f1, Charset.defaultCharset()),
                Files.readAllLines(f2, Charset.defaultCharset()));

        List<String> fileOne = new ArrayList<>();
        List<String> fileTwo = new ArrayList<>();
        for (DiffRow row : rows) {
            fileOne.add(row.getOldLine());
            fileTwo.add(row.getNewLine());
        }
        return new FileDiff(fileOne, fileTwo);
    }

    public static Double cosineSimilarity(Path f1, Path f2) throws IOException {
        Map<CharSequence, Integer> leftVector = Files.readAllLines(f1, Charset.defaultCharset()).stream().distinct()
                .collect(Collectors.toMap(Function.identity(), String::length));
        Map<CharSequence, Integer> rightVector = Files.readAllLines(f2, Charset.defaultCharset()).stream().distinct()
                .collect(Collectors.toMap(Function.identity(), String::length));

        CosineSimilarity score = new CosineSimilarity();
        return formatResult(score.cosineSimilarity(leftVector, rightVector));
    }

    private static Double formatResult(Double score) {
        BigDecimal formattedScore = new BigDecimal(Double.toString(score * 100));
        formattedScore = formattedScore.setScale(2, RoundingMode.HALF_UP);
        return formattedScore.doubleValue();
    }
}
