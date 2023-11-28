package example.indexing.repo;

import example.indexing.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findByTitle(String title);

    Post findByUuid(UUID uuid);

    Post findByTitleAndCategory(String title, String category);
}
