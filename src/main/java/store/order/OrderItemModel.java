// OrderItemModel.java
package store.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "order_item")
@Setter @Accessors(fluent = true)
@NoArgsConstructor
public class OrderItemModel {

    @Id
    @Column(name = "id_order_item")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "id_order")
    private String idOrder;

    @Column(name = "id_product")
    private String idProduto;

    @Column(name = "num_quantity")
    private Integer quantidade;

    @Column(name = "db_total")
    private Double total;

    public OrderItemModel(OrderItem item) {
        this.id = item.id();
        this.idOrder = item.idOrder();
        this.idProduto = item.idProduto();
        this.quantidade = item.quantidade();
        this.total = item.total();
    }

    public OrderItem to() {
        return OrderItem.builder()
            .id(this.id)
            .idOrder(this.idOrder)
            .idProduto(this.idProduto)
            .quantidade(this.quantidade)
            .total(this.total)
            .build();
    }
}