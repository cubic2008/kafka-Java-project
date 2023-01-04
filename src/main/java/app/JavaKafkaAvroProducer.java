package app;

import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.cubic.kafka.datatypes.Event;
import com.cubic.kafka.datatypes.EventStatus;

public class JavaKafkaAvroProducer {

	public static void main(String[] args) {
		
		for (long i = 0; i < 5; i ++) {
			Event e = new Event(UUID.randomUUID().toString(), System.currentTimeMillis(), EventStatus.Major);
			sendEventMessage("helloworld-avro", e);
		}

	}

	private static void sendEventMessage(String topic, Event event) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092,localhost:9093");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
		props.put("schema.registry.url", "http://localhost:8081");
		Producer<CharSequence, Event> producer = new KafkaProducer<>(props);
		ProducerRecord<CharSequence, Event> producerRecord = new ProducerRecord<>(topic, event.getEventId(), event);
		producer.send(producerRecord);
		producer.close();		
	}

}
