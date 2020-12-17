package cz.kalas.training.sentencegenerator.domain.word;

import cz.kalas.training.sentencegenerator.model.entity.Word;

import java.util.Collection;

/**
 * Represents one-time load of word
 */
public interface WordLoad {

    Word getWord();

    Collection<Word> getWords();

}
