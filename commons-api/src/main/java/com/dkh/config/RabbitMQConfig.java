package com.dkh.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.dkh.utils.RabbitMQConstants.*;

@Configuration
public class RabbitMQConfig {


    @Bean
    public Queue delayedQueue(){
        return new Queue(DELAYED_QUEUE_ORDER);
    }
    @Bean
    public Queue boxOfficeQueue(){
        return new Queue(DELAYED_QUEUE_BOX_OFFICE);
    }
    @Bean
    public Queue filmQueue(){
        return new Queue(DELAYED_QUEUE_FILM);
    }
    //声明交换机,基于插件的交换机
    @Bean
    public CustomExchange delayedExchange(){

        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type","direct");
        /**
         * 1.交换机的名称
         * 2.交换机的类型 x-delayed-message
         * 3.是否需要持久化
         * 4.是否需要自动删除
         * 5.其他的参数
         */
        return new CustomExchange(DELAYED_EXCHANGE_NAME,"x-delayed-message",
                true,false,arguments);
    }

    //绑定
    @Bean
    public Binding delayedQueueBindingDelayedExchange(
            @Qualifier("delayedQueue") Queue delayedQueue,
            @Qualifier("delayedExchange")CustomExchange delayedExchange){
        return BindingBuilder.bind(delayedQueue).to(delayedExchange)
                .with(DELAYED_ROUTING_KEY).noargs();
    }
    //绑定
    @Bean
    public Binding boxOfficeQueueBindingDelayedExchange(
            @Qualifier("boxOfficeQueue") Queue delayedQueue,
            @Qualifier("delayedExchange")CustomExchange delayedExchange){
        return BindingBuilder.bind(delayedQueue).to(delayedExchange)
                .with(BOX_OFFICE_KEY).noargs();
    }
    //绑定
    @Bean
    public Binding filmQueueBindingDelayedExchange(
            @Qualifier("filmQueue") Queue delayedQueue,
            @Qualifier("delayedExchange")CustomExchange delayedExchange){
        return BindingBuilder.bind(delayedQueue).to(delayedExchange)
                .with(FILM_KEY).noargs();
    }
}
