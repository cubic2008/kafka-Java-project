package app;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.cubic.kafka.datatypes.Event;

public class JavaKafkaAvroConsumer {

	public static void main(String[] args) {
		
		receiveMessages("helloworld-avro", "helloconsumer");

	}

	private static void receiveMessages(String topic, String groupId) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092,localhost:9093");
		props.put("group.id", groupId);
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
		props.put("schema.registry.url", "http://localhost:8081");
		try (KafkaConsumer<CharSequence, Event> consumer = new KafkaConsumer<>(props)) {
			consumer.subscribe(Arrays.asList(topic));
			while (true) {
				ConsumerRecords<CharSequence, Event> records = consumer.poll(Duration.ofMillis(100));
				for (ConsumerRecord<CharSequence, Event> record : records) {
					System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(),record.key(), record.value());
				}
				
			}
		}
	}


}
