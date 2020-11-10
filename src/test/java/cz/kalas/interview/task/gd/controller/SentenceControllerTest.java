package cz.kalas.interview.task.gd.controller;

import cz.kalas.interview.task.gd.TestUtils;
import cz.kalas.interview.task.gd.converter.GoodDataSentenceToDtoConverter;
import cz.kalas.interview.task.gd.converter.GoodDataYodaSentenceToDtoConverter;
import cz.kalas.interview.task.gd.domain.sentence.GoodDataSentenceFactory;
import cz.kalas.interview.task.gd.domain.sentence.GoodDataSentenceValidator;
import cz.kalas.interview.task.gd.domain.sentence.SentenceFactory;
import cz.kalas.interview.task.gd.exception.NotFoundException;
import cz.kalas.interview.task.gd.model.dto.SentenceDto;
import cz.kalas.interview.task.gd.model.dto.SentenceYodaDto;
import cz.kalas.interview.task.gd.model.dto.WordDto;
import cz.kalas.interview.task.gd.model.entity.Sentence;
import cz.kalas.interview.task.gd.model.entity.SentenceStats;
import cz.kalas.interview.task.gd.model.entity.Word;
import cz.kalas.interview.task.gd.service.SentenceService;
import cz.kalas.interview.task.gd.service.SentenceStatsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @see SentenceController
 */
public class SentenceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SentenceService sentenceService;

    @Mock
    private SentenceStatsService statsService;

    @Mock
    private ConversionService conversionService;

    private SentenceFactory sentenceFactory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        var sentenceController = new SentenceController(sentenceService, statsService, conversionService);
        var sentenceValidator = new GoodDataSentenceValidator();
        mockMvc = MockMvcBuilders
                .standaloneSetup(sentenceController)
                .setControllerAdvice(new CustomExceptionHandler(sentenceValidator))
                .build();

        sentenceFactory = new GoodDataSentenceFactory(TestUtils::getOptionalDummyWord);
        var sentenceConverter = new GoodDataSentenceToDtoConverter(sentenceValidator::isValid);
        var sentenceYodaConverter = new GoodDataYodaSentenceToDtoConverter(sentenceValidator::isValid);

        when(conversionService.convert(any(), eq(SentenceDto.class)))
                .thenAnswer(w -> sentenceConverter.convert(w.getArgument(0, Sentence.class)));
        when(conversionService.convert(any(), eq(SentenceYodaDto.class)))
                .thenAnswer(w -> sentenceYodaConverter.convert(w.getArgument(0, Sentence.class)));
    }

    @Test
    public void getSentencesEmpty() throws Exception {
        this.mockMvc
                .perform(get("/sentences"))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(("[]")));
    }

    @Test
    public void getSentencesReturnCorrectArray() throws Exception {
        var dummySentencesCount = 3;
        var dummySentences = TestUtils.getDummySentences(sentenceFactory, dummySentencesCount);
        when(sentenceService.getAll()).thenReturn(dummySentences);

        var json = this.mockMvc
                .perform(get("/sentences"))
                .andExpect(status().isOk())
                .andExpect(json().isPresent())
                .andReturn().getResponse().getContentAsString();

        assertThatJson(json).isArray().hasSize(dummySentencesCount);
        assertThatJson(json).inPath("$[0].sentence").isObject();
        assertThatJson(json).inPath("$[0].sentence.text").asString().isEqualTo(dummySentences.get(0).getAsText());
        assertThatJson(json).inPath("$[0].sentence.created").isPresent();
    }


    @Test
    public void getSentencesPageableReturnsPage() throws Exception {
        var pageNo = 0;
        var pageSize = 2;
        var dummySentencesCount = 3;

        var dummySentences = TestUtils.getDummySentences(sentenceFactory, dummySentencesCount);
        when(sentenceService.getAllPageable(pageNo, pageSize))
                .thenReturn(new PageImpl<>(
                        dummySentences.subList(pageNo, pageSize),
                        PageRequest.of(pageNo, pageSize),
                        dummySentencesCount));

        var json = this.mockMvc
                .perform(get(String.format("/sentences/pageable?page=%s&size=%s", pageNo, pageSize)))
                .andExpect(status().isOk())
                .andExpect(json().isPresent())
                .andReturn().getResponse().getContentAsString();
        assertThatJson(json).inPath("$.content").isArray().hasSize(pageSize);
        assertThatJson(json).inPath("$.totalElements").isIntegralNumber().isEqualTo(dummySentencesCount);
        assertThatJson(json).inPath("$.content[0]").isObject();
        assertThatJson(json).inPath("$.content[0].text").asString().isEqualTo(dummySentences.get(0).getAsText());
    }

    @Test
    public void getNonExistingSentence() throws Exception {
        when(sentenceService.getById(any())).thenThrow(new NotFoundException("sample"));

        var nonExistingId = 666;
        var json = this.mockMvc
                .perform(get("/sentences/" + nonExistingId))
                .andExpect(status().isNotFound())
                .andExpect(json().isPresent())
                .andReturn().getResponse().getContentAsString();

        assertThatJson(json).isObject().containsKey("error");
        assertThatJson(json).inPath("$.error").isObject().containsKey("message");
        assertThatJson(json).inPath("$.error").isObject().containsKey("timestamp");
    }

    @Test
    public void getExistingSentence() throws Exception {
        var dummySentence = TestUtils.getDummySentences(sentenceFactory, 1).get(0);
        dummySentence.setId(123L);
        when(sentenceService.getById(any())).thenReturn(dummySentence);
        when(statsService.getStatsBySentence(any())).thenReturn(TestUtils.getDummySentenceStats(dummySentence));

        var json = this.mockMvc
                .perform(get("/sentences/" + dummySentence.getId()))
                .andExpect(status().isOk())
                .andExpect(json().isPresent())
                .andReturn().getResponse().getContentAsString();

        assertThatJson(json).inPath("$.sentence").isObject();
        assertThatJson(json).inPath("$.sentence.id").isEqualTo(dummySentence.getId());
        assertThatJson(json).inPath("$.sentence.text").asString().isEqualTo(dummySentence.getAsText());
        assertThatJson(json).inPath("$.sentence.created").isPresent();
        assertThatJson(json).inPath("$.sentence.showDisplayCount").isPresent();
    }

    @Test
    public void getExistingSentenceYoda() throws Exception {
        var dummySentence = TestUtils.getDummySentences(sentenceFactory, 1).get(0);
        dummySentence.setId(123L);
        when(sentenceService.getById(any())).thenReturn(dummySentence);
        when(statsService.getStatsBySentence(any())).thenReturn(TestUtils.getDummySentenceStats(dummySentence));

        var json = this.mockMvc
                .perform(get("/sentences/" + dummySentence.getId() + "/yodaTalk"))
                .andExpect(status().isOk())
                .andExpect(json().isPresent())
                .andReturn().getResponse().getContentAsString();

        assertThatJson(json).inPath("$.sentence.text").asString().isNotEqualTo(dummySentence.getAsText());
    }

    @Test
    public void generate() throws Exception {
        var dummySentence = TestUtils.getDummySentences(sentenceFactory, 1).get(0);
        dummySentence.setId(123L);
        when(sentenceService.generate()).thenReturn(dummySentence);
        when(statsService.getStatsBySentence(any())).thenReturn(TestUtils.getDummySentenceStats(dummySentence));

        var json = this.mockMvc
                .perform(post("/sentences/generate"))
                .andExpect(status().isOk())
                .andExpect(json().isPresent())
                .andReturn().getResponse().getContentAsString();

        assertThatJson(json).inPath("$.sentence").isObject();
        assertThatJson(json).inPath("$.sentence.id").isEqualTo(dummySentence.getId());
        assertThatJson(json).inPath("$.sentence.text").asString().isEqualTo(dummySentence.getAsText());
        assertThatJson(json).inPath("$.sentence.created").isPresent();
        assertThatJson(json).inPath("$.sentence.showDisplayCount").isPresent();
    }


}