package ru.development.MSS.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.development.MSS.dto.Message;
import ru.development.MSS.entity.MessageEntity;
import ru.development.MSS.entity.ProducerEntity;
import ru.development.MSS.entity.ProducerSubscriberEntity;
import ru.development.MSS.entity.SubscriberEntity;
import ru.development.MSS.repository.MessageRepository;
import ru.development.MSS.repository.ProducerRepository;
import ru.development.MSS.repository.ProducerSubscriberRepository;
import ru.development.MSS.repository.SubscriberRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MssService {
    private final MessageRepository messageRepository;
    private final ProducerRepository producerRepository;
    private final SubscriberRepository subscriberRepository;
    private final ProducerSubscriberRepository producerSubscriberRepository;

    public Message create(Message message) {
        MessageEntity entity = new MessageEntity();
        entity.setContent(message.getContent());
        entity.setProducerId(message.getProducerId());
        entity.setCreated(message.getCreated() != null ? message.getCreated() : (int) Instant.now().getEpochSecond());
        MessageEntity saved = messageRepository.save(entity);
        return toDto(saved);
    }

    public void delete(Integer id) {
        messageRepository.deleteById(id);
    }

    public Optional<Message> getById(Integer id) {
        return messageRepository.findById(id).map(this::toDto);
    }

    public List<Message> getByProducer(Integer producerId) {
        return messageRepository.findByProducerId(producerId).stream().map(this::toDto).toList();
    }

    public List<Message> getBySubscriber(Integer subscriberId) {
        return messageRepository.findForSubscriber(subscriberId).stream().map(this::toDto).toList();
    }

    public List<Message> getAll() {
        return messageRepository.findAllByOrderByCreatedDesc().stream().map(this::toDto).toList();
    }

    public void subscribe(Integer subscriberExternalId, Integer producerExternalId) {
        ProducerEntity producer = producerRepository.findByProducerId(producerExternalId)
                .orElseGet(() -> {
                    ProducerEntity p = new ProducerEntity();
                    p.setProducerId(producerExternalId);
                    return producerRepository.save(p);
                });

        SubscriberEntity subscriber = subscriberRepository.findBySubscriberId(subscriberExternalId)
                .orElseGet(() -> {
                    SubscriberEntity s = new SubscriberEntity();
                    s.setSubscriberId(subscriberExternalId);
                    return subscriberRepository.save(s);
                });

        Integer subscribersId = subscriber.getId();
        Integer producersId = producer.getId();

        if (!producerSubscriberRepository.existsByProducersIdAndSubscribersId(producersId, subscribersId)) {
            ProducerSubscriberEntity link = new ProducerSubscriberEntity();
            link.setProducersId(producersId);
            link.setSubscribersId(subscribersId);
            producerSubscriberRepository.save(link);
        }
    }

    public void unsubscribe(Integer subscriberExternalId, Integer producerExternalId) {
        SubscriberEntity subscriber = subscriberRepository.findBySubscriberId(subscriberExternalId)
                .orElseThrow(() -> new EntityNotFoundException("Subscriber not found"));

        ProducerEntity producer = producerRepository.findByProducerId(producerExternalId)
                .orElseThrow(() -> new EntityNotFoundException("Producer not found"));

        Integer subscribersId = subscriber.getId();
        Integer producersId = producer.getId();
        if (producerSubscriberRepository.existsByProducersIdAndSubscribersId(producersId, subscribersId)) {
            producerSubscriberRepository.deleteByProducersIdAndSubscribersId(producersId, subscribersId);

        } else {
            throw new EntityNotFoundException("Subscriber is not subscribed to the producer");
        }
    }

    public boolean isSubscribed(Integer subscriberExternalId, Integer producerExternalId) {
        Optional<SubscriberEntity> subscriber = subscriberRepository.findBySubscriberId(subscriberExternalId);
        Optional<ProducerEntity> producer = producerRepository.findByProducerId(producerExternalId);
        if (subscriber.isEmpty() || producer.isEmpty()) {
            return false;
        }
        Integer subscribersId = subscriber.get().getId();
        Integer producersId = producer.get().getId();
        return producerSubscriberRepository.existsByProducersIdAndSubscribersId(producersId, subscribersId);
    }

    private Message toDto(MessageEntity e) {
        Message m = new Message();
        m.setId(e.getId());
        m.setContent(e.getContent());
        m.setCreated(e.getCreated());
        m.setProducerId(e.getProducerId());
        return m;
    }
}
