package clients;

import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.cubic.kafka.datatypes.EventStatus;

import domains.AppEvent;
import domains.AppEventStatus;

public class JavaKafkaCustomSerdeWithPartitionerProducer {

	public static void main(String[] args) {
		
		for (long i = 0; i < 5; i ++) {
			AppEvent e = new AppEvent(
					UUID.randomUUID().toString(), 
					System.currentTimeMillis(),
					"Event #" + (i + 1),
					"Event message ...",
					AppEventStatus.values()[(int)i % AppEventStatus.values().length]);
			sendEventMessage("helloworld-partition", e);
		}

	}

	private static void sendEventMessage(String topic, AppEvent event) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092,localhost:9093");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "serializers.AppEventSerde");
		props.put("schema.registry.url", "http://localhost:8081");
		props.put("partitioner.class", "partitioners.EventStatusPartitioner");
		props.put("interceptor.classes", "interceptors.AppEventProducerInterceptor");

		Producer<String, AppEvent> producer = new KafkaProducer<>(props);
		ProducerRecord<String, AppEvent> producerRecord = new ProducerRecord<>(topic, event.getEventId(), event);
		producer.send(producerRecord);
		producer.close();		
	}

}
