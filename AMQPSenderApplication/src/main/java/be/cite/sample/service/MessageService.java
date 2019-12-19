package be.cite.sample.service;

import be.cite.sample.domain.Message;
import org.bson.types.ObjectId;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class MessageService {

    private final RabbitTemplate rabbitTemplate;

    private final TopicExchange topicExchange;

    public MessageService(RabbitTemplate rabbitTemplate, TopicExchange topicExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.topicExchange = topicExchange;
    }


    public List<Message> generateMessages(String name, int start, int end) {
        return IntStream.range(start, end)
                .boxed()
                .map(i -> {
                    Message msg = new Message();
                    msg.setId(new ObjectId().toString());
                    msg.setName(name);
                    msg.setIndex(i);
                    msg.setDate(LocalDateTime.now());
                    msg.setGood( Math.random() >= 0.1); //10% of false
                    return msg;
                })
               .map(msg -> {
                   rabbitTemplate.convertAndSend(topicExchange.getName(), "message.create",msg);
                   return msg;
               })
                .collect(Collectors.toList());
    }
}
