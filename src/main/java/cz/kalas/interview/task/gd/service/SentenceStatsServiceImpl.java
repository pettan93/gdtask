package cz.kalas.interview.task.gd.service;

import cz.kalas.interview.task.gd.model.dto.DuplicateSentenceStats;
import cz.kalas.interview.task.gd.model.entity.Sentence;
import cz.kalas.interview.task.gd.model.entity.SentenceStats;
import cz.kalas.interview.task.gd.model.projection.DuplicateSentenceEntry;
import cz.kalas.interview.task.gd.repository.sentence.SentenceRepository;
import cz.kalas.interview.task.gd.repository.sentence.SentenceStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SentenceStatsServiceImpl implements SentenceStatsService {

    private final SentenceStatsRepository statsRepository;

    private final SentenceRepository sentenceRepository;

    @Override
    public SentenceStats getStatsBySentence(Sentence sentence) {
        return statsRepository.findBySentence(sentence)
                .orElseGet(() -> incrementDisplayCount(sentence));
    }

    @Override
    public Collection<DuplicateSentenceStats> getDuplicates() {
        List<DuplicateSentenceEntry> duplicateSentenceEntries = sentenceRepository.getDuplicateSentences();

        Map<String, List<Long>> hashIdsMap = new HashMap<>();
        duplicateSentenceEntries.forEach((e) ->
                hashIdsMap.computeIfAbsent(e.getSentenceHash(), k -> new ArrayList<>()).add(e.getSentenceId())
        );

        return duplicateSentenceEntries
                .stream()
                .collect(Collectors.toMap(
                        DuplicateSentenceEntry::getSentenceHash,
                        Function.identity(),
                        (a1, a2) -> a1))
                .values()
                .stream()
                .map(e -> new DuplicateSentenceStats(
                        sentenceRepository.getOne(e.getSentenceId()).getAsText(),
                        e.getDuplicateCount(),
                        hashIdsMap.get(e.getSentenceHash())))
                .collect(Collectors.toList());
    }


    @Override
    public SentenceStats incrementDisplayCount(Sentence sentence) {
        var sentenceStats = statsRepository.findBySentence(sentence)
                .orElseGet(() -> new SentenceStats(sentence));
        sentenceStats.incrementDisplayCount();
        return statsRepository.save(sentenceStats);
    }
}
