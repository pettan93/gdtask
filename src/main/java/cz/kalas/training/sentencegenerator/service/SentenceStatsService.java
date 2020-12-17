package cz.kalas.training.sentencegenerator.service;

import cz.kalas.training.sentencegenerator.model.dto.DuplicateSentenceStats;
import cz.kalas.training.sentencegenerator.model.entity.Sentence;
import cz.kalas.training.sentencegenerator.model.entity.SentenceStats;

import java.util.Collection;

public interface SentenceStatsService {

    SentenceStats getStatsBySentence(Sentence sentence);

    SentenceStats incrementDisplayCount(Sentence sentence);

    Collection<DuplicateSentenceStats> getDuplicates();
}
