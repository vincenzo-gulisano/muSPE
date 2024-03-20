package component;

import common.Named;
import java.util.Collection;
import stream.Stream;

/**
 * A stream {@link Component} that consumes tuples.
 *
 * @param <IN> The input type for this component.
 */
public interface StreamConsumer<IN> extends Named, Component {

  /**
   * Connect this consumer with the given {@link StreamProducer} using the provided stream.
   * Different implementations allow one or more calls to this function.
   *
   * @param stream The {@link Stream} that forms the data connection.
   * @see ConnectionsNumber
   */
  void addInput(Stream<IN> stream);

  /**
   * Get the input {@link Stream} of this consumer, if is the type of consumer that always has a
   * unique input. {@link StreamConsumer}s that cannot conform to this interface can throw {@link
   * UnsupportedOperationException} (this is done for example in {@link
   * component.operator.union.UnionOperator})
   *
   * @return The unique input stream of this consumer.
   */
  Stream<IN> getInput() throws UnsupportedOperationException;

  /**
   * Get all the input {@link Stream}s of this consumer.
   *
   * @param <T> The superclass of all input contents (in the case of input streams of different
   *     types, as in {@link component.operator.in2.Operator2In}.
   * @return All the input streams of this consumer.
   */
  <T> Collection<? extends Stream<T>> getInputs();

  @Override
  default int getTopologicalOrder() {
    int maxUpstreamOrder = 0;
    for (Stream<?> input : getInputs()) {
      for (StreamProducer<?> source : input.producers()) {
        maxUpstreamOrder = Math.max(source.getTopologicalOrder(), maxUpstreamOrder);
      }
    }
    return maxUpstreamOrder + 1;
  }

}
