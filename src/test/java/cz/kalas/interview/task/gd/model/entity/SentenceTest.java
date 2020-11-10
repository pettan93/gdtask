package cz.kalas.interview.task.gd.model.entity;

import cz.kalas.interview.task.gd.model.WordCategory;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class SentenceTest {

    String nounText = "Petr";

    String verbText = "drinks";

    String adjectiveText = "slowly";

    @Test
    public void sentenceCreatedNoWords() {
        var sentence = new Sentence(Lists.emptyList());
        assertThat(sentence.getUsedWords().size(), equalTo(0));
        assertThat(sentence.getAsText(), equalTo(""));
    }

    @Test
    public void sentenceCreatedThreeWords() {
        var noun = new Word(nounText, WordCategory.NOUN);
        var verb = new Word(verbText, WordCategory.VERB);
        var adjective = new Word(adjectiveText, WordCategory.ADJECTIVE);

        var sentence = new Sentence(List.of(noun, verb, adjective));

        assertThat(sentence.getUsedWords().size(), equalTo(3));
        assertThat(sentence.getAsText(), equalTo(String.format("%s %s %s", nounText, verbText, adjectiveText)));
    }

    @Test
    public void sentenceCreatedMoreWords() {
        var noun = new Word(nounText, WordCategory.NOUN);
        var verb = new Word(verbText, WordCategory.VERB);
        var adjective = new Word(adjectiveText, WordCategory.ADJECTIVE);

        var sentence = new Sentence(List.of(noun, verb, adjective, noun, verb, adjective));

        assertThat(sentence.getUsedWords().size(), equalTo(6));
        assertThat(sentence.getAsText(),
                equalTo(String.format("%s %s %s %s %s %s",
                        nounText, verbText, adjectiveText, nounText, verbText, adjectiveText)));
    }

}