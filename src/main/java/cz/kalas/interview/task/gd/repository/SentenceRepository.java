package cz.kalas.interview.task.gd.repository;

import cz.kalas.interview.task.gd.model.entity.Sentence;
import cz.kalas.interview.task.gd.model.projection.DuplicateSentenceEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Set;

public interface SentenceRepository extends JpaRepository<Sentence, Long>, PagingAndSortingRepository<Sentence, Long> {

    @EntityGraph(value = "Sentence.ALL", type = EntityGraph.EntityGraphType.FETCH)
    @Override
    List<Sentence> findAll();

    @Query(value = "SELECT s.id FROM Sentence s")
    Page<Long> findIdsPageable(Pageable pageable);

    @EntityGraph(value = "Sentence.ALL", type = EntityGraph.EntityGraphType.FETCH)
    List<Sentence> findByIdIn(Set<Long> ids);


    @Query(nativeQuery = true)
    List<DuplicateSentenceEntry> getDuplicateSentences();

}
