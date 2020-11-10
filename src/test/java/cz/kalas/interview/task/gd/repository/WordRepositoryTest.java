package cz.kalas.interview.task.gd.repository;

import cz.kalas.interview.task.gd.model.WordCategory;
import cz.kalas.interview.task.gd.model.entity.Word;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class WordRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    WordRepository wordRepository;

    Word noun = new Word("Trump", WordCategory.NOUN);
    Word adjective = new Word("recently", WordCategory.ADJECTIVE);
    Word verb = new Word("lost", WordCategory.VERB);

    @Test
    public void getRandomReturnsCorrectWord() {
        testEntityManager.persist(noun);
        assertTrue(wordRepository.getRandom(WordCategory.NOUN).isPresent());
    }

    @Test
    public void getRandomReturnsEmptyResult() {
        testEntityManager.persist(adjective);
        assertTrue(wordRepository.getRandom(WordCategory.NOUN).isEmpty());
    }

    @Test
    public void getRandomReturnsCorrectWordByCategory() {
        testEntityManager.persist(noun);
        testEntityManager.persist(verb);
        assertTrue(wordRepository.getRandom(WordCategory.NOUN).isPresent());
        assertThat(wordRepository.getRandom(WordCategory.NOUN).get(), equalTo(noun));
    }

}