package com.example.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import java.util.List;

public class Main {


    public static void main(String[] args) throws InterruptedException {

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        String queueUrl= args[0];

        while(true) {
           
         // receive messages from the queue
         List<Message> messages = sqs.receiveMessage(queueUrl).getMessages();
         // delete messages from the queue
         for (Message m : messages) {
             System.out.println(m.getBody());
             sqs.deleteMessage(queueUrl, m.getReceiptHandle());
         }
          //do your job...
          Thread.sleep(5 * 1000);
          
        }
    }
}
