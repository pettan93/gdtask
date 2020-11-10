package cz.kalas.interview.task.gd.domain.sentence;

import cz.kalas.interview.task.gd.model.WordCategory;
import cz.kalas.interview.task.gd.model.entity.Sentence;
import cz.kalas.interview.task.gd.model.entity.WordSentenceUsage;

import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * GoodData sentence rule: sentence is in the form of NOUN VERB ADJECTIVE
 */
public class GoodDataSentenceValidator implements SentenceValidator {

    private static final int sentenceWordCount = 3;

    private static final Map<Integer, WordCategory> wordCategoryOrder = Map.of(
            0, WordCategory.NOUN,
            1, WordCategory.VERB,
            2, WordCategory.ADJECTIVE
    );

    @Override
    public boolean isValid(Sentence sentence) {
        return sentence.getUsedWords().size() == sentenceWordCount &&
                sentence.getUsedWords().stream().allMatch(isValidWordUsage());
    }

    @Override
    public String getRulesReadable() {
        var msg = new StringBuilder();
        msg.append(sentenceWordCount)
                .append(" words total with given types of words in order: ")
                .append(wordCategoryOrder.keySet()
                        .stream()
                        .sorted()
                        .map(o -> wordCategoryOrder.get(o).name().toLowerCase())
                        .collect(Collectors.joining(", ")));
        return msg.toString();
    }

    private Predicate<WordSentenceUsage> isValidWordUsage() {
        return p -> wordCategoryOrder.get(p.getPlace())
                .equals(p.getWord().getWordCategory());
    }

}
