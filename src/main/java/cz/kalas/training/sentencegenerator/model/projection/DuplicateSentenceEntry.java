package cz.kalas.training.sentencegenerator.model.projection;

public interface DuplicateSentenceEntry {

    Long getSentenceId();

    String getSentenceHash();

    Integer getDuplicateCount();

}
