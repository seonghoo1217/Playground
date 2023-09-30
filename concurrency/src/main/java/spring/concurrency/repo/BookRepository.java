package spring.concurrency.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.concurrency.entity.Book;

public interface BookRepository extends JpaRepository<Book,Long> {
}
