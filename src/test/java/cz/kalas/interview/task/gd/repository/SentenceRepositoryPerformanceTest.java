package cz.kalas.interview.task.gd.repository;

import cz.kalas.interview.task.gd.TestUtils;
import cz.kalas.interview.task.gd.model.entity.Word;
import cz.kalas.interview.task.gd.repository.sentence.SentenceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
public class SentenceRepositoryPerformanceTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    SentenceRepository sentenceRepository;


    @Test
    public void batchGenerateSentences() {
        List<Word> dummyWords = TestUtils.getDummyWords(100_000);
        sentenceRepository.batchPersist(new ArrayList<>());
        // TODO implement asserts
    }



}