package com.trepudox;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class Main {

    private static final String QUEUE_NAME = "hello_queue";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("consumer");
        factory.setPassword("consumer");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            log.info("Message received: {}", msg);
        };

        CancelCallback cancelCallback = consumerTag -> log.warn("Canceling??");

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}