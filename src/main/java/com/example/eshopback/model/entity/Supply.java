package com.example.eshopback.model.entity;

import com.example.eshopback.model.entity.audit.AuditModel;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "product_supplies")
@Builder
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Supply extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supply_seq")
    @SequenceGenerator(name = "supply_seq", sequenceName = "supply_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "sales_point_id")
    private SalesPoint salesPoint;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int amount;
    @Builder.Default
    private double price = 0;
}
