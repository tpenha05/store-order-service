// OrderParser.java
package store.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import store.product.ProdutoOut;

public class OrderParser {

    public static Order to(OrderIn in, String idUser) {
        if (in == null) return null;
        
        return Order.builder()
            .idUser(idUser)
            .data(LocalDateTime.now())
            .items(in.items().stream().map(OrderParser::to).toList())
            .build();
    }
    
    public static OrderItem to(OrderItemIn in) {
        if (in == null) return null;
        
        return OrderItem.builder()
            .idProduto(in.idProduto())
            .quantidade(in.quantidade())
            .build();
    }


    public static OrderOut to(Order order, List<OrderItem> items, List<ProdutoOut> products) {
        if (order == null) return null;
        
        return OrderOut.builder()
            .id(order.id())
            .data(order.data())
            .total(order.total())
            .build();
    }
    
    public static OrderOut toSummary(Order order) {
        if (order == null) return null;
        
        return OrderOut.builder()
            .id(order.id())
            .data(order.data())
            .total(order.total())
            .build();
    }
}