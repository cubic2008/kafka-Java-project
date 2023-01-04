package interceptors;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

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
		return record;
	}

	@Override
	public void onAcknowledgement(RecordMetadata metadata, Exception exception) {}

	@Override
	public void close() {}

}
