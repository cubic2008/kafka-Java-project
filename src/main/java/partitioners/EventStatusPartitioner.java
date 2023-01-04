package partitioners;

import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import domains.AppEvent;
import domains.AppEventStatus;

public class EventStatusPartitioner implements Partitioner {

	@Override
	public void configure(Map<String, ?> configs) {
	}

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		final List<PartitionInfo> partitionInfoList = cluster.availablePartitionsForTopic(topic);
		final int partitionSize = partitionInfoList.size();
		final AppEvent event = (AppEvent) value;
		if (event.getStatus() == AppEventStatus.Creation) {
			return partitionSize - 1;
		} else {
			return event.getStatus().ordinal() % (partitionSize - 1);
		}
	}

	@Override
	public void close() {
	}

}
