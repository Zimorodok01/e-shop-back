package com.example.eshopback.model.entity;

import com.example.eshopback.model.entity.audit.AuditModel;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "product_writeOffs")
@Getter@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class WriteOff extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "writeOff_seq")
    @SequenceGenerator(name = "writeOff_seq", sequenceName = "writeOff_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String productName;

    @OneToOne
    @JoinColumn(name = "sales_point_id")
    private SalesPoint salesPoint;

    private int amount;
}
