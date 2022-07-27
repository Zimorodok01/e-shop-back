package com.example.eshopback.model.entity;

import com.example.eshopback.model.entity.audit.AuditModel;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_remains")
//public class Remain extends AuditModel {
public class Remain {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "remain_seq")
    @SequenceGenerator(name = "remain_seq", sequenceName = "remain_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sales_point_id")
    private SalesPoint salesPoint;

    @Builder.Default
    private int amount = 0;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private Product product;
}
