package cz.kalas.interview.task.gd.controller;

import cz.kalas.interview.task.gd.domain.sentence.SentenceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/")
public class RootController {

    private final SentenceValidator sentenceValidator;

    @GetMapping()
    public ResponseEntity<Map<String,String>> getInfo() {
                return ResponseEntity.ok(Map.of(
                        "message",
                        "Service alive! Ready to generate sentences based on rules : " +
                                sentenceValidator.getRulesReadable())
                );
    }

}
