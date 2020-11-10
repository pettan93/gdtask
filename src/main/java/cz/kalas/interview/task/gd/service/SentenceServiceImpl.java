package cz.kalas.interview.task.gd.service;

import cz.kalas.interview.task.gd.domain.sentence.SentenceFactory;
import cz.kalas.interview.task.gd.domain.sentence.SentenceValidator;
import cz.kalas.interview.task.gd.exception.NotFoundException;
import cz.kalas.interview.task.gd.exception.SentenceStructureException;
import cz.kalas.interview.task.gd.model.entity.Sentence;
import cz.kalas.interview.task.gd.repository.SentenceRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SentenceServiceImpl implements SentenceService {

    private final SentenceStatsServiceImpl sentenceStatsService;

    private final SentenceRepository sentenceRepository;

    private final SentenceFactory sentenceFactory;

    private final SentenceValidator sentenceValidator;

    @Override
    public Collection<Sentence> getAll() {
        return sentenceRepository.findAll();
    }

    /**
     * Pageable sentence querying
     * <p>
     * Custom pagination implemented there.
     * Method queries ids of sentence in pageable way and fetches collection of Sentence by obtained set of ids.
     * The reason for this approach is bad Hibernate support of paging entity graphs, which leads to N+1 queries
     * problem or 'HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!' problem which
     * indicates fetching all data to memory and applying paginating there.
     * These problems becomes significant witch large datasets, otherwise can't be easily overlooked.
     *
     * @param pageNumber Page no.
     * @param pageSize   Desired result size
     * @return Page of sentences
     * @see <a href="https://in.relation.to/2019/01/21/hibernate-community-newsletter-2019-02/">More about problem</a>
     * @see <a href="https://vladmihalcea.com/fix-hibernate-hhh000104-entity-fetch-pagination-warning-message/">More about problem</a>
     */
    @Override
    public Page<Sentence> getAllPageable(Integer pageNumber, Integer pageSize) {
        var ids = sentenceRepository.findIdsPageable(PageRequest.of(pageNumber, pageSize));
        var idMap = sentenceRepository.findByIdIn(ids.toSet()).stream()
                .collect(Collectors.toMap(Sentence::getId, Function.identity()));
        return ids.map(idMap::get);
    }

    @Transactional
    @Override
    public Sentence generate() {
        var sentence = sentenceFactory.createSentence();
        if (!sentenceValidator.isValid(sentence)) {
            throw new SentenceStructureException("Structure of randomly generated sentence is malformed", sentence);
        }

        var savedSentence = sentenceRepository.save(sentence);
        sentenceStatsService.incrementDisplayCount(savedSentence);
        return savedSentence;
    }

    @Override
    public Sentence getById(Long id) {
        var resolvedSentence = sentenceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Sentence with id %s was not found", id)));
        sentenceStatsService.incrementDisplayCount(resolvedSentence);
        return resolvedSentence;
    }


}
