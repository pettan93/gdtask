package cz.kalas.interview.task.gd.repository;

import cz.kalas.interview.task.gd.domain.word.RandomWordLoad;
import cz.kalas.interview.task.gd.model.WordCategory;
import cz.kalas.interview.task.gd.repository.word.WordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles(profiles = "postgres")
public class WordBatchPersistRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    WordRepository wordRepository;

    @Test
    public void getRandomReturnsCorrectWord() {
        wordRepository.batchPersist(new ArrayList<>(new RandomWordLoad(100_000).getWords()));
        assertTrue(wordRepository.getRandom(WordCategory.NOUN).isPresent());
        // TODO implements assertions
    }

}