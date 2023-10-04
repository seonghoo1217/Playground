package spring.concurrency.lock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.concurrency.entity.Book;
import spring.concurrency.entity.Stock;
import spring.concurrency.facade.BookOptimisticFacade;
import spring.concurrency.repo.BookRepository;
import spring.concurrency.service.BookOptimisticService;
import spring.concurrency.service.BookPessimisticService;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OptimisticTest {

    @Autowired
    private BookOptimisticFacade optimisticFacade;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void OptimisticLock_동시에_100개의_요청() throws InterruptedException {
        Long bookId = bookRepository.save(new Book("객체지향의 사실과 오해", 18000, new Stock(100)))
                .getId();
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch countDownLatch = new CountDownLatch(100);

        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                try {
                    optimisticFacade.purchaseByOptimisticLock(bookId,1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
