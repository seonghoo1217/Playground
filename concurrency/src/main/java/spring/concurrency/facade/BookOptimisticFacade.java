package spring.concurrency.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.concurrency.service.BookOptimisticService;

@Service
@RequiredArgsConstructor
public class BookOptimisticFacade {

    private final BookOptimisticService optimisticService;

    public void purchaseByOptimisticLock(final Long bookId,final long quantity) throws InterruptedException{
        while (true){
            try{
                optimisticService.purchaseByOptimisticLock(bookId,quantity);
                break;
            }catch (Exception e){
                System.out.println("Err:"+e.getMessage());
            }
        }
    }
}
