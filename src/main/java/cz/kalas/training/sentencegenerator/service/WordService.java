package cz.kalas.training.sentencegenerator.service;

import cz.kalas.training.sentencegenerator.model.WordCategory;
import cz.kalas.training.sentencegenerator.model.entity.Word;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.Optional;

public interface WordService {

    Collection<Word> getAll();

    Page<Word> getAllPageable(Integer pageNumber, Integer pageSize);

    Word getWord(String text);

    Optional<Word> getRandom(WordCategory wordCategory);

    Word save(Word word);

    Collection<Word> saveAll(Collection<Word> word);

    Collection<Word> registerForbiddenWords(Collection<Word> words);
}
