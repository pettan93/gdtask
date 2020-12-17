package cz.kalas.training.sentencegenerator.repository.word;

import cz.kalas.training.sentencegenerator.model.entity.Word;

import java.util.List;

public interface WordBatchPersistRepository {

    void batchPersist(List<Word> entityList);
}

