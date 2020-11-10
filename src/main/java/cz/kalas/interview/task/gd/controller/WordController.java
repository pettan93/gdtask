package cz.kalas.interview.task.gd.controller;

import cz.kalas.interview.task.gd.model.dto.WordDto;
import cz.kalas.interview.task.gd.model.entity.Word;
import cz.kalas.interview.task.gd.service.WordService;
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
@RequestMapping("/words")
public class WordController {

    private final WordService wordService;

    private final ConversionService conversionService;

    @GetMapping()
    public ResponseEntity<Collection<WordDto>> getAllWords() {
        var words = wordService.getAll();
        return ResponseEntity.ok(words
                .stream()
                .map(word -> conversionService.convert(word, WordDto.class))
                .collect(Collectors.toList()));
    }

    @GetMapping("/pageable")
    public ResponseEntity<Page<WordDto>> getAllWordsPageable(
            @RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "size", defaultValue = "10") Integer pageSize) {
        var words = wordService.getAllPageable(pageNumber, pageSize);
        return ResponseEntity.ok(words.map(word -> conversionService.convert(word, WordDto.class)));
    }

    @GetMapping("/{word}")
    public ResponseEntity<WordDto> getWord(@PathVariable String word) {
        var resolvedWord = wordService.getWord(word);
        return ResponseEntity.ok(Objects.requireNonNull(conversionService.convert(resolvedWord, WordDto.class)));
    }

    @PutMapping("/{word}")
    public ResponseEntity<WordDto> createWord(@PathVariable(name = "word") String text, @RequestBody WordDto wordDto) {
        wordDto.setText(text);
        var savedWord = wordService.save(conversionService.convert(wordDto, Word.class));
        return ResponseEntity.ok(Objects.requireNonNull(conversionService.convert(savedWord, WordDto.class)));
    }

}
