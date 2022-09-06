package com.example.awskinesis.aws.sdk;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.amazonaws.regions.Regions.US_EAST_1;

@Configuration
public class Config {

    @Value("${aws.access-key-id}")
    private String accessKeyId;

    @Value("${aws.secret-key}")
    private String secretKey;

    @Bean
    public AmazonKinesis awsKinesis() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKeyId, secretKey);
        return AmazonKinesisClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(US_EAST_1)
                .build();
    }
}
