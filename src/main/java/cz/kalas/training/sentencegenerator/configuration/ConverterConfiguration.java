package cz.kalas.training.sentencegenerator.configuration;

import cz.kalas.training.sentencegenerator.converter.GoodDataSentenceToDtoConverter;
import cz.kalas.training.sentencegenerator.converter.GoodDataYodaSentenceToDtoConverter;
import cz.kalas.training.sentencegenerator.domain.sentence.SentenceValidator;
import cz.kalas.training.sentencegenerator.model.dto.SentenceDto;
import cz.kalas.training.sentencegenerator.model.dto.SentenceYodaDto;
import cz.kalas.training.sentencegenerator.model.dto.WordDto;
import cz.kalas.training.sentencegenerator.model.entity.Sentence;
import cz.kalas.training.sentencegenerator.model.entity.Word;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class ConverterConfiguration implements WebMvcConfigurer {

    @Bean
    public Converter<Sentence, SentenceDto> sentenceToDtoConverter(
            @Autowired SentenceValidator sentenceValidator) {
        return new GoodDataSentenceToDtoConverter(sentenceValidator::isValid);
    }

    @Bean
    public Converter<Sentence, SentenceYodaDto> sentenceToYodaDtoConverter(
            @Autowired SentenceValidator sentenceValidator) {
        return new GoodDataYodaSentenceToDtoConverter(sentenceValidator::isValid);
    }

    @Bean
    public Converter<Word, WordDto> wordToDtoConverter() {
        // cant be shortened, Spring bean initialization problem
        return new Converter<Word, WordDto>() {
            @Override
            public WordDto convert(Word source) {
                return new ModelMapper().map(source, WordDto.class);
            }
        };
    }

    @Bean
    public Converter<WordDto, Word> wordFromDtoConverter() {
        // cant be shortened, Spring bean initialization problem
        return new Converter<WordDto, Word>() {
            @Override
            public Word convert(WordDto source) {
                return new ModelMapper().map(source, Word.class);
            }
        };
    }


}