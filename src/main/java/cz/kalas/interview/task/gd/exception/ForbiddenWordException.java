package cz.kalas.interview.task.gd.exception;

import cz.kalas.interview.task.gd.model.entity.Word;
import lombok.Getter;

@Getter
public class ForbiddenWordException extends RuntimeException {

    private final Word forbiddenWord;

    public ForbiddenWordException(Word forbiddenWord) {
        this.forbiddenWord = forbiddenWord;
    }
}
