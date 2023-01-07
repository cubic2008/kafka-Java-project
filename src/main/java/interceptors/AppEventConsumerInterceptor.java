package interceptors;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

import domains.AppEvent;

public class AppEventConsumerInterceptor implements ConsumerInterceptor<String, AppEvent> {
	// Implementing ConsumerInterceptor is needed to allow Kafka to recognize our intercepter

	@Override
	public void configure(Map<String, ?> configs) {
	}

	@Override
	public ConsumerRecords<String, AppEvent> onConsume(ConsumerRecords<String, AppEvent> records) {
		if (records.isEmpty()) {
			return records;
		} else {
			for (ConsumerRecord<String, AppEvent> record : records) {
				Headers headers = record.headers(); // We are looping through each record’s headers
				for (Header header : headers) {
					if ("traceId".equals(header.key())) { // Once we find our custom header we added from the producer intercepter, we will log it to standard output
						System.out.println("TraceId is: " + new String(header.value()));
					}
				}
			}
		}
		return records; // We return the records to continue on the path of callers from our interceptor
	}

	@Override
	public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {}

	@Override
	public void close() {
	}

}
