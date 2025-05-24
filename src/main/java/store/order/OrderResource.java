// OrderResource.java
package store.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderResource implements OrderController {

    @Autowired
    private OrderService orderService;

    @Override
    public ResponseEntity<OrderOut> create(OrderIn orderIn, String idUser) {
        OrderOut created = orderService.create(orderIn, idUser);
        return ResponseEntity.created(null).body(created);
    }

    @Override
    public ResponseEntity<List<OrderOut>> findAll(String idUser) {
        return ResponseEntity
            .ok()
            .body(orderService.findAllByAccount(idUser));
    }
    
    @Override
    public ResponseEntity<OrderOut> findById(String id, String idUser) {
        return ResponseEntity
            .ok()
            .body(orderService.findByIdAndAccount(id, idUser));
    }
}