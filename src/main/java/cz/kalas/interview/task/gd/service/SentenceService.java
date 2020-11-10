package cz.kalas.interview.task.gd.service;

import cz.kalas.interview.task.gd.model.entity.Sentence;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.Optional;

public interface SentenceService {

    Collection<Sentence> getAll();

    Page<Sentence> getAllPageable(Integer pageNumber, Integer pageSize);

    Sentence generate();

    Sentence getById(Long id);

}
