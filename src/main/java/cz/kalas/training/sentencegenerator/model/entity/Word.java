package cz.kalas.training.sentencegenerator.model.entity;

import cz.kalas.training.sentencegenerator.model.WordCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"text", "word_category"})
})
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WORD_SEQ_GENERATOR")
    @SequenceGenerator(name = "WORD_SEQ_GENERATOR", sequenceName = "WORD_SEQ", allocationSize = 100_000)
    private Long id;

    @Size(min = 1, max = 100, message = "Word text must be between {min} and {max}")
    private String text;

    @Column(name = "word_category")
    private WordCategory wordCategory;

    private Boolean forbidden = false;

    public Word(String text, WordCategory wordCategory) {
        this.text = text;
        this.wordCategory = wordCategory;
        this.forbidden = false;
    }

    public Word(String text, WordCategory wordCategory, Boolean forbidden) {
        this.text = text;
        this.wordCategory = wordCategory;
        this.forbidden = forbidden;
    }

    public Boolean isForbidden() {
        return forbidden;
    }

    public Word normalize(Function<String, String> normalizer) {
        this.text = normalizer.apply(this.text);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return Objects.equals(text, word.text) &&
                wordCategory == word.wordCategory;
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, wordCategory);
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", wordCategory=" + wordCategory +
                ", forbidden=" + forbidden +
                '}';
    }
}
