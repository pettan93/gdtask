package cz.kalas.interview.task.gd;

import cz.kalas.interview.task.gd.domain.word.WordFileLoad;
import cz.kalas.interview.task.gd.model.WordCategory;
import cz.kalas.interview.task.gd.model.entity.Sentence;
import cz.kalas.interview.task.gd.model.entity.Word;
import cz.kalas.interview.task.gd.repository.SentenceRepository;
import cz.kalas.interview.task.gd.repository.WordRepository;
import cz.kalas.interview.task.gd.service.SentenceService;
import cz.kalas.interview.task.gd.service.WordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class BootstrapService {

    public static String FORBIDDEN_WORDS_FILENAME = "forbidden.txt";

    private final WordRepository wordRepository;

    private final WordService wordService;

    private final SentenceRepository sentenceRepository;

    private final SentenceService sentenceService;

    @EventListener(ApplicationReadyEvent.class)
    public void applicationPreparedEventListener() {

        wordService.registerForbiddenWords(
                new WordFileLoad(new File(FORBIDDEN_WORDS_FILENAME)).getWords()
        );


//        List<Word> words = generateRandomWords(20);
//        int step = 100000;
//        for (int i = 0; i < words.size(); i = i + step) {
//            wordRepository.saveAll(words.subList(i, Math.min(i + step, words.size())));
//        }
//
//        generateSentences(10);


//        wordService.registerForbiddenWords(List.of(
//                new Word("fuck", WordCategory.VERB, true),
//                new Word("whore", WordCategory.NOUN, true)
//        ));

//        List<Word> words = generateRandomWords(2_000_000);
//        int step = 100_000;
//        for (int i = 0; i < words.size(); i = i + step) {
//            wordRepository.saveAll(words.subList(i, Math.min(i + step, words.size())));
//        }
//
//        generateSentences(1_000_000);

    }

    public void generateSentences(Integer size) {

        List<Sentence> sentences = new ArrayList<>();

        List<Word> nouns = wordRepository.findByWordCategory(WordCategory.NOUN);
        List<Word> adjectives = wordRepository.findByWordCategory(WordCategory.VERB);
        List<Word> verbs = wordRepository.findByWordCategory(WordCategory.ADJECTIVE);

        for (int s = 0; s < size; s++) {
            sentences.add(new Sentence(List.of(
                    nouns.get(ThreadLocalRandom.current().nextInt(nouns.size())),
                    adjectives.get(ThreadLocalRandom.current().nextInt(adjectives.size())),
                    verbs.get(ThreadLocalRandom.current().nextInt(verbs.size()))
            )));
        }
        int step = 100_000;
        for (int i = 0; i < sentences.size(); i = i + step) {
            sentenceRepository.saveAll(sentences.subList(i, Math.min(i + step, sentences.size())));
        }

    }

    public List<Word> generateRandomWords(Integer size) {
        int[] randomWordCategoryOrdinals = ThreadLocalRandom.current()
                .ints(size, 0, WordCategory.values().length)
                .toArray();
        return IntStream.range(1, size + 1)
                .boxed()
                .map(i -> createDummyWord(i, WordCategory.values()[randomWordCategoryOrdinals[i - 1]]))
                .collect(Collectors.toList());
    }

    private Word createDummyWord(int seed, WordCategory wordCategory) {
        return new Word(seed + wordCategory.name().toLowerCase(), wordCategory);
    }


}
