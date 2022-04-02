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
         for (Message m : messages) {
             System.out.println(m.getBody());
             sqs.deleteMessage(queueUrl, m.getReceiptHandle());
         }
    }
}
