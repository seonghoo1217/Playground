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
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StopWatch;

import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@SpringBootTest
public class IndexingTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostingRepository postingRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private List<String> categories = List.of("자유", "유머", "연애", "고민");

    private final Random random = new Random();

    @Test
    public void initializeData() {
        dummyMemberBatchInsert();
        dummyPostBatchInsert();
        dummyPostingBatchInsert();
    }

    @Test
    public void dummyPostAndPosting() {
        dummyPostBatchInsert();
        dummyPostingBatchInsert();
    }

    public List<Member> initializeMember() {
        List<Member> members = new ArrayList<>();
        for (int i = 1; i <= 100000; i++) {
            members.add(new Member("test" + i + "@eamil.com", "testuser" + i, "1234", random.nextInt(61) + 20, "서울시 중앙" + i + "동"));
        }
        return members;
    }

    private List<Post> initializePost() {
        StopWatch stopWatch = new StopWatch("Post Index 데이터 삽입 시간 기록");
        List<Post> posts = new ArrayList<>();

        stopWatch.start();
        for (int i = 1; i <= 100000; i++) {
            String category = categories.get(random.nextInt(4));
            Member member = memberRepository.findById((long) i).get();
            posts.add(new Post(UUID.randomUUID(), "test post" + i, category, member));
        }
        stopWatch.stop();

        stopWatchRecordPrint(stopWatch);
        return posts;
    }

    private List<Posting> initializePosting() {
        StopWatch stopWatch = new StopWatch("Postingx 데이터 삽입 시간 기록");
        List<Posting> postings = new ArrayList<>();

        stopWatch.start();
        for (int i = 1; i <= 100000; i++) {
            String category = categories.get(random.nextInt(4));
            Member member = memberRepository.findById((long) i).get();
            postings.add(new Posting(UUID.randomUUID(), "test post" + i, category, member));
        }
        stopWatch.stop();

        stopWatchRecordPrint(stopWatch);

        return postings;
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
    public void basicIndexCombineCompareToPostAndPosting() {
        String targetTitle = "test post59000";
        String targetCategory = "";

        StopWatch stopWatch = new StopWatch("USE INDEX SELECT");

        stopWatch.start();
//        postingRepository.findByTitleAndCategory(targetTitle)
    }

    private void stopWatchRecordPrint(StopWatch stopWatch) {
        System.out.println(stopWatch.prettyPrint());
        System.out.println("마지막 작업 걸린 시간 : " + stopWatch.getTotalTimeNanos());
        System.out.println("totalTimeSeconds : " + stopWatch.getTotalTimeSeconds());
    }

    private void dummyMemberBatchInsert() {
        List<Member> members = initializeMember();
        String memberSql = "INSERT INTO member(email, nickname, password, age, address) VALUES (?,?,?,?,?)";
        jdbcTemplate.batchUpdate(memberSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, members.get(i).getEmail());
                ps.setString(2, members.get(i).getNickname());
                ps.setString(3, members.get(i).getPassword());
                ps.setInt(4, members.get(i).getAge());
                ps.setString(5, members.get(i).getAddress());
            }

            @Override
            public int getBatchSize() {
                return members.size();
            }
        });
    }


    private void dummyPostBatchInsert() {
        List<Post> posts = initializePost();
        StopWatch stopWatch = new StopWatch("post bulk insert");

        stopWatch.start();
        String postSql = "INSERT INTO post(uuid, title, category, member_id) VALUES (?,?,?,?)";
        jdbcTemplate.batchUpdate(postSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setBytes(1, toBytes(posts.get(i).getUuid()));
                ps.setString(2, posts.get(i).getTitle());
                ps.setString(3, posts.get(i).getCategory());
                ps.setLong(4, posts.get(i).getMember().getId());
            }

            @Override
            public int getBatchSize() {
                return posts.size();
            }
        });
        stopWatch.stop();
        stopWatchRecordPrint(stopWatch);
    }

    private void dummyPostingBatchInsert() {
        List<Posting> postings = initializePosting();
        StopWatch stopWatch = new StopWatch("posting bulk insert");

        stopWatch.start();
        String postingSql = "INSERT INTO post(uuid, title, category, member_id) VALUES (?,?,?,?)";
        jdbcTemplate.batchUpdate(postingSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setBytes(1, toBytes(postings.get(i).getUuid()));
                ps.setString(2, postings.get(i).getTitle());
                ps.setString(3, postings.get(i).getCategory());
                ps.setLong(4, postings.get(i).getMember().getId());
            }

            @Override
            public int getBatchSize() {
                return postings.size();
            }
        });
        stopWatch.stop();

        stopWatchRecordPrint(stopWatch);
    }

    private byte[] toBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }
}
