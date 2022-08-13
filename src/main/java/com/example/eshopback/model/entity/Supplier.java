package com.example.eshopback.model.entity;

import com.example.eshopback.model.entity.audit.AuditModel;
import com.example.eshopback.model.enums.SupplierType;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "suppliers")
@Getter@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Supplier extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supplier_seq")
    @SequenceGenerator(name = "supplier_seq", sequenceName = "supplier_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private String phone;
    private String email;
    private String contact;
    private SupplierType type;
    private String comments;
}
