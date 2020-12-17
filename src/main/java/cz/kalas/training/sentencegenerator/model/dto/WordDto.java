package cz.kalas.training.sentencegenerator.model.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import cz.kalas.training.sentencegenerator.model.WordCategory;
import cz.kalas.training.sentencegenerator.model.entity.Word;
import lombok.Data;

/**
 * @see Word
 */
@Data
@JsonTypeName("word")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class WordDto {

    private String text;

    private WordCategory wordCategory;

}
