package redis.caching.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.caching.entity.Contents;
import redis.caching.repo.ContentsRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Log4j2
public class ContentsService {
    private final ContentsRepository contentsRepository;

    @Transactional
    public void createContents(final Contents contents){
        contentsRepository.save(contents);
    }

    @Cacheable(key = "#id",cacheNames = "contents")
    public Contents getContentsByIdUseCaching(final Long id){
        Contents contents = contentsRepository.findById(id).orElseThrow(() -> new RuntimeException(id + "Not Found Cache Miss"));
        log.info("Fetching Contents Cache Hit:"+contents);
        return contents;
    }
    public Contents getContentsById(final Long id){
        Contents contents = contentsRepository.findById(id).orElseThrow(() -> new RuntimeException(id + "Not Found"));
        log.info("Fetching Contents:"+contents);
        return contents;
    }
}
