package stream;

import component.StreamConsumer;
import component.StreamProducer;
import common.util.backoff.Backoff;
import common.util.backoff.InactiveBackoff;

/** Factory for {@link Stream}s. */
public interface StreamFactory {

  <T> Stream<T> newStream(
      StreamProducer<T> from, StreamConsumer<T> to, int capacity, Backoff backoff);

  default <T> Stream<T> newStream(StreamProducer<T> from, StreamConsumer<T> to, int capacity) {
    return newStream(from, to, capacity, InactiveBackoff.INSTANCE);
  }
  
  default String getStreamId(StreamProducer<?> from, StreamConsumer<?> to) {
    return String.format("%s_%s", from.getId(), to.getId());
  }

}
