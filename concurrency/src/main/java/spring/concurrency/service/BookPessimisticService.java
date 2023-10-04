package spring.concurrency.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.concurrency.entity.Book;
import spring.concurrency.repo.BookRepository;

@Service
@RequiredArgsConstructor
public class BookPessimisticService {

    private final BookRepository bookRepository;

    @Transactional
    public void purchaseByPessimisticLock(final Long bookId,final long quantity){
        Book book = bookRepository.findByWithPessimisticLock(bookId);
        book.purchase(quantity);
    }
}
