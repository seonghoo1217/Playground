package spring.concurrency.service;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.concurrency.entity.Book;
import spring.concurrency.repo.BookRepository;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final RedissonClient redissonClient;


    @Transactional
    public void purchase(final Long bookId, final long quantity) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(IllegalArgumentException::new);
        book.purchase(quantity);
    }

    @Transactional
    public synchronized void syncPurchase(final Long bookId, final long quantity) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(IllegalArgumentException::new);
        book.purchase(quantity);
    }

    @Transactional
    public void load(final Long bookId, final long quantity) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(IllegalArgumentException::new);
        book.load(quantity);
    }


    /*@Transactional
    public void purchase(final Long bookId,final long quantity){
        RLock lock = redissonClient.getLock(String.format("purchase:book:%d", bookId));
        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if (!available) {
                System.out.println("redisson getLock timeout");
                return;
            }
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(IllegalArgumentException::new);
            book.purchase(quantity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }*/
}
