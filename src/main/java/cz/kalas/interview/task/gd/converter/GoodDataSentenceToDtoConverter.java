package cz.kalas.interview.task.gd.converter;

import cz.kalas.interview.task.gd.exception.SentenceStructureException;
import cz.kalas.interview.task.gd.model.WordCategory;
import cz.kalas.interview.task.gd.model.dto.SentenceDto;
import cz.kalas.interview.task.gd.model.entity.Sentence;
import cz.kalas.interview.task.gd.model.entity.Word;
import cz.kalas.interview.task.gd.model.entity.WordSentenceUsage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class GoodDataSentenceToDtoConverter implements Converter<Sentence, SentenceDto> {

    private final Function<Sentence, Boolean> sentenceValidator;


    @Override
    public SentenceDto convert(Sentence source) {
        if (!sentenceValidator.apply(source)) {
            throw new SentenceStructureException("", source);
        }

        var words = source.getUsedWords()
                .stream()
                .map(WordSentenceUsage::getWord)
                .collect(Collectors.toMap(Word::getWordCategory, Function.identity()));

        return new SentenceDto(
                source.getId(),
                String.format("%s %s %s",
                        words.get(WordCategory.NOUN).getText(),
                        words.get(WordCategory.VERB).getText(),
                        words.get(WordCategory.ADJECTIVE).getText()),
                source.getCreated());
    }
}
