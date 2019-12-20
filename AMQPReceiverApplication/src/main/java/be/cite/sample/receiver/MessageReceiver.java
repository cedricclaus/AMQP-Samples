package be.cite.sample.receiver;


import be.cite.sample.domain.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import static be.cite.sample.config.RabbitMQConfiguration.AMQP_SAMPLES_MSG_QUEUE;
import static be.cite.sample.config.RabbitMQConfiguration.DEAD_LETTER_QUEUE_NAME;


public class MessageReceiver {



    private final Logger log = LoggerFactory.getLogger(MessageReceiver.class);

    public MessageReceiver() {

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
    public void receiveFromDeadLetter(MessageDTO msg){
        String name = Thread.currentThread().getName();
        log.info("RECEIVED Message From dead Letter queue (" +name + ")");
        log.info(msg.toString());
    }

}
