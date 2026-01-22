package ru.development.MSS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.development.MSS.entity.ProducerEntity;

import java.util.Optional;

public interface ProducerRepository extends JpaRepository<ProducerEntity, Integer> {
    Optional<ProducerEntity> findByProducerId(Integer producerId);
}

