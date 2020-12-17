package cz.kalas.training.sentencegenerator.exception;

public class NotEnoughWordsException extends RuntimeException {

    public NotEnoughWordsException(String message) {
        super(message);
    }
}
