// OrderService.java
package store.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import store.product.ProdutoOut;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private RestTemplate restTemplate;

    private ProdutoOut findProductById(String id) {
    String url = "http://product-service:8080/produto/" + id;
        try {
            ResponseEntity<ProdutoOut> response = restTemplate.getForEntity(url, ProdutoOut.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found");
            }
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found");
            }
    }

    public OrderOut create(OrderIn orderIn, String idUser) {
        if (orderIn.items() == null || orderIn.items().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order must have at least one item");
        }
        
        Order order = OrderParser.to(orderIn, idUser);
        double orderTotal = 0.0;
        
        List<OrderItem> orderItems = new ArrayList<>();
        List<ProdutoOut> products = new ArrayList<>();
        
        for (OrderItemIn itemIn : orderIn.items()) {
            if (itemIn.quantidade() == null || itemIn.quantidade() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item quantity must be greater than zero");
            }
            
            ProdutoOut product = findProductById(itemIn.idProduto());
            products.add(product);
            
            double itemTotal = product.preco() * itemIn.quantidade();
            
            OrderItem orderItem = OrderItem.builder()
                .idProduto(itemIn.idProduto())
                .quantidade(itemIn.quantidade())
                .total(itemTotal)
                .build();
            
            orderItems.add(orderItem);
            orderTotal += itemTotal;
        }
        
        order.total(orderTotal);
        
        OrderModel savedOrder = orderRepository.save(new OrderModel(order));
        order = savedOrder.to();
        
        List<OrderItem> savedItems = new ArrayList<>();
        for (OrderItem item : orderItems) {
            item.idOrder(order.id());
            OrderItemModel savedItem = orderItemRepository.save(new OrderItemModel(item));
            savedItems.add(savedItem.to());
        }
        
        return OrderParser.to(order, savedItems, products);
    }

    public List<OrderOut> findAllByAccount(String idAccount) {
        List<OrderOut> result = new ArrayList<>();
        for (OrderModel orderModel : orderRepository.findByIdUser(idAccount)) {
            result.add(OrderParser.toSummary(orderModel.to()));
        }
        return result;
    }
    
    public OrderOut findByIdAndAccount(String id, String idAccount) {
        OrderModel orderModel = orderRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        
        Order order = orderModel.to();
        
        // Verificar se o pedido pertence ao usuário atual
        if (!order.idUser().equals(idAccount)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        
        // Buscar os itens do pedido
        List<OrderItem> items = new ArrayList<>();
        for (OrderItemModel itemModel : orderItemRepository.findByIdOrder(id)) {
            items.add(itemModel.to());
        }
        
        // Buscar os produtos usando RestTemplate
        List<ProdutoOut> products = new ArrayList<>();
        for (OrderItem item : items) {
            ProdutoOut product = findProductById(item.idProduto());
            if (product != null) {
                products.add(product);
            }
        }

        return OrderParser.to(order, items, products);
    }
}