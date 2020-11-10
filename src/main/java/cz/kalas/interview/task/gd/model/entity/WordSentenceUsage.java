package cz.kalas.interview.task.gd.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class WordSentenceUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "id_sentence")
    private Sentence sentence;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_word")
    private Word word;

    private Integer place;

    public WordSentenceUsage(Word word, Sentence sentence, Integer place) {
        this.sentence = sentence;
        this.word = word;
        this.place = place;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordSentenceUsage that = (WordSentenceUsage) o;
        return Objects.equals(sentence, that.sentence) &&
                Objects.equals(word, that.word) &&
                Objects.equals(place, that.place);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sentence, word, place);
    }


}
