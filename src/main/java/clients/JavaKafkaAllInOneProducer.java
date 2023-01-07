package clients;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import domains.AppEvent;
import domains.AppEventStatus;

public class JavaKafkaAllInOneProducer {

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
//		props.put("interceptor.classes", "interceptors.AppEventProducerInterceptor");
		props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, "interceptors.AppEventProducerInterceptor");
		props.put("acks", "all");
		props.put("retries", "3");
		props.put("max.in.flight.requests.per.connection", "5");
		Producer<String, AppEvent> producer = new KafkaProducer<>(props);
		ProducerRecord<String, AppEvent> producerRecord = new ProducerRecord<>(topic, event.getEventId(), event);
		
		// Option #1: obtain the send result synchronously
//		Future<RecordMetadata> futureResult = producer.send(producerRecord);
//		try {
//			RecordMetadata result = futureResult.get();
//			System.out.printf("Message sent: offset = %d, topic = %s, timestamp = %Tc %n", result.offset(), result.topic(),
//					result.timestamp());
//		} catch (InterruptedException | ExecutionException e) {
//			e.printStackTrace();
//		}
		// Option #2: obtain the send result asynchronously
//		producer.send(producerRecord, new Callback() {
//			
//			@Override
//			public void onCompletion(RecordMetadata metadata, Exception exception) {
//				System.out.print(exception == null ? "Message sent: " : "Error in sending message: ");
//				System.out.printf("offset = %d, topic = %s, timestamp = %Tc %n", metadata.offset(), metadata.topic(),
//							metadata.timestamp());
//			}
//		});
		// Option #3: obtain the send result asynchronously, using lambda
		producer.send(producerRecord, (metadata, exception) -> {
			System.out.print(exception == null ? "Message sent: " : "Error in sending message: ");
			System.out.printf("offset = %d, topic = %s, timestamp = %Tc %n", metadata.offset(), metadata.topic(),
						metadata.timestamp());
		});

		producer.close();		
	}

}
