package cz.kalas.interview.task.gd.service;


import cz.kalas.interview.task.gd.exception.ForbiddenWordException;
import cz.kalas.interview.task.gd.exception.NotFoundException;
import cz.kalas.interview.task.gd.model.WordCategory;
import cz.kalas.interview.task.gd.model.entity.Word;
import cz.kalas.interview.task.gd.repository.WordRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@AllArgsConstructor
@Service
public class WordServiceImpl implements WordService {

    private final WordRepository wordRepository;

    private final Validator validator;

    @Override
    public Collection<Word> getAll() {
        return wordRepository.findByForbiddenFalse();
    }

    @Override
    public Page<Word> getAllPageable(Integer pageNumber, Integer pageSize) {
        return wordRepository.findAllByForbiddenFalse(PageRequest.of(pageNumber, pageSize));
    }

    @Override
    public Word getWord(String text) {
        return wordRepository.findByText(normalize(text))
                .orElseThrow(() -> new NotFoundException(String.format("Word with text %s was not found", text)));
    }

    @Override
    public Optional<Word> getRandom(WordCategory wordCategory) {
        return wordRepository.getRandom(wordCategory);
    }

    /**
     * Stores new word if it does not exists, retrieves existing otherwise
     * Throws exception if word is forbidden
     *
     * @param word Word to store
     * @return Stored word
     */
    @Transactional
    @Override
    public Word save(Word word) {
        word.normalize(this::normalize);
        var duplicate = wordRepository.findByTextAndWordCategory(
                normalize(word.getText()), word.getWordCategory()
        );
        if (duplicate.isPresent()) {
            var existingWord = duplicate.get();
            if (existingWord.isForbidden()) {
                log.info("Attempt to save forbidden word '{}'", existingWord.toString());
                throw new ForbiddenWordException(duplicate.get());
            }
            log.info("Attempt to save word which already exists {}", existingWord.toString());
            return duplicate.get();
        } else {
            var a = validator.validate(word);
            if (!a.isEmpty()) {
                throw new ConstraintViolationException(a);
            }
            return wordRepository.save(word);
        }
    }

    @Override
    public Collection<Word> saveAll(Collection<Word> words) {
        return words.stream().map(this::save).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Collection<Word> registerForbiddenWords(Collection<Word> words) {
        var existingForbidden = wordRepository.findByForbiddenTrue();
        log.info("Storing {} forbidden words", words.size());
        return wordRepository.saveAll(words.stream()
                .map(w -> w.normalize(this::normalize))
                .peek(w -> w.setForbidden(true))
                .filter(w -> !existingForbidden.contains(w))
                .collect(Collectors.toList()));
    }

    private String normalize(String text) {
        return text.trim().toLowerCase();
    }

}
