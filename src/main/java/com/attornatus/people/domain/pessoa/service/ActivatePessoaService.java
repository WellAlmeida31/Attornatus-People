package com.attornatus.people.domain.pessoa.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

@Service
public class ActivatePessoaService {

    private final AmazonSNS snsClient;
    private final Topic registrationTopic;

    @Autowired
    public ActivatePessoaService(AmazonSNS snsClient, @Qualifier("registrationTopic") Topic registrationTopic) {
        this.snsClient = snsClient;
        this.registrationTopic = registrationTopic;
    }

    public void subscribeSms(String phoneNumber, String topicArn){
        SubscribeRequest subscribeRequest = new SubscribeRequest()
                .withProtocol("sms")
                .withEndpoint(phoneNumber)
                .withReturnSubscriptionArn(true)
                .withTopicArn(topicArn);
        SubscribeResult subscribe = snsClient.subscribe(subscribeRequest);
        System.out.println("Subscricao arn: "+ subscribe.getSubscriptionArn() +
                "\n Status: " + subscribe.getSdkHttpMetadata().getHttpStatusCode());
    }

    @Async
    public void sendSnsGlobalNewRegister(String phoneNumber){
        subscribeSms(phoneNumber, registrationTopic.getTopicArn());
        HashMap<String, String> attributes = new HashMap<>(1);
        attributes.put("DefaultSMSType", "Promotional");
        SetSMSAttributesRequest setSMSRequest = new SetSMSAttributesRequest()
                .withAttributes(attributes);
        snsClient.setSMSAttributes(setSMSRequest);
        PublishRequest request = new PublishRequest()
                .withMessage("Bem vindo seu cadastro foi efetuado com sucesso\n" +
                        "Ative seu cadastro com o código: " + randomCode())
                .withPhoneNumber(phoneNumber);
        PublishResult result = snsClient.publish(request);
        System.out.println("Send SMS for register, id: "+ result.getMessageId() +
                "\n Status: " + result.getSdkHttpMetadata().getHttpStatusCode());
    }

    private StringBuilder randomCode(){
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i <=6; i++) code.append(random.nextInt(10));
        return code;
    }

    public void publishEvent(String message) {
        snsClient.publish(registrationTopic.getTopicArn(),
                message);
    }

    public void sendSubscribeEmail(String emailAddress){
        SubscribeRequest subscribeRequest = new SubscribeRequest()
                .withTopicArn(registrationTopic.getTopicArn())
                .withProtocol("email")
                .withEndpoint(emailAddress);
        snsClient.subscribe(subscribeRequest);
    }

    public void sendEmailMessage(String subject, String message){
        PublishRequest request = new PublishRequest()
                .withTopicArn(registrationTopic.getTopicArn())
                .withSubject(subject)
                .withMessage(message);
        snsClient.publish(request);
    }



}
