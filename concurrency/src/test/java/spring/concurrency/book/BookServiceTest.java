package spring.concurrency.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.concurrency.entity.Book;
import spring.concurrency.entity.Stock;
import spring.concurrency.repo.BookRepository;
import spring.concurrency.service.BookLockFacade;
import spring.concurrency.service.BookService;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BookServiceTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookLockFacade bookLockFacade;

    @Test
    void 동시에_100명이_책을_구매한다() throws InterruptedException {
        Long bookId = bookRepository.save(new Book("객체지향의 사실과 오해", 18000, new Stock(100)))
                .getId();
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch countDownLatch = new CountDownLatch(100);

        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                try {
                    bookService.purchase(bookId, 1);
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

    @Test
    void 동시에_책_재고_100개_적재() throws InterruptedException {
        Long bookId = bookRepository.save(new Book("객체지향의 사실과 오해", 18000, new Stock(100)))
                .getId();
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch countDownLatch = new CountDownLatch(100);

        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                try {
                    bookService.load(bookId, 1);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        Book actual = bookRepository.findById(bookId)
                .orElseThrow();

        assertThat(actual.getStock().getRemain()).isEqualTo(100);
    }

    @Test
    void 동시에_100명이_책을_구매한다_분산락사용() throws InterruptedException {
        Long bookId = bookRepository.save(new Book("이펙티브 자바", 36_000, new Stock(100)))
                .getId();
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch countDownLatch = new CountDownLatch(100);

        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                try {
                    bookLockFacade.purchase(bookId, 1);
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
