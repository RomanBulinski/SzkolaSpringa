package rombuulean.buuleanBook.order.domain;

import lombok.*;
import rombuulean.buuleanBook.jpa.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/*
 @Embeddable - adnotacja która pozwala dopisac
 do tabeli rodzica pola obiektu który nie jest encja
*/
//@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Recipient extends BaseEntity {

    private String name;
    private String phone;
    private String street;
    private String city;
    private String zipCode;
    private String email;

}
