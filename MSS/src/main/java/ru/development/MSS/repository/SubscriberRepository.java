package ru.development.MSS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.development.MSS.entity.SubscriberEntity;

import java.util.Optional;

public interface SubscriberRepository extends JpaRepository<SubscriberEntity, Integer> {
    Optional<SubscriberEntity> findBySubscriberId(Integer subscriberId);
}

