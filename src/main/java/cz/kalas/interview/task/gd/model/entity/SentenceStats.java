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
public class SentenceStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long displayCount = 0L;

    @OneToOne
    private Sentence sentence;

    public void incrementDisplayCount() {
        this.displayCount++;
    }

    public SentenceStats(Sentence sentence) {
        this.sentence = sentence;
        this.displayCount = 0L;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SentenceStats that = (SentenceStats) o;
        return Objects.equals(displayCount, that.displayCount) &&
                Objects.equals(sentence, that.sentence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayCount, sentence);
    }
}
