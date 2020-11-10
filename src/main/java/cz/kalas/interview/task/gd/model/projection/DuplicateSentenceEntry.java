package cz.kalas.interview.task.gd.model.projection;

public interface DuplicateSentenceEntry {

    Long getSentenceId();

    String getSentenceHash();

    Integer getDuplicateCount();

}
