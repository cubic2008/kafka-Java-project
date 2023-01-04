package app;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class JavaKafkaProducer {

	public static void main(String[] args) {
		
		for (int i = 0; i < 5; i ++) {
			sendMessage("helloworld", String.format("Message #%d (from Java Producer)", i + 1));
		}

	}

	private static void sendMessage(String topic, String message) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092,localhost:9093");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		Producer<String, String> producer = new KafkaProducer<>(props);
		ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(topic, null, message);
		producer.send(producerRecord);
		producer.close();		
	}

}
