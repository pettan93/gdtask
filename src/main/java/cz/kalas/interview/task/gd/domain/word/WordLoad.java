package cz.kalas.interview.task.gd.domain.word;

import cz.kalas.interview.task.gd.model.entity.Word;

import java.util.Collection;

/**
 * Represents one-time load of word
 */
public interface WordLoad {

    Word getWord();

    Collection<Word> getWords();

}
