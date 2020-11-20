package cz.kalas.interview.task.gd.integration;

import cz.kalas.interview.task.gd.TestUtils;
import cz.kalas.interview.task.gd.domain.sentence.GoodDataSentenceFactory;
import cz.kalas.interview.task.gd.domain.sentence.GoodDataSentenceValidator;
import cz.kalas.interview.task.gd.domain.sentence.SentenceFactory;
import cz.kalas.interview.task.gd.domain.sentence.SentenceValidator;
import cz.kalas.interview.task.gd.model.entity.WordSentenceUsage;
import cz.kalas.interview.task.gd.repository.sentence.SentenceRepository;
import cz.kalas.interview.task.gd.service.WordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Collectors;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SentenceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WordService wordService;

    @Autowired
    private SentenceRepository sentenceRepository;

    private SentenceFactory sentenceFactory;

    private SentenceValidator sentenceValidator;

    @BeforeEach
    public void setup() {
        sentenceValidator = new GoodDataSentenceValidator();
        sentenceFactory = new GoodDataSentenceFactory(TestUtils::getOptionalDummyWord);
    }

    @Test
    public void getSentencesEmpty() throws Exception {
        this.mockMvc
                .perform(get("/sentences"))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(("[]")));
    }

    @Test
    public void cantGetNonExistingSentenceById() throws Exception {
        var json = this.mockMvc
                .perform(get("/sentences/666"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        assertThatJson(json).isObject().containsKey("error");
        assertThatJson(json).inPath("$.error").isObject().containsKey("message");
    }

    @Test
    public void cantGetNonExistingSentenceInYodaById() throws Exception {
        var json = this.mockMvc
                .perform(get("/sentences/666/yodaTalk"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        assertThatJson(json).isObject().containsKey("error");
        assertThatJson(json).inPath("$.error").isObject().containsKey("message");
    }

    @Test
    public void cantGenerateSentenceWithoutWords() throws Exception {
        var json = this.mockMvc
                .perform(post("/sentences/generate"))
                .andExpect(status().is5xxServerError())
                .andReturn().getResponse().getContentAsString();

        assertThat(sentenceRepository.findAll(), hasSize(0));
        assertThatJson(json).isObject().containsKey("error");
        assertThatJson(json).inPath("$.error").isObject().containsKey("message");
    }

    @Test
    public void cantGenerateSentenceWithoutRightWords() throws Exception {
        wordService.registerForbiddenWords(TestUtils.getDummyWords(20));
        var json = this.mockMvc
                .perform(post("/sentences/generate"))
                .andExpect(status().is5xxServerError())
                .andReturn().getResponse().getContentAsString();

        assertThat(sentenceRepository.findAll(), hasSize(0));
        assertThatJson(json).isObject().containsKey("error");
        assertThatJson(json).inPath("$.error").isObject().containsKey("message");
    }

    @Test
    public void canGenerateSentenceFromWords() throws Exception {
        var validSentence = TestUtils.getDummySentences(sentenceFactory, 1).get(0);
        wordService.saveAll(validSentence.getUsedWords()
                .stream().map(WordSentenceUsage::getWord)
                .collect(Collectors.toList()));

        var json = this.mockMvc
                .perform(post("/sentences/generate"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var generatedSentence = sentenceRepository.findAll().get(0);
        assertThat(generatedSentence, notNullValue());
        assertThat(sentenceRepository.findAll(), hasSize(1));
        assertThatJson(json).inPath("$.sentence").isObject();
        assertThatJson(json).inPath("$.sentence.id").isEqualTo(generatedSentence.getId());
        assertThatJson(json).inPath("$.sentence.text").asString().isEqualTo(generatedSentence.getAsText());
        assertThatJson(json).inPath("$.sentence.created").isPresent();
        assertThatJson(json).inPath("$.sentence.showDisplayCount").isEqualTo(1);
    }


    @Test
    public void getSentencesReturnCorrectArray() throws Exception {
        var dummySentenceCount = 3;
        sentenceRepository.saveAll(TestUtils.getDummySentences(sentenceFactory, dummySentenceCount));

        var json = this.mockMvc
                .perform(get("/sentences"))
                .andExpect(status().isOk())
                .andExpect(json().isPresent())
                .andReturn().getResponse().getContentAsString();

        assertThat(sentenceRepository.findAll(), hasSize(dummySentenceCount));
        assertThatJson(json).isArray().hasSize(dummySentenceCount);
    }

    @Test
    public void cantGetGeneratedSentenceByAndIncrementViewCount() throws Exception {
        var validSentence = TestUtils.getDummySentences(sentenceFactory, 1).get(0);
        wordService.saveAll(validSentence.getUsedWords()
                .stream().map(WordSentenceUsage::getWord)
                .collect(Collectors.toList()));

        this.mockMvc.perform(post("/sentences/generate"));

        var generatedSentence = sentenceRepository.findAll().get(0);

        var json = this.mockMvc
                .perform(get("/sentences/" + generatedSentence.getId()))
                .andExpect(status().isOk())
                .andExpect(json().isPresent())
                .andReturn().getResponse().getContentAsString();

        assertThatJson(json).inPath("$.sentence").isObject();
        assertThatJson(json).inPath("$.sentence.id").isEqualTo(generatedSentence.getId());
        assertThatJson(json).inPath("$.sentence.text").asString().isEqualTo(generatedSentence.getAsText());
        assertThatJson(json).inPath("$.sentence.created").isPresent();
        assertThatJson(json).inPath("$.sentence.showDisplayCount").isEqualTo(2);
    }

    @Test
    public void canGenerateIdenticalSentences() throws Exception {
        var validSentence = TestUtils.getDummySentences(sentenceFactory, 1).get(0);
        wordService.saveAll(validSentence.getUsedWords()
                .stream().map(WordSentenceUsage::getWord)
                .collect(Collectors.toList()));

        this.mockMvc.perform(post("/sentences/generate"));
        this.mockMvc.perform(post("/sentences/generate"));
        this.mockMvc.perform(post("/sentences/generate"));
        this.mockMvc.perform(post("/sentences/generate"));

        assertThat(sentenceRepository.findAll(), hasSize(4));
    }

    @Test
    public void cantGenerateSentencesFromForbiddenWords() throws Exception {
        var validSentence = TestUtils.getDummySentences(sentenceFactory, 1).get(0);
        wordService.registerForbiddenWords(validSentence.getUsedWords()
                .stream().map(WordSentenceUsage::getWord)
                .collect(Collectors.toList()));

        this.mockMvc.perform(post("/sentences/generate"));

        assertThat(sentenceRepository.findAll(), hasSize(0));
    }


}