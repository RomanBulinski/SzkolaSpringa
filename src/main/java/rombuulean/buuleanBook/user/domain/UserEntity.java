package rombuulean.buuleanBook.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rombuulean.buuleanBook.jpa.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table( name = "users")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class UserEntity extends BaseEntity {

    private String username;
    private String password;

    @CollectionTable(
            name = " user_roles",
            joinColumns = @JoinColumn( name = "user_id")
    )
    @Column( name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles = new HashSet<>();

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    public UserEntity(String username, String password) {
        this.username = username;
        this.password  =password;
        this.roles = Set.of("ROLE_USER");
    }
}
