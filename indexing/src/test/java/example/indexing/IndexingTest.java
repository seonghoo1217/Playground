package example.indexing;

import example.indexing.entity.Member;
import example.indexing.entity.Post;
import example.indexing.entity.Posting;
import example.indexing.repo.MemberRepository;
import example.indexing.repo.PostRepository;
import example.indexing.repo.PostingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@SpringBootTest
public class IndexingTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostingRepository postingRepository;

    private List<String> categories = List.of("자유", "유머", "연애", "고민");

    private final Random random = new Random();

    @Test
    public void initializeData() {
        initializeMember();
        initializePost();
        initializePosting();
    }

    public void initializeMember() {
        Random random = new Random();
        for (int i = 1; i <= 100000; i++) {
            Member member = new Member("test" + i + "@eamil.com", "testuser" + i, "1234", random.nextInt(61) + 20, "서울시 중앙" + i + "동");
            memberRepository.save(member);
        }
    }

    private void initializePost() {
        StopWatch stopWatch = new StopWatch("Post Index 데이터 삽입 시간 기록");

        stopWatch.start();
        for (int i = 1; i <= 100000; i++) {
            String category = categories.get(random.nextInt(4));
            Member member = memberRepository.findById((long) i).get();
            Post post = new Post(UUID.randomUUID(), "test post" + i, category, member);
            postRepository.save(post);
        }
        stopWatch.stop();

        stopWatchRecordPrint(stopWatch);
    }

    private void initializePosting() {
        StopWatch stopWatch = new StopWatch("Postingx 데이터 삽입 시간 기록");

        stopWatch.start();
        for (int i = 1; i <= 100000; i++) {
            String category = categories.get(random.nextInt(4));
            Member member = memberRepository.findById((long) i).get();
            Posting posting = new Posting(UUID.randomUUID(), "test post" + i, category, member);
            postingRepository.save(posting);
        }
        stopWatch.stop();

        stopWatchRecordPrint(stopWatch);
    }

    @Test
    public void indexFullScan() {
        //given
        String targetEmail = "test59000@eamil.com";

        StopWatch stopWatch = new StopWatch("Unique Index Scan");

        stopWatch.start();
        Member findMember = memberRepository.findAllByEmail(targetEmail);
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
        System.out.println("마지막 작업 걸린 시간 : " + stopWatch.getTotalTimeNanos());
        System.out.println("totalTimeSeconds : " + stopWatch.getTotalTimeSeconds());
    }

    @Test
    public void tableFullScan() {
        String targetEmail = "test59000@eamil.com";

        StopWatch stopWatch = new StopWatch("Table Full Scan");

        stopWatch.start();
        Optional<Member> findMember = memberRepository.findAll().stream().filter(member -> member.getEmail().equals(targetEmail)).findFirst();
        stopWatch.stop();

        stopWatchRecordPrint(stopWatch);
    }

    @Test
    public void uniqueIndexScan() {
        String targetEmail = "test59000@eamil.com";

        StopWatch stopWatch = new StopWatch("Unique Index Scan");

        stopWatch.start();
        Member byEmail = memberRepository.findByEmail(targetEmail);
        stopWatch.stop();

        stopWatchRecordPrint(stopWatch);
    }

    @Test
    public void uniqueIndexScanCombine() {
        String targetEmail = "test59000@eamil.com";
        String targetNickName = "testuser59000";

        StopWatch stopWatch = new StopWatch("Unique Index Scan Combine");

        stopWatch.start();
        Member member = memberRepository.findByEmailAndNickname(targetEmail, targetNickName);
        stopWatch.stop();

        stopWatchRecordPrint(stopWatch);
    }

    @Test
    public void postBasicIndexScan() {
        String targetTitle = "test post59000";
        StopWatch stopWatch = new StopWatch("Basic Index Scan");

        stopWatch.start();
        Post byTitle = postRepository.findByTitle(targetTitle);
        stopWatch.stop();

        stopWatchRecordPrint(stopWatch);
    }

    @Test
    public void postUniqueIndexScan() {
        String targetUUID = "9b7a53f4-6406-414a-9f68-fb7af56b221a";
        UUID targetUuid = UUID.fromString(targetUUID);

        StopWatch stopWatch = new StopWatch("Post Unique Index Scan");

        stopWatch.start();
        Post post = postRepository.findByUuid(targetUuid);
        stopWatch.stop();

        stopWatchRecordPrint(stopWatch);
    }


    @Test
    public void postUniqueIndex() {

    }

    private void stopWatchRecordPrint(StopWatch stopWatch) {
        System.out.println(stopWatch.prettyPrint());
        System.out.println("마지막 작업 걸린 시간 : " + stopWatch.getTotalTimeNanos());
        System.out.println("totalTimeSeconds : " + stopWatch.getTotalTimeSeconds());
    }
}
