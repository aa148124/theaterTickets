package com.dkh.utils;

public class RabbitMQConstants {
    //交换机
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange.order";
    //队列
    public static final String DELAYED_QUEUE_ORDER = "delayed.queue.order";
    public static final String DELAYED_QUEUE_BOX_OFFICE = "delayed.queue.box.office";
    public static final String DELAYED_QUEUE_FILM = "delayed.queue.film";
    //routingKey
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";
    public static final String BOX_OFFICE_KEY = "box.office.routingkey";
    public static final String FILM_KEY = "film.routingkey";

}
