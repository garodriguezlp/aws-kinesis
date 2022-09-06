package com.example.awskinesis.aws.sdk;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.amazonaws.services.kinesis.model.PutRecordResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.nio.ByteBuffer.wrap;

@Component
@Order(1)
@Profile("aws-sdk")
public class KinesisProducer implements CommandLineRunner {

    @Autowired
    private AmazonKinesis kinesis;

    @Value("${kinesis.stream.name}")
    private String streamName;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Start to put records on kinesis....");
        List.of("John", "Paul", "George", "Ringo", "Pete", "Stuart", "Mick", "Keith", "Ronnie", "Charlie")
                .forEach(name -> putRecord(name, "english"));
        List.of("Juan", "Pedro", "Jorge", "Ricardo", "Pablo", "Santiago", "Miguel", "Kevin", "Ronald", "Carlos")
                .forEach(name -> putRecord(name, "spanish"));
    }

    private void putRecord(String name, String key) {
        System.out.println("Putting record for name: " + name);
        PutRecordRequest putRecordRequest = new PutRecordRequest();
        putRecordRequest.setStreamName(streamName);
        putRecordRequest.setPartitionKey(key);
        putRecordRequest.setData(wrap("Hello, ".concat(name).getBytes()));
        PutRecordResult putRecordResult = kinesis.putRecord(putRecordRequest);
        System.out.println("Put record result: " + putRecordResult);
    }
}
