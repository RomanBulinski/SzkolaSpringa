package rombuulean.buuleanBook.order.domain;

import lombok.*;

import javax.persistence.Embeddable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Recipient {

    private String name;
    private String phone;
    private String street;
    private String city;
    private String zipCode;
    private String email;

}
