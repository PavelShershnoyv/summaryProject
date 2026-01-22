package ru.development.MSS.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "producers")
@Data
public class ProducerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "producer_id")
    private Integer producerId;
}
