package be.cite.sample.receiver;


import be.cite.sample.domain.MessageDTO;
import be.cite.sample.domain.MongoDBRabbitMessage;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import static be.cite.sample.config.RabbitMQConfiguration.AMQP_SAMPLES_MSG_QUEUE;
import static be.cite.sample.config.RabbitMQConfiguration.DEAD_LETTER_QUEUE_NAME;


public class MessageReceiver {


    final MongoTemplate mongoTemplate;

    private final Logger log = LoggerFactory.getLogger(MessageReceiver.class);

    public MessageReceiver(MongoTemplate mongoTemplate) {

        this.mongoTemplate = mongoTemplate;
    }

    @RabbitListener(queues = AMQP_SAMPLES_MSG_QUEUE)
    public void receive(MessageDTO msg) throws Exception {
        String name = Thread.currentThread().getName();
        log.info("RECEIVED Message (" +name + ")");
        log.info(msg.toString());
        if(!msg.getGood()){
            throw new Exception("Could not handle message");
        }
    }

    @RabbitListener(queues = DEAD_LETTER_QUEUE_NAME)
    public void receiveFromDeadLetter(Message msg) throws UnsupportedEncodingException {
        String name = Thread.currentThread().getName();
        log.info("RECEIVED Message From dead Letter queue (" +name + ")");
        log.error(msg.toString());
//        Document doc = new Document("headers", msg.getMessageProperties().getHeaders());
//        doc.append("properties",msg.getMessageProperties());
//        doc.append("body",Document.parse(new String(msg.getBody(), Charset.defaultCharset().name())));
        mongoTemplate.save(new MongoDBRabbitMessage(msg),"DEAD_LETTERS_MSG");
    }

}
