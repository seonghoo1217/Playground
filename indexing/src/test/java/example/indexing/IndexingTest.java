package example.indexing;

import example.indexing.entity.Member;
import example.indexing.repo.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.Random;
import java.util.UUID;

@SpringBootTest
public class IndexingTest {

    @Autowired
    private MemberRepository memberRepository;


    @Test
    public void generateMember() {
        Random random = new Random();
        for (int i = 1; i <= 100000; i++) {
            Member member = new Member(UUID.randomUUID(), "test" + i + "@eamil.com", "testuser" + i, "1234", random.nextInt(61) + 20, "서울시 중앙" + i + "동");
            memberRepository.save(member);
        }
    }

    @Test
    public void basicSelect() {
        //given
        String targetEmail = "test59000@eamil.com";

        StopWatch stopWatch = new StopWatch("Index Full Scan");

        stopWatch.start();
        Member findMember = memberRepository.findAllByEmail(targetEmail);
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
        System.out.println("마지막 작업 걸린 시간 : " + stopWatch.getTotalTimeNanos());
        System.out.println("totalTimeSeconds : " + stopWatch.getTotalTimeSeconds());
    }
}
