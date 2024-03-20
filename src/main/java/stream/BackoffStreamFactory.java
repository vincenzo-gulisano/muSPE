package stream;

import component.StreamConsumer;
import component.StreamProducer;
import common.util.backoff.Backoff;
import java.util.concurrent.atomic.AtomicInteger;

public class BackoffStreamFactory implements StreamFactory {

	private final AtomicInteger indexes = new AtomicInteger();

	@Override
	public <T> Stream<T> newStream(StreamProducer<T> from,
			StreamConsumer<T> to,
			int capacity, Backoff backoff) {
		return new BackoffStream<>(
				getStreamId(from, to), indexes.getAndIncrement(), from, to, capacity, backoff);
	}

}
