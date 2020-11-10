package cz.kalas.interview.task.gd.controller;

import cz.kalas.interview.task.gd.TestUtils;
import cz.kalas.interview.task.gd.domain.sentence.GoodDataSentenceValidator;
import cz.kalas.interview.task.gd.exception.NotFoundException;
import cz.kalas.interview.task.gd.model.dto.WordDto;
import cz.kalas.interview.task.gd.model.entity.Word;
import cz.kalas.interview.task.gd.service.WordService;
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
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @see WordController
 */
public class WordControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WordService wordService;

    @Mock
    private ConversionService conversionService;

    @BeforeEach
    public void setup() {
        var modelMapper = new ModelMapper();
        MockitoAnnotations.initMocks(this);
        var wordController = new WordController(wordService, conversionService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(wordController)
                .setControllerAdvice(new CustomExceptionHandler(new GoodDataSentenceValidator()))
                .build();

        when(conversionService.convert(any(), eq(WordDto.class)))
                .thenAnswer(w -> modelMapper.map(w.getArgument(0, Word.class), WordDto.class));
        when(conversionService.convert(any(), eq(Word.class)))
                .thenAnswer(w -> modelMapper.map(w.getArgument(0, WordDto.class), Word.class));
    }

    @Test
    public void getWordsEmpty() throws Exception {
        this.mockMvc
                .perform(get("/words"))
                .andExpect(status().isOk())
                .andExpect(json().isEqualTo(("[]")));
    }

    @Test
    public void getWordsReturnCorrectArray() throws Exception {
        var dummyWordsCount = 3;
        var dummyWords = TestUtils.getDummyWords(dummyWordsCount);
        when(wordService.getAll()).thenReturn(dummyWords);

        var json = this.mockMvc
                .perform(get("/words"))
                .andExpect(status().isOk())
                .andExpect(json().isPresent())
                .andReturn().getResponse().getContentAsString();

        System.out.println("-- " + dummyWords);
        System.out.println("-- " + json);

        assertThatJson(json).isArray().hasSize(dummyWordsCount);
        assertThatJson(json).inPath("$[0].word").isObject();
        assertThatJson(json).inPath("$[0].word.text").asString().isEqualTo(dummyWords.get(0).getText());
        assertThatJson(json).inPath("$[0].word.wordCategory").asString()
                .isEqualTo(dummyWords.get(0).getWordCategory().name().toUpperCase());
    }

    @Test
    public void getWordsPageableReturnsPage() throws Exception {
        var pageNo = 0;
        var pageSize = 2;
        var dummyWordsCount = 3;

        var dummyWords = TestUtils.getDummyWords(dummyWordsCount);
        when(wordService.getAllPageable(pageNo, pageSize))
                .thenReturn(new PageImpl<>(
                        dummyWords.subList(pageNo, pageSize),
                        PageRequest.of(pageNo, pageSize),
                        dummyWordsCount));

        var json = this.mockMvc
                .perform(get(String.format("/words/pageable?page=%s&size=%s", pageNo, pageSize)))
                .andExpect(status().isOk())
                .andExpect(json().isPresent())
                .andReturn().getResponse().getContentAsString();
        assertThatJson(json).inPath("$.content").isArray().hasSize(pageSize);
        assertThatJson(json).inPath("$.totalElements").isIntegralNumber().isEqualTo(dummyWordsCount);
        assertThatJson(json).inPath("$.content[0]").isObject();
        assertThatJson(json).inPath("$.content[0].text").asString().isEqualTo(dummyWords.get(0).getText());
        assertThatJson(json).inPath("$.content[0].wordCategory").asString()
                .isEqualTo(dummyWords.get(0).getWordCategory().name().toUpperCase());
    }

    @Test
    public void getNonExistingWord() throws Exception {
        when(wordService.getWord(any())).thenThrow(new NotFoundException("sample"));

        var searchTerm = "nonExistingWord";
        var json = this.mockMvc
                .perform(get("/words/" + searchTerm))
                .andExpect(status().isNotFound())
                .andExpect(json().isPresent())
                .andReturn().getResponse().getContentAsString();

        assertThatJson(json).isObject().containsKey("error");
        assertThatJson(json).inPath("$.error").isObject().containsKey("message");
        assertThatJson(json).inPath("$.error").isObject().containsKey("timestamp");
    }

    @Test
    public void getExistingWord() throws Exception {
        var dummyWord = TestUtils.getDummyWord();
        when(wordService.getWord(any())).thenReturn(dummyWord);

        var json = this.mockMvc
                .perform(get("/words/" + dummyWord.getText()))
                .andExpect(status().isOk())
                .andExpect(json().isPresent())
                .andReturn().getResponse().getContentAsString();

        assertThatJson(json).inPath("$.word").isObject();
        assertThatJson(json).inPath("$.word.text").asString().isEqualTo(dummyWord.getText());
        assertThatJson(json).inPath("$.word.wordCategory").asString()
                .isEqualTo(dummyWord.getWordCategory().name().toUpperCase());
    }

    @Test
    public void createWord() throws Exception {
        var dummyWord = TestUtils.getDummyWord();
        when(wordService.save(any())).thenReturn(dummyWord);

        var json = this.mockMvc
                .perform(put("/words/" + dummyWord.getText())
                        .content("{ \"word\": { \"wordCategory\": \"VERB\" } }")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(json().isPresent())
                .andReturn().getResponse().getContentAsString();

        assertThatJson(json).inPath("$.word").isObject();
        assertThatJson(json).inPath("$.word.text").asString().isEqualTo(dummyWord.getText());
        assertThatJson(json).inPath("$.word.wordCategory").asString()
                .isEqualTo(dummyWord.getWordCategory().name().toUpperCase());
    }


}