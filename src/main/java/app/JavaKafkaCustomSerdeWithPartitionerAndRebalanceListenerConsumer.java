package app;

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

public class JavaKafkaCustomSerdeWithPartitionerAndRebalanceListenerConsumer {

	public static void main(String[] args) {
		
		receiveMessages("helloworld-partition", "helloconsumer");

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
		props.put("auto.offset.reset", "earliest");
		try (KafkaConsumer<String, AppEvent> consumer = new KafkaConsumer<>(props)) {
//			consumer.subscribe(Arrays.asList(topic));
			consumer.subscribe(Arrays.asList(topic),
		              new ConsumerRebalanceListener() {
		                  @Override
		                  public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
		                      System.out.printf("onPartitionsRevoked - partitions: %s%n",
		                              formatPartitions(partitions));
		                  }

		                  @Override
		                  public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
		                      System.out.printf("onPartitionsAssigned - partitions: %s%n", 
		                              formatPartitions(partitions));
		                  }
		              });
			while (true) {
				ConsumerRecords<String, AppEvent> records = consumer.poll(Duration.ofMillis(100));
				for (ConsumerRecord<String, AppEvent> record : records) {
					System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(),record.key(), record.value());
				}
				
			}
		}
	}
	
	private static List<String> formatPartitions(Collection<TopicPartition> partitions) {
	      return partitions.stream().map(topicPartition ->
	              String.format("topic: %s, partition: %s", topicPartition.topic(), topicPartition.partition()))
	                       .collect(Collectors.toList());
	}


}
