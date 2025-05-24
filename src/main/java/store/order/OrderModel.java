// OrderModel.java
package store.order;

import java.time.LocalDateTime;

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
@Table(name = "order_tb")
@Setter @Accessors(fluent = true)
@NoArgsConstructor
public class OrderModel {

    @Id
    @Column(name = "id_order")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "id_account")
    private String idUser;

    @Column(name = "dt_date")
    private LocalDateTime data;

    @Column(name = "db_total")
    private Double total;

    public OrderModel(Order o) {
        this.id = o.id();
        this.idUser = o.idUser();
        this.data = o.data();
        this.total = o.total();
    }

    public Order to() {
        return Order.builder()
            .id(this.id)
            .idUser(this.idUser)
            .data(this.data)
            .total(this.total)
            .build();
    }
}