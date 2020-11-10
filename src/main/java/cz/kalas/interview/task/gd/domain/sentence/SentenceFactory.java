package cz.kalas.interview.task.gd.domain.sentence;

import cz.kalas.interview.task.gd.model.entity.Sentence;

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
