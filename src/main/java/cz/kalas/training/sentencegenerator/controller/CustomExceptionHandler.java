package cz.kalas.training.sentencegenerator.controller;

import cz.kalas.training.sentencegenerator.domain.sentence.SentenceValidator;
import cz.kalas.training.sentencegenerator.exception.ForbiddenWordException;
import cz.kalas.training.sentencegenerator.exception.NotEnoughWordsException;
import cz.kalas.training.sentencegenerator.exception.NotFoundException;
import cz.kalas.training.sentencegenerator.exception.SentenceStructureException;
import cz.kalas.training.sentencegenerator.model.dto.ApiError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private final SentenceValidator sentenceValidator;

    @ExceptionHandler(SentenceStructureException.class)
    protected ResponseEntity<Object> handleConflict(SentenceStructureException ex, WebRequest request) {
        var apiError = new ApiError(
                ex.getLocalizedMessage(),
                String.format("Generated sentence '%s' do not matches validation rules: %s",
                        ex.getMalformedSentence().getAsText(),
                        sentenceValidator.getRulesReadable())
        );
        return handleExceptionInternal(ex, apiError,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(ForbiddenWordException.class)
    protected ResponseEntity<Object> handleConflict(ForbiddenWordException ex, WebRequest request) {
        var apiError = new ApiError(
                ex.getLocalizedMessage(),
                String.format("Word '%s' is forbidden.", ex.getForbiddenWord().getText())
        );
        return handleExceptionInternal(ex, apiError,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleConflict(NotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex, new ApiError(ex.getLocalizedMessage()),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(NotEnoughWordsException.class)
    protected ResponseEntity<Object> handleConflict(NotEnoughWordsException ex, WebRequest request) {
        return handleExceptionInternal(ex, new ApiError(ex.getLocalizedMessage()),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConflict(ConstraintViolationException ex, WebRequest request) {
        return handleExceptionInternal(ex, new ApiError(ex.getLocalizedMessage()),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {
        ex.printStackTrace();
        log.error("Exception", ex);
        return handleExceptionInternal(ex, new ApiError(ex.getLocalizedMessage()),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


}
