package com.example.awskinesis.aws.sdk;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.model.GetRecordsRequest;
import com.amazonaws.services.kinesis.model.GetRecordsResult;
import com.amazonaws.services.kinesis.model.GetShardIteratorRequest;
import com.amazonaws.services.kinesis.model.GetShardIteratorResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.amazonaws.services.kinesis.model.ShardIteratorType.TRIM_HORIZON;

@Component
@Order(2)
@Profile("aws-sdk")
public class KinesisConsumer implements CommandLineRunner {

    @Value("${kinesis.stream.name}")
    private String streamName;

    @Value("${kinesis.stream.shard-id}")
    private String shardId;

    @Autowired
    private AmazonKinesis kinesis;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Start to consume records from kinesis....");

        GetRecordsRequest recordsRequest = buildRecordsRequest();

        GetRecordsResult recordsResult = kinesis.getRecords(recordsRequest);
        while (!recordsResult.getRecords().isEmpty()) {
            System.out.println("Getting records using shard iterator: " + recordsRequest.getShardIterator());

            recordsResult.getRecords().stream()
                    .map(kinesisRecord -> new String(kinesisRecord.getData().array()))
                    .forEach(System.out::println);

            recordsRequest.setShardIterator(recordsResult.getNextShardIterator());
            recordsResult = kinesis.getRecords(recordsRequest);
        }
    }

    private GetRecordsRequest buildRecordsRequest() {
        GetRecordsRequest recordsRequest = new GetRecordsRequest();
        recordsRequest.setShardIterator(requestShardIterator().getShardIterator());
        recordsRequest.setLimit(5);
        return recordsRequest;
    }

    public GetShardIteratorResult requestShardIterator() {
        GetShardIteratorRequest readShardsRequest = new GetShardIteratorRequest();
        readShardsRequest.setStreamName(streamName);
        readShardsRequest.setShardIteratorType(TRIM_HORIZON);
        readShardsRequest.setShardId(shardId);
        return kinesis.getShardIterator(readShardsRequest);
    }
}
