package example.indexing.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post",
        indexes = {
                @Index(name = "post_title", columnList = "title"),
                @Index(name = "post_category", columnList = "category"),
                @Index(name = "post_title_category", columnList = "title,category")
        })
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, columnDefinition = "BINARY(16)")
    private UUID uuid;

    private String title;

    private String category;

    @ManyToOne
    private Member member;

    public Post(UUID uuid, String title, String category, Member member) {
        this.uuid = uuid;
        this.title = title;
        this.category = category;
        this.member = member;
    }
}
