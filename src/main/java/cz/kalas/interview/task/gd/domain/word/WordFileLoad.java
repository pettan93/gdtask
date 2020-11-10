package cz.kalas.interview.task.gd.domain.word;

import cz.kalas.interview.task.gd.model.WordCategory;
import cz.kalas.interview.task.gd.model.entity.Word;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class WordFileLoad implements WordLoad {

    private final File file;

    @Override
    public Word getWord() {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public Collection<Word> getWords() {
        if (!file.exists()) return Collections.emptyList();

        var resultCollection = new ArrayList<Word>();
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                parseWord(line).ifPresent(resultCollection::add);
            }
        } catch (Exception e) {
            log.error("Word file read problem", e);
            e.printStackTrace();
        }
        return resultCollection;
    }

    private Optional<Word> parseWord(String line) {
        var split = line.trim().split(";");
        if (split.length < 2)
            return Optional.empty();
        var word = split[0];
        var wordCategory = WordCategory.get(split[1]);
        return wordCategory.map(c -> new Word(word, c));
    }

}
