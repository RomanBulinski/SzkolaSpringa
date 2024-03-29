package rombuulean.buuleanBook.order.application;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@Value
@ConstructorBinding
@ConfigurationProperties("app.orders")
public class OrdersProperties {
    String abandonCron;
    Duration paymentPeriod;
}
