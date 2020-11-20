package cz.kalas.interview.task.gd.repository.sentence;

import cz.kalas.interview.task.gd.model.entity.Sentence;
import cz.kalas.interview.task.gd.model.entity.SentenceStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SentenceStatsRepository extends JpaRepository<SentenceStats, Long> {

    Optional<SentenceStats> findBySentence(Sentence sentence);


}
