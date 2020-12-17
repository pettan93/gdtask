package cz.kalas.training.sentencegenerator;

import cz.kalas.training.sentencegenerator.domain.word.RandomWordLoad;
import cz.kalas.training.sentencegenerator.domain.word.WordFileLoad;
import cz.kalas.training.sentencegenerator.repository.word.WordRepository;
import cz.kalas.training.sentencegenerator.service.WordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class BootstrapService {

    public static String FORBIDDEN_WORDS_FILENAME = "forbidden.txt";

    private final WordService wordService;

    private final WordRepository wordRepository;

    @Value("${app.random.words}")
    public Integer randomWords;

    @EventListener(ApplicationReadyEvent.class)
    public void applicationPreparedEventListener() {
        loadForbiddenWords();
        loadRandomWords();
    }

    private void loadForbiddenWords() {
        wordService.registerForbiddenWords(
                new WordFileLoad(new File(FORBIDDEN_WORDS_FILENAME)).getWords()
        );
    }

    private void loadRandomWords() {
        var words = new RandomWordLoad(randomWords).getWords();
        log.debug("Generated {} words", words.size());
        wordRepository.batchPersist(new ArrayList<>(words));
    }


}
