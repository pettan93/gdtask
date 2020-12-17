package cz.kalas.training.sentencegenerator.repository.sentence;

import cz.kalas.training.sentencegenerator.model.entity.Sentence;

import java.util.List;

public interface SentenceBatchPersistRepository {

    void batchPersist(List<Sentence> entityList);
}
