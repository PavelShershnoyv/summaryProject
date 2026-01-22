package ru.development.MSS.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "producers_has_subscribers")
@Data
public class ProducerSubscriberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "producers_id", nullable = false)
    private Integer producersId;

    @Column(name = "subscribers_id", nullable = false)
    private Integer subscribersId;
}
