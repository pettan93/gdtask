package cz.kalas.training.sentencegenerator.repository.word;

import cz.kalas.training.sentencegenerator.model.entity.Word;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class WordBatchPersistRepositoryImpl implements WordBatchPersistRepository {

    private final static int persistBatchSize = 100_000;

    private final WordRepository wordRepository;

    @Override
    public void batchPersist(List<Word> entityList) {
        for (int i = 0; i < entityList.size(); i = i + persistBatchSize) {
            log.debug("persisting generated words..");
            wordRepository.saveAll(entityList.subList(i, Math.min(i + persistBatchSize, entityList.size())));
        }
    }
}
