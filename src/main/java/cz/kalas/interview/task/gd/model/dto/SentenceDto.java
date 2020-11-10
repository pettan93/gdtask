package cz.kalas.interview.task.gd.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import cz.kalas.interview.task.gd.model.entity.Sentence;
import cz.kalas.interview.task.gd.model.entity.SentenceStats;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @see Sentence
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName("sentence")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class SentenceDto implements SentenceDtoInterface {

    private final Long id;

    private final String text;

    private final LocalDateTime created;

    private Long showDisplayCount;

    public SentenceDto(Long id, String text, LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.created = created;
    }

    public SentenceDto(Long id, String text, LocalDateTime created, Long showDisplayCount) {
        this.id = id;
        this.text = text;
        this.created = created;
        this.showDisplayCount = showDisplayCount;
    }

    public SentenceDto fillStats(SentenceStats stats) {
        this.showDisplayCount = stats.getDisplayCount();
        return this;
    }
}
