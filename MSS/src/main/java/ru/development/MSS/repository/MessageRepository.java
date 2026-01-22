package ru.development.MSS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.development.MSS.entity.MessageEntity;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {
    List<MessageEntity> findByProducerId(Integer producerId);

    @Query(value = """
            SELECT m.* FROM messages m
            JOIN producers p ON p.producer_id = m.producer_id
            JOIN producers_has_subscribers ps ON ps.producers_id = p.producer_id
            JOIN subscribers s ON s.id = ps.subscribers_id
            WHERE s.subscriber_id = :subscriberExternalId
            """, nativeQuery = true)
    List<MessageEntity> findForSubscriber(@Param("subscriberExternalId") Integer subscriberExternalId);

    List<MessageEntity> findAllByOrderByCreatedDesc();
}
