package serializers;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import domains.AppEvent;
import domains.AppEventStatus;

public class AppEventSerde implements Serializer<AppEvent>, Deserializer<AppEvent> {

	@Override
	public byte[] serialize(String topic, AppEvent event) {
		String s = String.format("%36s %15d %50s %250s %c", event.getEventId(), event.getTimestamp(), event.getSubject(), event.getMessage(), event.getStatus().getCode());
		try {
			return s.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		Deserializer.super.configure(configs, isKey);
	}

	@Override
	public void close() {
		Deserializer.super.close();
	}

	@Override
	public AppEvent deserialize(String topic, byte[] data) {
		String s = new String(data);
		return new AppEvent(
				s.substring(0, 36),
				Long.parseLong(s.substring(37, 52).trim()),
				s.substring(53, 103).trim(),
				s.substring(104, 354).trim(),
				AppEventStatus.fromCode(s.charAt(355)) );
	}

}
