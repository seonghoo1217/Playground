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
                @Index(name = "post_uuid_title", columnList = "uuid,title")
        })
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private UUID uuid;

    private String title;

    private String category;
}