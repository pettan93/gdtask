package cz.kalas.training.sentencegenerator.service;

import cz.kalas.training.sentencegenerator.model.entity.Sentence;
import org.springframework.data.domain.Page;

import java.util.Collection;

public interface SentenceService {

    Collection<Sentence> getAll();

    Page<Sentence> getAllPageable(Integer pageNumber, Integer pageSize);

    Sentence generate();

    Sentence getById(Long id);

}
