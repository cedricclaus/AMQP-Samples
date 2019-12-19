package be.cite.sample.receiver;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import static be.cite.sample.config.RabbitMQConfiguration.AMQP_SAMPLES_MSG_QUEUE;


public class MessageReceiver {

    private final Logger log = LoggerFactory.getLogger(MessageReceiver.class);

    @RabbitListener(queues = AMQP_SAMPLES_MSG_QUEUE)
    public void receive(Message msg) {
        log.info("RECEIVED Message");
        log.info( msg.getMessageProperties().toString());
        log.info(new String(msg.getBody()));
    }

}
