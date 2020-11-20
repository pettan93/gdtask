package cz.kalas.interview.task.gd.integration;

import cz.kalas.interview.task.gd.TestUtils;
import cz.kalas.interview.task.gd.model.WordCategory;
import cz.kalas.interview.task.gd.repository.word.WordRepository;
import cz.kalas.interview.task.gd.service.WordService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class WordControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WordService wordService;

    @Autowired
    private WordRepository wordRepository;

    @Test
    public void getWordsEmpty() throws Exception {
        this.mockMvc
                .perform(get("/sentences"))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(("[]")));
        var forbiddenWordsCount = wordRepository.findByForbiddenTrue().size();
        assertThat(wordRepository.findAll(), hasSize(forbiddenWordsCount));
    }

    @Test
    public void wordsPersistedAndReturned() throws Exception {
        var dummySentenceCount = 3;
        for (int i = 0; i < dummySentenceCount; i++) {
            wordService.save(TestUtils.getDummyWord());
        }

        var json = this.mockMvc
                .perform(get("/words"))
                .andExpect(status().isOk())
                .andExpect(json().isPresent())
                .andReturn().getResponse().getContentAsString();

        var forbiddenWordsCount = wordRepository.findByForbiddenTrue().size();
        assertThat(wordRepository.findAll(), hasSize(dummySentenceCount + forbiddenWordsCount));
        assertThatJson(json).isArray().hasSize(dummySentenceCount);
    }

    @Test
    public void wordPersistedAndFoundByItsText() throws Exception {
        var dummyWord = TestUtils.getDummyWord();
        wordService.save(dummyWord);

        var json = this.mockMvc
                .perform(get("/words/" + dummyWord.getText()))
                .andExpect(status().isOk())
                .andExpect(json().isPresent())
                .andReturn().getResponse().getContentAsString();

        assertThat(wordRepository.findByText(dummyWord.getText()).isPresent(), is(true));
        assertThat(wordRepository.findByText(dummyWord.getText()).get(), is(dummyWord));
        assertThatJson(json).inPath("$.word").isObject();
    }

    @Test
    public void nonExistingWordNotFound() throws Exception {
        var nonExistingWord = "Stratovarius";

        var json = this.mockMvc
                .perform(get("/words/" + nonExistingWord))
                .andExpect(status().isNotFound())
                .andExpect(json().isPresent())
                .andReturn().getResponse().getContentAsString();

        assertThat(wordRepository.findByText(nonExistingWord).isEmpty(), is(true));
        assertThatJson(json).isObject().containsKey("error");
    }

    @Test
    public void createWordStoredAndReturned() throws Exception {
        var dummyWord = TestUtils.getDummyWord(WordCategory.VERB);

        var json = this.mockMvc
                .perform(put("/words/" + dummyWord.getText())
                        .content("{ \"word\": { \"wordCategory\": \"VERB\" } }")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(json().isPresent())
                .andReturn().getResponse().getContentAsString();

        assertThat(wordRepository.findByText(dummyWord.getText()).isPresent(), is(true));
        assertThat(wordRepository.findByText(dummyWord.getText()).get(), is(dummyWord));
        assertThatJson(json).inPath("$.word").isObject();
        assertThatJson(json).inPath("$.word.text").asString().isEqualTo(dummyWord.getText());
        assertThatJson(json).inPath("$.word.wordCategory").asString()
                .isEqualTo(dummyWord.getWordCategory().name().toUpperCase());
    }

    @Test
    public void cantStoreForbiddenWord() throws Exception {
        var dummyWord = TestUtils.getDummyWord(WordCategory.VERB);
        wordService.registerForbiddenWords(List.of(dummyWord));

        var json = this.mockMvc
                .perform(put("/words/" + dummyWord.getText())
                        .content("{ \"word\": { \"wordCategory\": \"VERB\" } }")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(json().isPresent())
                .andReturn().getResponse().getContentAsString();

        assertThatJson(json).isObject().containsKey("error");
    }


}