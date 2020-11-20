package cz.kalas.interview.task.gd.repository.word;

import cz.kalas.interview.task.gd.model.entity.Word;

import java.util.List;

public interface WordBatchPersistRepository {

    void batchPersist(List<Word> entityList);
}

