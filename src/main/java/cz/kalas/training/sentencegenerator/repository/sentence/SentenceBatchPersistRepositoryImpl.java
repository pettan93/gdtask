package cz.kalas.training.sentencegenerator.repository.sentence;


import cz.kalas.training.sentencegenerator.model.entity.Sentence;
import cz.kalas.training.sentencegenerator.repository.word.WordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

@Slf4j
@Component
public class SentenceBatchPersistRepositoryImpl implements SentenceBatchPersistRepository {

    private final EntityManager em;

    private final WordRepository wordRepository;

    public SentenceBatchPersistRepositoryImpl(EntityManagerFactory entityManagerFactory, WordRepository wordRepository) {
        this.em = entityManagerFactory.createEntityManager();
        this.wordRepository = wordRepository;
    }

    @Override
    public void batchPersist(List<Sentence> sentences) {
        // todo implement
        throw new IllegalStateException("Not implemented yet");
    }
}
