package cz.kalas.training.sentencegenerator.domain.sentence;

import cz.kalas.training.sentencegenerator.model.entity.Sentence;

/**
 * Represents object capable of creating sentences
 */
public interface SentenceFactory {

    /**
     * Creates new sentence
     * @return new sentence
     */
    Sentence createSentence();

}
