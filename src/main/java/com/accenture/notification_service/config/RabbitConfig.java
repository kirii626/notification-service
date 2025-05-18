package com.accenture.notification_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    @Value("{accreditation.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingkey.accreditation}")
    private String accreditationCreatedRoutingKey;

    @Value("${rabbitmq.queue.accreditation}")
    private String accreditationQueue;

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Queue accreditationQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 20000);

        return new Queue(accreditationQueue);
    }

    @Bean
    public Binding bindingAccreditationCreated(Queue accreditationQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(accreditationQueue)
                .to(exchange)
                .with(accreditationCreatedRoutingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
