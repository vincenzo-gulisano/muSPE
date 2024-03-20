package component;

import common.Named;
import java.util.Collection;
import stream.Stream;

/**
 * A stream {@link Component} that produces tuples.
 *
 * @param <OUT> The output type of this component.
 */
public interface StreamProducer<OUT> extends Named, Component {

  /**
   * Connect this producer with the given {@link StreamConsumer} using the provided stream.
   * Different implementations allow one or more calls to this function.
   *
   * @param stream The {@link Stream} that forms the data connection.
   * @see ConnectionsNumber
   */
  void addOutput(Stream<OUT> stream);

  /**
   * Get the output {@link Stream} of this producer, <emph>if is the type of producer that always
   * has a unique input.</emph> {@link StreamProducer}s that cannot conform to this interface can
   * throw {@link UnsupportedOperationException} (this is done for example in {@link
   * component.operator.router.BaseRouterOperator}.
   *
   * @return The unique output stream of this producer.
   */
  Stream<OUT> getOutput() throws UnsupportedOperationException;

  /**
   * Get all the output {@link Stream}s of this producer.
   *
   * @return All the output streams of this producer.
   */
  Collection<? extends Stream<OUT>> getOutputs();

  @Override
  default long getOutputQueueSize() {
    long size = 0;
    for (Stream<?> output : getOutputs()) {
      size += output.size();
    }
    return size;
  }

}
