package com.postech.fiap.service;

import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@ApplicationScoped
public class PubSubService {

//    @ConfigProperty(name = "gcp.project-id", defaultValue = "project-id")
//    String projectId;
//
//    public void publishMessage(String topicName, String messageJson) throws IOException, ExecutionException, InterruptedException {
//        TopicName topic = TopicName.of(projectId, topicName);
//        Publisher publisher = null;
//
//        try {
//            publisher = Publisher.newBuilder(topic).build();
//
//            ByteString data = ByteString.copyFromUtf8(messageJson);
//            PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
//                    .setData(data)
//                    .build();
//
//            publisher.publish(pubsubMessage).get();
//        } finally {
//            if (publisher != null) {
//                publisher.shutdown();
//            }
//        }
//    }
}
