package cz.kalas.interview.task.gd.repository;

import cz.kalas.interview.task.gd.model.WordCategory;
import cz.kalas.interview.task.gd.model.entity.Sentence;
import cz.kalas.interview.task.gd.model.entity.Word;
import cz.kalas.interview.task.gd.repository.sentence.SentenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
public class SentenceRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    SentenceRepository sentenceRepository;

    List<Word> wordsGroupOne;

    List<Word> wordsGroupTwo;

    @BeforeEach
    public void setup() {
        wordsGroupOne = List.of(
                new Word("Petr", WordCategory.NOUN),
                new Word("types", WordCategory.VERB),
                new Word("fast", WordCategory.ADJECTIVE))
                .stream()
                .map(testEntityManager::persist)
                .collect(Collectors.toList());

        wordsGroupTwo = List.of(
                new Word("Marie", WordCategory.NOUN),
                new Word("drives", WordCategory.VERB),
                new Word("great", WordCategory.ADJECTIVE))
                .stream()
                .map(testEntityManager::persist)
                .collect(Collectors.toList());
    }

    @Test
    public void sentenceSavedCorrectly() {
        var newSentence = testEntityManager.persist(new Sentence(wordsGroupOne));
        assertThat(sentenceRepository.count(), equalTo(1L));
        assertThat(newSentence.getCreated(), notNullValue());
    }

    @Test
    public void differentSentencesSavedCorrectly() {
        testEntityManager.persist(new Sentence(wordsGroupOne));
        testEntityManager.persist(new Sentence(wordsGroupTwo));
        assertThat(sentenceRepository.count(), equalTo(2L));
    }

    @Test
    public void sameSentencesSavedCorrectly() {
        testEntityManager.persist(new Sentence(wordsGroupOne));
        testEntityManager.persist(new Sentence(wordsGroupOne));
        assertThat(sentenceRepository.count(), equalTo(2L));
    }

    @Test
    public void findIdsPageableReturnsIds() {
        List<Long> ids = List.of(new Sentence(wordsGroupOne), new Sentence(wordsGroupOne))
                .stream()
                .map(s -> testEntityManager.persist(s).getId())
                .collect(Collectors.toList());
        assertThat(sentenceRepository.findIdsPageable(PageRequest.of(0, 2)).getTotalElements(),
                equalTo((long) ids.size()));
        assertThat(sentenceRepository.findIdsPageable(PageRequest.of(0, 2)).getContent(), equalTo(ids));
    }


}