package cz.kalas.interview.task.gd;

import cz.kalas.interview.task.gd.domain.word.WordFileLoad;
import cz.kalas.interview.task.gd.service.WordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;

@Slf4j
@Service
@RequiredArgsConstructor
public class BootstrapService {

    public static String FORBIDDEN_WORDS_FILENAME = "forbidden.txt";

    private final WordService wordService;

    @EventListener(ApplicationReadyEvent.class)
    public void applicationPreparedEventListener() {
        loadForbiddenWords();
    }

    public void loadForbiddenWords() {
        wordService.registerForbiddenWords(
                new WordFileLoad(new File(FORBIDDEN_WORDS_FILENAME)).getWords()
        );
    }

}
