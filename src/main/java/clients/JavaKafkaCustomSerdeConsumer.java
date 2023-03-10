package clients;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import domains.AppEvent;

public class JavaKafkaCustomSerdeConsumer {

	public static void main(String[] args) {
		
		receiveMessages("helloworld-cust-serde", "helloconsumer");

	}

	private static void receiveMessages(String topic, String groupId) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092,localhost:9093");
		props.put("group.id", groupId);
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "serializers.AppEventSerde");
		props.put("schema.registry.url", "http://localhost:8081");
		try (KafkaConsumer<String, AppEvent> consumer = new KafkaConsumer<>(props)) {
			consumer.subscribe(Arrays.asList(topic));
			while (true) {
				ConsumerRecords<String, AppEvent> records = consumer.poll(Duration.ofMillis(100));
				for (ConsumerRecord<String, AppEvent> record : records) {
					System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(),record.key(), record.value());
				}
				
			}
		}
	}


}
