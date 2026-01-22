package ru.development.MSS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.development.MSS.entity.ProducerSubscriberEntity;

public interface ProducerSubscriberRepository extends JpaRepository<ProducerSubscriberEntity, Integer> {
    boolean existsByProducersIdAndSubscribersId(Integer producersId, Integer subscribersId);
    void deleteByProducersIdAndSubscribersId(Integer producersId, Integer subscribersId);
}
