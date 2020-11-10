package cz.kalas.interview.task.gd.controller;

import cz.kalas.interview.task.gd.model.dto.DuplicateSentenceStats;
import cz.kalas.interview.task.gd.model.dto.SentenceDto;
import cz.kalas.interview.task.gd.model.dto.SentenceDtoInterface;
import cz.kalas.interview.task.gd.model.dto.SentenceYodaDto;
import cz.kalas.interview.task.gd.service.SentenceService;
import cz.kalas.interview.task.gd.service.SentenceStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sentences")
public class SentenceController {

    private final SentenceService sentenceService;

    private final SentenceStatsService sentenceStatsService;

    private final ConversionService conversionService;

    /**
     * API endpoint is part of assignment, but as data grows it quickly becomes slow
     * Use pageable endpoint instead
     * @return Response containing all sentences in system
     */
    @GetMapping()
    public ResponseEntity<Collection<SentenceDto>> getAll() {
        var sentences = sentenceService.getAll();
        return ResponseEntity.ok(sentences
                .stream()
                .map(sentence -> conversionService.convert(sentence, SentenceDto.class))
                .collect(Collectors.toList()));
    }

    @GetMapping("/pageable")
    public ResponseEntity<Page<SentenceDto>> getAllPageable(
            @RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "size", defaultValue = "10") Integer pageSize) {
        var sentencesPage = sentenceService.getAllPageable(pageNumber, pageSize);
        return ResponseEntity.ok(
                sentencesPage.map(sentence -> conversionService.convert(sentence, SentenceDto.class))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SentenceDto> getById(@PathVariable Long id) {
        var sentence = sentenceService.getById(id);
        var stats = sentenceStatsService.getStatsBySentence(sentence);
        return ResponseEntity.ok(Objects.requireNonNull(
                conversionService.convert(sentence, SentenceDto.class)).fillStats(stats)
        );
    }

    @GetMapping("/{id}/yodaTalk")
    public ResponseEntity<SentenceDtoInterface> getByIdYodaStyle(@PathVariable Long id) {
        return ResponseEntity.ok(
                Objects.requireNonNull(conversionService.convert(sentenceService.getById(id), SentenceYodaDto.class))
        );
    }

    @PostMapping("/generate")
    public ResponseEntity<SentenceDto> generate() {
        var sentence = sentenceService.generate();
        var stats = sentenceStatsService.getStatsBySentence(sentence);
        return ResponseEntity.ok(Objects.requireNonNull(
                conversionService.convert(sentence, SentenceDto.class)).fillStats(stats)
        );
    }

    /**
     * Separate resource for optional task to keep track of exactly same generated sentences
     * @return List of sentence duplicates
     */
    @GetMapping("/duplicates")
    public ResponseEntity<Collection<DuplicateSentenceStats>> getAllDuplicates() {
        return ResponseEntity.ok(sentenceStatsService.getDuplicates());
    }

}
