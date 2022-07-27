package com.example.eshopback.model.entity;


import com.example.eshopback.model.entity.audit.AuditModel;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales_points")
@Getter@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalesPoint extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tt_seq")
    @SequenceGenerator(name = "tt_seq", sequenceName = "tt_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    private String pointName;


    @OneToMany(
            mappedBy = "salesPoint",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<User> employees = new ArrayList<>();
}
