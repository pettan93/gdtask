package cz.kalas.training.sentencegenerator.exception;

import cz.kalas.training.sentencegenerator.model.entity.Sentence;
import lombok.Getter;

@Getter
public class SentenceStructureException extends RuntimeException {

    private final Sentence malformedSentence;

    public SentenceStructureException(String msg, Sentence malformedSentence) {
        super(msg);
        this.malformedSentence = malformedSentence;
    }
}
