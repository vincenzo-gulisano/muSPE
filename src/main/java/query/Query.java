package query;

import common.util.backoff.Backoff;
import common.util.backoff.ExponentialBackoff;
import component.Component;
import component.StreamConsumer;
import component.StreamProducer;
import component.operator.Operator;
import component.sink.*;
import component.source.*;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scheduling.muSPEScheduler;
import scheduling.basic.BasicMuSPEScheduler;
import stream.BackoffStreamFactory;
import stream.Stream;
import stream.StreamFactory;

import java.util.*;

/**
 * The main execution unit. Acts as a factory for the stream {@link Component}s
 * such as {@link
 * Operator}s, {@link Source}s and {@link Sink}s through various helper methods.
 * It also handles the
 * connections of the components with the correct types of {@link Stream}s and
 * the
 * activation/deactivation of the query. Activating the query also starts
 * executing it by delegating
 * this work to the provided {@link muSPEScheduler} implementation.
 */
public final class Query {

  private static final Logger LOGGER = LogManager.getLogger();
  public static final int DEFAULT_STREAM_CAPACITY = 10000;
  public static final int DEFAULT_SGSTREAM_MAX_LEVELS = 3;
  public static final String OPERATOR = "operator";
  public static final String SOURCE = "source";
  public static final String SINK = "sink";
  private final Map<String, Operator<?, ?>> operators = new HashMap<>();
  private final Map<String, Source<?>> sources = new HashMap<>();
  private final Map<String, Sink<?>> sinks = new HashMap<>();
  private final muSPEScheduler muSPEScheduler;
  private final StreamFactory streamFactory;
  private Backoff defaultBackoff = new ExponentialBackoff(1, 10, 3);
  private volatile boolean active;

  /** Construct. */
  public Query() {
    this(new BasicMuSPEScheduler(), new BackoffStreamFactory());
  }

  /**
   * Construct.
   *
   * @param muSPEScheduler The muSPEScheduler implementation to use when executing
   *                       the query after
   *                       Query{@link #activate()} is called.
   */
  public Query(muSPEScheduler muSPEScheduler, StreamFactory streamFactory) {
    this.muSPEScheduler = muSPEScheduler;
    this.streamFactory = streamFactory;
  }

  /**
   * Set the parameters for the default {@link ExponentialBackoff} strategy.
   *
   * @param min     The minimum backoff limit
   * @param max     The maximum backoff limit
   * @param retries The number of retries before the backoff limit is updated.
   */
  public synchronized void setBackoff(int min, int max, int retries) {
    this.defaultBackoff = new ExponentialBackoff(min, max, retries);
  }

  public synchronized void setBackoff(Backoff backoff) {
    this.defaultBackoff = backoff;
  }

  public synchronized <IN, OUT> Operator<IN, OUT> addOperator(Operator<IN, OUT> operator) {
    saveComponent(operators, operator, "component/operator");
    return operator;
  }

  // **
  public synchronized <T> Source<T> addSource(Source<T> source) {
    saveComponent(sources, source, SOURCE);
    return source;
  }

  // **
  public synchronized <T> Source<T> addBaseSource(String id, SourceFunction<T> function) {
    return addSource(new BaseSource<>(id, function));
  }

  public synchronized <T> Sink<T> addSink(Sink<T> sink) {
    saveComponent(sinks, sink, SINK);
    return sink;
  }

  public synchronized <T> Sink<T> addBaseSink(String id, SinkFunction<T> sinkFunction) {
    return addSink(new BaseSink<>(id, sinkFunction));
  }

  public synchronized <T> Query connect(StreamProducer<T> producer, StreamConsumer<T> consumer) {
    return connect(producer, consumer, defaultBackoff);
  }

  public synchronized <T> Query connect(
      StreamProducer<T> producer, StreamConsumer<T> consumer, Backoff backoff) {
    // Validate.isTrue(
    // consumer instanceof Operator2In == false,
    // "Error when connecting '%s': Please use connect2inXX() for Operator2In and
    // subclasses!",
    // consumer.getId());
    Stream<T> stream = getStream(producer, consumer, backoff);
    producer.addOutput(stream);
    consumer.addInput(stream);
    return this;
  }

  private synchronized <T> Stream<T> getStream(
      StreamProducer<T> producer, StreamConsumer<T> consumer, Backoff backoff) {
    Stream<T> stream = streamFactory.newStream(producer, consumer, DEFAULT_STREAM_CAPACITY, backoff);
    return stream;
  }

  /**
   * Activate and start executing the query.
   */
  public synchronized void activate() {
    muSPEContext.init(this);
    LOGGER.info("Activating query...");
    LOGGER.info(
        "Components: {} Sources, {} Operators, {} Sinks, {} Streams",
        sources.size(),
        operators.size(),
        sinks.size(),
        streams().size());
    muSPEScheduler.addTasks(sinks.values());
    muSPEScheduler.addTasks(operators.values());
    muSPEScheduler.addTasks(sources.values());
    muSPEScheduler.enable();
    muSPEScheduler.startTasks();
    active = true;
  }

  /** Deactivate and stop executing the query. */
  public synchronized void deActivate() {
    if (!active) {
      return;
    }
    LOGGER.info("Deactivating query...");
    muSPEScheduler.disable();
    LOGGER.info("Waiting for threads to terminate...");
    muSPEScheduler.stopTasks();
    LOGGER.info("DONE!");
    active = false;
    muSPEContext.terminated(this);
  }

  Collection<Source<?>> sources() {
    return sources.values();
  }

  public Collection<Sink<?>> sinks() {
    return sinks.values();
  }

  private Set<Stream<?>> streams() {
    Set<Stream<?>> streams = new HashSet<>();
    for (Operator<?, ?> op : operators.values()) {
      streams.addAll(op.getInputs());
    }
    return streams;
  }

  private <T extends Component> void saveComponent(Map<String, T> map, T component, String type) {
    Validate.validState(
        !map.containsKey(component.getId()),
        "A component of type %s  with id '%s' has already been added!",
        type,
        component);
    Validate.notNull(component);
    if (component.getId().contains("_")) {
      LOGGER.warn(
          "It is best to avoid component IDs that contain an underscore because it will make it more difficult to analyze statistics date. Offending component: {}",
          component);
    }
    map.put(component.getId(), component);
  }
}
