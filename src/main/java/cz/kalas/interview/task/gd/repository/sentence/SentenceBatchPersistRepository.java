package cz.kalas.interview.task.gd.repository.sentence;

import cz.kalas.interview.task.gd.model.entity.Sentence;

import java.util.List;

public interface SentenceBatchPersistRepository {

    void batchPersist(List<Sentence> entityList);
}
