// Order.java
package store.order;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Builder
@Data @Accessors(fluent = true)
public class Order {

    private String id;
    private String idUser;
    private LocalDateTime data;
    private List<OrderItem> items;
    private Double total;
    
}