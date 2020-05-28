package com.example.integration.subflows.separateflows;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;

import java.util.Collection;

@EnableIntegration
@IntegrationComponentScan
public class SeparateFlowsExample {

    @MessagingGateway
    public interface NumbersClassifier {
        @Gateway(requestChannel = "multipleOfThreeFlow.input")
        void multipleOfThree(Collection<Integer> number);

        @Gateway(requestChannel = "remainderIsOneFlow.input")
        void remainderIsOne(Collection<Integer> numbers);

        @Gateway(requestChannel = "remainderIsTwoFlow.input")
        void remainderIsTwo(Collection<Integer> numbers);

    }

    @Bean
    QueueChannel multipleOfThreeChannel() {
        return new QueueChannel();

    }

    @Bean
    QueueChannel remainderIsOneChannel() {
        return new QueueChannel();

    }

    @Bean
    QueueChannel remainderIsTwoChannel() {
        return new QueueChannel();
    }

    @Bean
    public IntegrationFlow multipleOfThreeFlow() {
        return flow -> flow.split().<Integer>filter(this::isMultipleOfThree).channel("multipleOfThreeChannel");
    }

    @Bean
    public IntegrationFlow remainderIsOneFlow() {
        return flow -> flow.split().<Integer>filter(this::isRemainderIsOne).channel("remainderIsOneChannel");
    }

    @Bean
    public IntegrationFlow remainderIsTwoFlow() {
        return flow -> flow.split().<Integer>filter(this::isRemainderIsTwo).channel("remainderIsTwoChannel");
    }


    boolean isMultipleOfThree(Integer number) {
        return number % 3 == 0;
    }

    boolean isRemainderIsOne(Integer number) {
        return number % 3 == 1;
    }

    boolean isRemainderIsTwo(Integer number) {
        return number % 3 == 2;
    }
}
