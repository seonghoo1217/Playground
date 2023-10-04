package spring.concurrency.repo;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import spring.concurrency.entity.Book;

public interface BookRepository extends JpaRepository<Book,Long> {
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from Book b where b.id = :id")
    Book findByWithPessimisticLock(@Param("id") Long id);

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select b from Book b where b.id = :id")
    Book findByWithOptimisticLock(@Param("id") Long id);
}
