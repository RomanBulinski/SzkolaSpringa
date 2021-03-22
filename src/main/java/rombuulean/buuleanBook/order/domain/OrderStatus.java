package rombuulean.buuleanBook.order.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

public enum OrderStatus {

    NEW {
        @Override
        public OrderStatus updateStatus(OrderStatus status){
             switch (status){
                 case PAID:
                     return PAID;
                 case CANCELED:
                     return CANCELED;
                 case ABANDONED:
                     return ABANDONED;
                 default:
                    return super.updateStatus(status);
                }
            }
    },
    PAID {
        @Override
        public OrderStatus updateStatus(OrderStatus status) {
            if(status==SHIPPED){
                return SHIPPED;
            }
            return super.updateStatus(status);
        }
    },
    CANCELED,
    ABANDONED,
    SHIPPED;

    public static Optional<OrderStatus> parseString(String value){
        return Arrays.stream( values() )
                .filter(it -> StringUtils.equalsIgnoreCase(it.name(), value))
                .findFirst();
    }

    public OrderStatus updateStatus(OrderStatus status){
        throw new IllegalArgumentException("Unable to mark " + this.name() + " order as " + status.name());
    }

}
