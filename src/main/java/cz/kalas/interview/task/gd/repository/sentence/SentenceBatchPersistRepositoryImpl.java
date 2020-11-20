package cz.kalas.interview.task.gd.repository.sentence;


import cz.kalas.interview.task.gd.model.entity.Sentence;
import cz.kalas.interview.task.gd.repository.word.WordRepository;
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
    }
}
