package cz.kalas.training.sentencegenerator.repository;

import cz.kalas.training.sentencegenerator.TestUtils;
import cz.kalas.training.sentencegenerator.model.entity.Word;
import cz.kalas.training.sentencegenerator.repository.sentence.SentenceRepository;
import org.junit.jupiter.api.Disabled;
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
    @Disabled
    public void batchGenerateSentences() {
        List<Word> dummyWords = TestUtils.getDummyWords(100_000);
        sentenceRepository.batchPersist(new ArrayList<>());
        // TODO implement asserts
        // TODO measure runtime time
    }



}