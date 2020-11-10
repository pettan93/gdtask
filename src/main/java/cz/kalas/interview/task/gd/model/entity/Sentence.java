package cz.kalas.interview.task.gd.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Setter
@NoArgsConstructor
@NamedEntityGraph(
        name = "Sentence.ALL",
        attributeNodes = @NamedAttributeNode(value = "usedWords", subgraph = "word.subgraph"),
        subgraphs = {@NamedSubgraph(name = "word.subgraph", attributeNodes = @NamedAttributeNode(value = "word"))}
)
@Entity
public class Sentence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime created;

    @OneToMany(mappedBy = "sentence",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private Set<WordSentenceUsage> usedWords;

    public Sentence(List<Word> orderedWords) {
        this.created = LocalDateTime.now();
        this.usedWords = IntStream.range(0, orderedWords.size())
                .boxed()
                .map(i -> new WordSentenceUsage(orderedWords.get(i), this, i))
                .collect(Collectors.toSet());
    }

    /**
     * Return sentence with words in natural order as it was created
     *
     * @return Sentence
     */
    public String getAsText() {
        return usedWords.stream()
                .sorted(Comparator.comparing(WordSentenceUsage::getPlace))
                .map(wordSentenceUsage -> wordSentenceUsage.getWord().getText())
                .collect(Collectors.joining(" "));
    }

    // TOSO
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sentence sentence = (Sentence) o;
        return Objects.equals(created, sentence.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(created);
    }

    @Override
    public String toString() {
        return "Sentence{" +
                "id=" + id +
                ", created=" + created +
                ", usedWords=" + usedWords +
                '}';
    }
}


