package be.cite.sample.config;


import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

}
