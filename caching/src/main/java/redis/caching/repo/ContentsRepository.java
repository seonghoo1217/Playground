package redis.caching.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import redis.caching.entity.Contents;

public interface ContentsRepository extends JpaRepository<Contents,Long> {
}
