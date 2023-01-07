package clients;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import domains.AppEvent;

public class JavaKafkaAllInOneConsumer {

	public static void main(String[] args) {
		
		receiveMessages("helloworld-partition", "helloconsumer");

	}

	private static void receiveMessages(String topic, String groupId, Integer...partitions) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092,localhost:9093");
		props.put("group.id", groupId);
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "serializers.AppEventSerde");
		props.put("schema.registry.url", "http://localhost:8081");
		props.put("auto.offset.reset", "earliest");
		props.put("interceptor.classes", "interceptors.AppEventConsumerInterceptor");
		try (KafkaConsumer<String, AppEvent> consumer = new KafkaConsumer<>(props)) {
			if (partitions.length == 0) {
				consumer.subscribe(Arrays.asList(topic));
			} else {
				List<TopicPartition> topicPartitions = Arrays.asList(partitions).stream().map(p -> new TopicPartition(topic, p)).collect(Collectors.toList());
				consumer.assign(topicPartitions);
			}
			while (true) {
				ConsumerRecords<String, AppEvent> records = consumer.poll(Duration.ofMillis(100));
				for (ConsumerRecord<String, AppEvent> record : records) {
					System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(),record.key(), record.value());
				}
			}
		}
	}
	
}
