package redis.caching.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import redis.caching.entity.Contents;
import redis.caching.service.ContentsService;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/contents")
public class RedisCachingAPI {
    private final ContentsService contentsService;

    @PostMapping
    public void createContents(@RequestBody Contents contents){
        contentsService.createContents(contents);
    }

    @GetMapping("/{id}/caching")
    public Contents getContentsCaching(@PathVariable Long id){
        return contentsService.getContentsByIdUseCaching(id);
    }

    @GetMapping("/{id}/not")
    public Contents getContents(@PathVariable Long id){
        return contentsService.getContentsById(id);
    }
}
