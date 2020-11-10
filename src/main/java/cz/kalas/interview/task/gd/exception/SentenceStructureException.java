package cz.kalas.interview.task.gd.exception;

import cz.kalas.interview.task.gd.model.entity.Sentence;
import lombok.Getter;

@Getter
public class SentenceStructureException extends RuntimeException {

    private final Sentence malformedSentence;

    public SentenceStructureException(String msg, Sentence malformedSentence) {
        super(msg);
        this.malformedSentence = malformedSentence;
    }
}
