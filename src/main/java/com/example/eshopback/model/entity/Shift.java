package com.example.eshopback.model.entity;

import com.example.eshopback.model.entity.audit.AuditModel;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter@Setter
@Table(name = "shifts")
public class Shift extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shift_seq")
    @SequenceGenerator(name = "shift_seq", sequenceName = "shift_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "sales_point_id")
    private SalesPoint salesPoint;

    private Date openedAt;
    private Date closedAt;
}
