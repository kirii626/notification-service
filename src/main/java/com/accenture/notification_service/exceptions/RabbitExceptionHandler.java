package com.accenture.notification_service.exceptions;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class RabbitExceptionHandler implements RabbitListenerErrorHandler {
    
    @Override
    public Object handleError(Message message, Channel channel, org.springframework.messaging.Message<?> message1, ListenerExecutionFailedException exception) throws Exception {
        Throwable cause = exception.getCause();

        if (cause instanceof PdfGenerationException) {
            log.error("PDF Generation Error: {}", cause.getMessage(), cause);
        } else if (cause instanceof EmailException) {
            log.error("Email Sending Error: {}", cause.getMessage(), cause);
        } else {
            log.error("General Error processing message: {}", new String(message.getBody()), cause);
        }
        return null;
    }

}
