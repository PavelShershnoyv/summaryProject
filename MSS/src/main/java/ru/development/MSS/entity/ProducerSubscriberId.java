package ru.development.MSS.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class ProducerSubscriberId implements Serializable {
    private Integer producersId;
    private Integer subscribersId;
}
