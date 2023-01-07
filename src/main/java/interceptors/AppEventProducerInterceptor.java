package interceptors;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Headers;

import domains.AppEvent;
import domains.AppEventStatus;

public class AppEventProducerInterceptor implements ProducerInterceptor<String, AppEvent> {
	private static Map<AppEventStatus, Long> stats = new HashMap<>(); 

	@Override
	public void configure(Map<String, ?> configs) {
//		System.out.println("--------AppEventInterceptor--------");
//		for (Map.Entry<String, ?> config : configs.entrySet()) {
//			System.out.printf("%s : %s%n", config.getKey(), config.getValue().toString());
//		}
//		System.out.println("-----------------------------------");
	}

	@Override
	public ProducerRecord<String, AppEvent> onSend(ProducerRecord<String, AppEvent> record) {
		long count = stats.getOrDefault(record.value().getStatus(), 0L) + 1;
		stats.put(record.value().getStatus(), count);
		System.out.printf("AppEvent sent: status = %s : key = %s, %s%n", record.value().getStatus().getName(), record.key(), count);
		
		Headers headers = record.headers();
		String traceId = UUID.randomUUID().toString();
		headers.add("traceId", traceId.getBytes());
		System.out.println("Created traceId: " + traceId);
		
		return record;
	}

	@Override
	public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
		if (exception != null) {
			System.out.println("producer send exception " + exception.getMessage());
		} else {
			System.out.println(String.format("Ack'ed topic=%s, partition=%d, offset=%d\n",
			metadata.topic(), metadata.partition(), metadata.offset()));
		}
	}

	@Override
	public void close() {}

}
