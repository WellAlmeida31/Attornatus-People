package com.attornatus.people.infrastructure.configuration;

import com.amazonaws.auth.*;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.Topic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class SnsConfig {


    @Value("${aws.region}")
    private String awsRegion;
    @Value("${aws.sns.topic.registration.arn}")
    private String registrationTopic;
    @Value("${aws.credentials.access.key}")
    private String accessKey;
    @Value("${aws.credentials.secret.key}")
    private String secretKey;

    @Bean
    public AmazonSNS snsClient() {
        return AmazonSNSClientBuilder.standard()
                .withRegion(awsRegion)
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(accessKey,secretKey)))
                .build();
    }

    @Bean(name = "registrationTopic")
    public Topic snsActivateTopic() {
        return new Topic().withTopicArn(registrationTopic);
    }

}
