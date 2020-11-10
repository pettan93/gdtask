package cz.kalas.interview.task.gd.configuration;

import cz.kalas.interview.task.gd.domain.sentence.GoodDataSentenceFactory;
import cz.kalas.interview.task.gd.domain.sentence.GoodDataSentenceValidator;
import cz.kalas.interview.task.gd.domain.sentence.SentenceFactory;
import cz.kalas.interview.task.gd.domain.sentence.SentenceValidator;
import cz.kalas.interview.task.gd.service.WordService;
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
