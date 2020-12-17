package cz.kalas.training.sentencegenerator.domain.sentence;

import cz.kalas.training.sentencegenerator.exception.NotEnoughWordsException;
import cz.kalas.training.sentencegenerator.model.WordCategory;
import cz.kalas.training.sentencegenerator.model.entity.Sentence;
import cz.kalas.training.sentencegenerator.model.entity.Word;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public class GoodDataSentenceFactory implements SentenceFactory {

    private final Function<WordCategory, Optional<Word>> randomWordSupplier;

    @Override
    public Sentence createSentence() {
        var noun = randomWordSupplier.apply(WordCategory.NOUN)
                .orElseThrow(() -> new NotEnoughWordsException("Not enough nouns in system."));
        var adjective = randomWordSupplier.apply(WordCategory.VERB)
                .orElseThrow(() -> new NotEnoughWordsException("Not enough verbs in system."));
        var verb = randomWordSupplier.apply(WordCategory.ADJECTIVE)
                .orElseThrow(() -> new NotEnoughWordsException("Not enough adjectives in system."));

        return new Sentence(List.of(noun, adjective, verb));
    }

}
