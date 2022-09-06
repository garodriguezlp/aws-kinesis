package com.example.awskinesis.aws.sdk;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.amazonaws.regions.Regions.US_EAST_1;

@Configuration
public class Config {

    @Bean
    public AmazonKinesis awsKinesis() {
        return AmazonKinesisClientBuilder.standard()
                .withRegion(US_EAST_1)
                .build();
    }


}
