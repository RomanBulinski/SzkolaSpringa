package rombuulean.buuleanBook.user.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rombuulean.buuleanBook.jpa.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table( name = "users")
@EntityListeners(AuditingEntityListener.class)
public class UserEntity extends BaseEntity {

    private String username;
    private String password;

    @CollectionTable(
            name = " user_roles",
            joinColumns = @JoinColumn( name = "user_id")
    )
    @Column( name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

}
