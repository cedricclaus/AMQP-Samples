package be.cite.sample.receiver;


import be.cite.sample.domain.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import static be.cite.sample.config.RabbitMQConfiguration.AMQP_SAMPLES_MSG_QUEUE;


public class MessageReceiver {



    private final Logger log = LoggerFactory.getLogger(MessageReceiver.class);

    public MessageReceiver() {

    }

    @RabbitListener(queues = AMQP_SAMPLES_MSG_QUEUE)
    public void receive(MessageDTO msg) throws Exception {
        log.info("RECEIVED Message");
        log.info(msg.toString());
        if(!msg.getGood()){
            throw new Exception("Could not handle message");
        }
    }

}
