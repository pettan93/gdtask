package cz.kalas.training.sentencegenerator.domain.sentence;

import cz.kalas.training.sentencegenerator.model.entity.Sentence;

/**
 * SentenceValidator validates if sentence structure follows structure rules
 */
public interface SentenceValidator {

    /**
     * Checks if sentence follows given sentence rules
     *
     * @param sentence Sentence to check
     * @return Sentence validity
     */
    boolean isValid(Sentence sentence);

    /**
     * Returns rules which should sentence follow in human readable form.
     * Useful for error messages, ui descriptions, etc.
     *
     * @return Brief summary rules for valid sentence
     */
    String getRulesReadable();
}
