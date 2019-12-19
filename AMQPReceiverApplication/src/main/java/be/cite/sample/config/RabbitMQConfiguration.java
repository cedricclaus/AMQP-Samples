package be.cite.sample.config;



import be.cite.sample.receiver.MessageReceiver;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Created by cedric.claus on 24-04-17.
 */
@Configuration
public class RabbitMQConfiguration {

    public static final String AMQP_SAMPLES_MSG_QUEUE = "AMQP-SAMPLES-MSG";
    private ApplicationProperties applicationProperties;


    @Autowired
    private ConnectionFactory rabbitConnectionFactory;

    public RabbitMQConfiguration(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }


    @Bean
    public TopicExchange topic() {
        return new TopicExchange("AMQP-TEST");
    }

    @Bean
    RabbitTemplate rabbitTemplate() throws IOException {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(this.rabbitConnectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(AMQP_SAMPLES_MSG_QUEUE)
            .build();
    }



    @Bean
    public Binding binding(TopicExchange topic, Queue queue) {
        return BindingBuilder.bind(queue).to(topic).with("message.create");
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(MessageReceiver receiver) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(receiver);

        return adapter;
    }

    @Bean
    public MessageReceiver messageReceiver(){
        return new MessageReceiver();
    }

}
