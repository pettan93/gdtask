package cz.kalas.interview.task.gd;

import cz.kalas.interview.task.gd.domain.sentence.SentenceFactory;
import cz.kalas.interview.task.gd.model.WordCategory;
import cz.kalas.interview.task.gd.model.entity.Sentence;
import cz.kalas.interview.task.gd.model.entity.SentenceStats;
import cz.kalas.interview.task.gd.model.entity.Word;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestUtils {

    public static Optional<Word> getOptionalDummyWord() {
        return Optional.of(
                getDummyWord(WordCategory.values()[(int) Math.abs(ThreadLocalRandom.current().nextLong() % 2)])
        );
    }

    public static Optional<Word> getOptionalDummyWord(WordCategory wordCategory) {
        return Optional.of(
                getDummyWord(wordCategory)
        );
    }

    public static Word getDummyWord() {
        return getDummyWord(WordCategory.values()[(int) Math.abs(ThreadLocalRandom.current().nextLong() % 2)]);
    }

    public static Word getDummyWord(WordCategory wordCategory) {
        long random = ThreadLocalRandom.current().nextLong();
        return new Word("word" + random, wordCategory);
    }

    public static List<Word> getDummyWords(int size) {
        return IntStream.range(0, size)
                .boxed()
                .map(i -> getDummyWord())
                .collect(Collectors.toList());
    }

    public static List<Sentence> getDummySentences(SentenceFactory factory, int size) {
        return IntStream.range(0, size)
                .boxed()
                .map(i -> factory.createSentence())
                .collect(Collectors.toList());
    }

    public static SentenceStats getDummySentenceStats(Sentence sentence) {
        var stats = new SentenceStats();
        stats.setSentence(sentence);
        stats.incrementDisplayCount();
        return stats;
    }


}
