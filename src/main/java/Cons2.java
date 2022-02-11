import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

public class Cons2 {
    public static void main(String[] args) {
    Properties producerConfig = new Properties();
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092,localhost:39092,localhost:49092");
        producerConfig.put(ProducerConfig.ACKS_CONFIG, "all");
        producerConfig.put(ProducerConfig.RETRIES_CONFIG, 0);
        producerConfig.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 3000);
        producerConfig.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, 3000);
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

    Properties consumerConfig = new Properties();
        consumerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092,localhost:39092,localhost:49092");
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group2");
        consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerConfig);
    Cons2.TestConsumerRebalanceListener rebalanceListener = new Cons2.TestConsumerRebalanceListener();
        consumer.subscribe(Collections.singletonList("topic51"), rebalanceListener);
    Producer<String, String> producer = new KafkaProducer<String, String>(producerConfig);
        int counter = 1;
        while (true) {
        ConsumerRecords<String, String> records = consumer.poll(1000);
        for (ConsumerRecord<String, String> record : records) {
            System.out.printf("Received Message topic =%s, partition =%s, offset = %d, key = %s, value = %s\n", record.topic(), record.partition(), record.offset(),record.key(), record.value());
            System.out.println("current time: " + System.currentTimeMillis());
            counter ++;
            if(record.value().equals("message-2000000")){
                System.out.println(System.currentTimeMillis());
                System.out.println("counter: " + counter);
            }
        }
        consumer.commitSync();
    }
}

private static class TestConsumerRebalanceListener implements ConsumerRebalanceListener {
    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        System.out.println("Called onPartitionsRevoked with partitions:" + partitions);
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        System.out.println("Called onPartitionsAssigned with partitions:" + partitions);
    }
}
}
