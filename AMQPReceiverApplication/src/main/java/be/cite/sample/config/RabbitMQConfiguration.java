package be.cite.sample.config;



import be.cite.sample.domain.MessageDTO;
import be.cite.sample.receiver.MessageReceiver;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
     Jackson2JsonMessageConverter messageConverter() {
        /**REplace the default spring as it's also used for JSON transformation on REST Endpoint
         * Should be aligned with the sender
         * **/
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); //handle date
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); //write date as string
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);     //allow to have different properties between sender/receiver
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter(mapper);
        jackson2JsonMessageConverter.setClassMapper(classMapper());

        return jackson2JsonMessageConverter;
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
    public MessageListenerAdapter listenerAdapter(MessageReceiver messageReceiver) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(messageReceiver);
        return adapter;
    }

    @Bean
    public MessageReceiver messageReceiver(){
        return new MessageReceiver();
    }

    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        /** Used to find the corresponding class to desrialize base on the  __TypeId__  header **/
        idClassMapping.put("be.cite.sample.domain.Message", MessageDTO.class);
        classMapper.setIdClassMapping(idClassMapping);
        return classMapper;
    }

}
