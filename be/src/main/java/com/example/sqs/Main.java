package com.example.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import java.util.Date;
import java.util.List;

public class Main {


    public static void main(String[] args) {

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        String queueUrl= args[0];
         // receive messages from the queue
         List<Message> messages = sqs.receiveMessage(queueUrl).getMessages();

         // delete messages from the queue
         for (software.amazon.awssdk.services.sqs.model.Message m : messages) {
             System.out.println(m.body());
             sqs.deleteMessage(queueUrl, m.getReceiptHandle());
         }
    }


    public static  List<Message> receiveMessages(SqsClient sqsClient, String queueUrl) {

        System.out.println("\nReceive messages");

        try {
            // snippet-start:[sqs.java2.sqs_example.retrieve_messages]
            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(5)
                .build();
            List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();
            return messages;
        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return null;
        // snippet-end:[sqs.java2.sqs_example.retrieve_messages]
    }
    // snippet-end:[sqs.java2.sqs_example.main]
}
