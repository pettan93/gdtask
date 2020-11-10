package cz.kalas.interview.task.gd.service;

import cz.kalas.interview.task.gd.model.dto.DuplicateSentenceStats;
import cz.kalas.interview.task.gd.model.entity.Sentence;
import cz.kalas.interview.task.gd.model.entity.SentenceStats;

import java.util.Collection;

public interface SentenceStatsService {

    SentenceStats getStatsBySentence(Sentence sentence);

    SentenceStats incrementDisplayCount(Sentence sentence);

    Collection<DuplicateSentenceStats> getDuplicates();
}
