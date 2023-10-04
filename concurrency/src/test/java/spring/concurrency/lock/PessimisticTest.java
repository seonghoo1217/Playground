package spring.concurrency.lock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.concurrency.entity.Book;
import spring.concurrency.entity.Stock;
import spring.concurrency.repo.BookRepository;
import spring.concurrency.service.BookService;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PessimisticTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void PessimisticLock_동시에_100개의_요청() throws InterruptedException {
        Long bookId = bookRepository.save(new Book("객체지향의 사실과 오해", 18000, new Stock(100)))
                .getId();
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch countDownLatch = new CountDownLatch(100);

        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                try {
                    bookService.purchaseByPessimisticLock(bookId, 1);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        Book actual = bookRepository.findById(bookId)
                .orElseThrow();

        assertThat(actual.getStock().getRemain()).isZero();
    }
}
