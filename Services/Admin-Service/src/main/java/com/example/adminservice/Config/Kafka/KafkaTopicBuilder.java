package com.example.adminservice.Config.Kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicBuilder {
    @Bean
    public NewTopic CreateUserTopic(){
        return TopicBuilder.name("create_user")
                .build();
    }

    @Bean
    public NewTopic EmailNotificationTopic(){
        return TopicBuilder.name("email_notification")
                .build();
    }

    @Bean
    public NewTopic SmsNotificationTopic(){
        return TopicBuilder.name("sms_notification")
                .build();
    }
    @Bean
    public NewTopic auditLogTopic(){
        return TopicBuilder.name("audit-topic")
                .build();
    }
}
