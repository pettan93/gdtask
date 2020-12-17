package cz.kalas.training.sentencegenerator.exception;

import cz.kalas.training.sentencegenerator.model.entity.Word;
import lombok.Getter;

@Getter
public class ForbiddenWordException extends RuntimeException {

    private final Word forbiddenWord;

    public ForbiddenWordException(Word forbiddenWord) {
        this.forbiddenWord = forbiddenWord;
    }
}
