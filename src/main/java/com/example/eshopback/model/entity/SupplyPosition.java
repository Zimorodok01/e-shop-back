package com.example.eshopback.model.entity;

import com.example.eshopback.model.entity.audit.AuditModel;
import com.example.eshopback.model.enums.NDSType;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "supply_positions")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplyPosition extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supply_seq")
    @SequenceGenerator(name = "supply_seq", sequenceName = "supply_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "supply_id")
    private Supply supply;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private NDSType ndsType;

    private int amount;
    @Builder.Default
    private double price = 0;
}
