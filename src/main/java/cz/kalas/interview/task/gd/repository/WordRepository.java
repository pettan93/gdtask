package cz.kalas.interview.task.gd.repository;

import cz.kalas.interview.task.gd.model.WordCategory;
import cz.kalas.interview.task.gd.model.entity.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Long>, PagingAndSortingRepository<Word, Long> {

    @Override
    Page<Word> findAll(Pageable pageable);

    Optional<Word> findByText(String text);

    Optional<Word> findByTextAndWordCategory(String text, WordCategory wordCategory);

    List<Word> findByWordCategory(WordCategory category);

    @Query(value =
            "SELECT * FROM word w WHERE w.forbidden = false AND " +
                    " w.word_category = :#{#wordCategory.ordinal()} ORDER BY random() LIMIT 1;",
            nativeQuery = true)
    Optional<Word> getRandom(@Param("wordCategory") WordCategory wordCategory);
}
