package rombuulean.buuleanBook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import rombuulean.buuleanBook.order.application.OrdersProperties;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
@EnableConfigurationProperties(OrdersProperties.class)
public class BuuleanBookWebBookstoreApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BuuleanBookWebBookstoreApplication.class, args);
        System.out.println(context);
    }

}
