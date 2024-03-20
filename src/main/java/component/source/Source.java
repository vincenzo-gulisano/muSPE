package component.source;

import component.StreamProducer;

/**
 * The component.source is the first component in a query. It generates the tuples that are
 * processed by the
 * stream processing system.
 *
 * @param <OUT> The type of output tuples.
 */
public interface Source<OUT> extends StreamProducer<OUT> {

  OUT getNextTuple();

  @Override
  default int getTopologicalOrder() {
    return 1;
  }

  int getPriority();

  void setPriority(int priority);

}
