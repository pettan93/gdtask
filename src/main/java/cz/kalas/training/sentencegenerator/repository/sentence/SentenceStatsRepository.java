package cz.kalas.training.sentencegenerator.repository.sentence;

import cz.kalas.training.sentencegenerator.model.entity.Sentence;
import cz.kalas.training.sentencegenerator.model.entity.SentenceStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SentenceStatsRepository extends JpaRepository<SentenceStats, Long> {

    Optional<SentenceStats> findBySentence(Sentence sentence);


}
