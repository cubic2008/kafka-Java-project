package admin;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.KafkaFuture;

public class KafkaAdminTool {

	public static void main(String[] args) {
		
		for (int i = 0; i < args.length; i ++) {
			System.out.printf("args[%d] = %s%n", i, args[i]);
		}
		KafkaAdminTool tool = new KafkaAdminTool();
		if (args.length >= 1) {
			switch (args[0].toUpperCase()) {
				case "CT":
					if (args.length >= 3) {
						String topicName = args[1];
						short partitionCount = Short.parseShort(args[2]);
						short replicaCount = Short.parseShort(args[3]);
						tool.createTopic(topicName, partitionCount, replicaCount);
						System.exit(0);
					}
					break;
				case "DT":
					if (args.length >= 3) {
						String topicName = args[1];
						tool.describeTopic(topicName);
						System.exit(0);
					}
					break;
			}
		}
		usage();
		System.exit(-1);
		
		

	}

	private static void usage() {
		System.err.println("Invalid parameters!");
		System.err.println("Usage: java KafkaAdminTool command options");
		System.err.println("\t command: CT - Create a topic");
		System.err.println("\t\toptions: topic-name partition-count replica-count");
		System.err.println("\t command: DT - Describe a topic");
		System.err.println("\t\toptions: topic-name");
		
	}

	private void createTopic(String topicName, short partitioinCount, short replicaCount) {
		Properties props = getProps();
		NewTopic requestedTopic = new NewTopic(topicName, partitioinCount, replicaCount);
		AdminClient client = AdminClient.create(props);
		CreateTopicsResult topicResult = client.createTopics(Collections.singleton(requestedTopic)); 
		try {
			topicResult.values().get(topicName).get();
			System.out.printf("Topic is created: %s%n", topicName);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	private void describeTopic(String topicName) {
		Properties props = getProps();
		AdminClient client = AdminClient.create(props);
		DescribeTopicsResult topicResult = client.describeTopics(Arrays.asList(topicName));
		try {
			for (Map.Entry<String, KafkaFuture<TopicDescription>> topicEntry : topicResult.topicNameValues().entrySet()) {
				System.out.println("Topic: " + topicEntry.getKey());
				TopicDescription topicDescription = topicEntry.getValue().get();
				System.out.printf("%s : %s%n", topicDescription.name(), topicDescription.toString());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	private Properties getProps() {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092,localhost:9093");
//		props.put("group.id", groupId);
//		props.put("enable.auto.commit", "true");
//		props.put("auto.commit.interval.ms", "1000");
//		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//		props.put("value.deserializer", "serializers.AppEventSerde");
//		props.put("schema.registry.url", "http://localhost:8081");
//		props.put("auto.offset.reset", "earliest");
		return props;
	}

}
