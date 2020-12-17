package cz.kalas.training.sentencegenerator.configuration;

import cz.kalas.training.sentencegenerator.domain.sentence.GoodDataSentenceFactory;
import cz.kalas.training.sentencegenerator.domain.sentence.GoodDataSentenceValidator;
import cz.kalas.training.sentencegenerator.domain.sentence.SentenceFactory;
import cz.kalas.training.sentencegenerator.domain.sentence.SentenceValidator;
import cz.kalas.training.sentencegenerator.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SentenceConfiguration {

    @Bean
    public SentenceFactory sentenceCreator(@Autowired WordService wordService) {
        return new GoodDataSentenceFactory(wordService::getRandom);
    }

    @Bean
    public SentenceValidator sentenceValidator() {
        return new GoodDataSentenceValidator();
    }

}
