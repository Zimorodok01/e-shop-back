package com.example.eshopback.model.entity;

import com.example.eshopback.model.entity.audit.AuditModel;
import com.example.eshopback.model.enums.ProductType;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "products")
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "product_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private double price;
    @Enumerated(EnumType.STRING)
    private ProductType type;
}
