package com.postech.fiap.service;

import com.google.api.gax.core.GoogleCredentialsProvider;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.TopicName;
import com.google.pubsub.v1.PubsubMessage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;

@ApplicationScoped
public class PubSubProducers {

    @Inject
    @ConfigProperty(name = "quarkus.google.cloud.project-id")
    String projectId;

    @Produces
    @ApplicationScoped
    public Publisher userEventsPublisher() throws IOException {
        TopicName topicName = TopicName.of(projectId, "user-events");
        GoogleCredentialsProvider credentialsProvider = GoogleCredentialsProvider.newBuilder().build();
        return Publisher.newBuilder(topicName)
                .setCredentialsProvider(credentialsProvider)
                .build();
    }

    @Produces
    @ApplicationScoped
    public Subscriber userEventsSubscriber(MessageReceiver receiver) {
        ProjectSubscriptionName subName =
                ProjectSubscriptionName.of(projectId, "user-events-sub");
        GoogleCredentialsProvider credentialsProvider = GoogleCredentialsProvider.newBuilder().build();
        return Subscriber.newBuilder(subName, receiver)
                .setCredentialsProvider(credentialsProvider)
                .build();
    }

    @Produces
    @ApplicationScoped
    public MessageReceiver userEventsMessageReceiver() {
        return (PubsubMessage message, AckReplyConsumer consumer) -> {
            try {
                String data = (!message.getData().isEmpty()) ? message.getData().toStringUtf8() : "<no-data>";
                System.out.println("[PubSubReceiver] Received message: " + data);
                consumer.ack();
            } catch (Exception e) {
                System.err.println("[PubSubReceiver] Failed to process message: " + e.getMessage());
                consumer.nack();
            }
        };
    }
}
