package com.example.eshopback.model.entity;

import com.example.eshopback.model.entity.audit.AuditModel;
import com.example.eshopback.model.enums.PaymentType;
import com.example.eshopback.model.enums.ProductType;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    @SequenceGenerator(name = "order_seq", sequenceName = "order_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "sales_point_id")
    private SalesPoint salesPoint;

    private String clientName;

    @OneToOne
    @JoinColumn(name = "cashier_id")
    private User cashier;
    private double sum;
    private double paymentSum;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
}
