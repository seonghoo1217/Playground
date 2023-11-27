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
public class Posting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private UUID uuid;

    private String title;

    private String category;

    @ManyToOne
    private Member member;

    public Posting(UUID uuid, String title, String category, Member member) {
        this.uuid = uuid;
        this.title = title;
        this.category = category;
        this.member = member;
    }
}
