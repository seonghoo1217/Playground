package example.indexing.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "member",
        uniqueConstraints = {@UniqueConstraint(name = "unique_nickname_email", columnNames = {"nickname", "email"})}
)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;

    private Integer age;

    private String address;

    @OneToMany
    private List<Post> posts = new ArrayList<>();


    public Member(String email, String nickname, String password, Integer age, String address) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.age = age;
        this.address = address;
    }
}
