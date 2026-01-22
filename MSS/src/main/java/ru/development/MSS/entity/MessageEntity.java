package ru.development.MSS.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "messages")
@Data
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @Column(name = "created")
    private Integer created;

    @Column(name = "producer_id")
    private Integer producerId;
}
