package com.example.eshopback.model.entity;

import com.example.eshopback.model.entity.audit.AuditModel;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "positions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Position extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "position_seq")
    @SequenceGenerator(name = "position_seq", sequenceName = "position_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int amount;
}
