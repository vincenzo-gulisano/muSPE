package component.sink;

import component.AbstractComponent;
import component.ComponentType;

import java.util.Collection;
import query.muSPEContext;
import stream.Stream;

/**
 * Abstract implementation of a {@link Sink}, controlling basic changes to the
 * state.
 *
 * @param <IN> The type of input tuples
 */
public abstract class AbstractSink<IN> extends AbstractComponent<IN, Void> implements Sink<IN> {

  private static final int INPUT_KEY = 0;

  /**
   * Construct.
   *
   * @param id The unique ID of this component.
   */
  public AbstractSink(String id) {
    super(id, ComponentType.SINK);
  }

  @Override
  protected final void process() {
    if (isFlushed()) {
      return;
    }

    Stream<IN> input = getInput();
    IN tuple = input.getNextTuple(getIndex());

    if (isStreamFinished(tuple, input)) {
      flush();
      return;
    }

    if (tuple != null) {
      increaseTuplesRead();
      increaseTuplesWritten();
      processTuple(tuple);
    } else {
      ping();
    }
  }

  @Override
  protected void flushAction() {
    muSPEContext.sinkFinished(this);
  }

  @Override
  public void addInput(Stream<IN> stream) {
    state.addInput(INPUT_KEY, stream);
  }

  @Override
  public Stream<IN> getInput() {
    return state.getInput(INPUT_KEY);
  }

  @Override
  public Collection<? extends Stream<IN>> getInputs() {
    return state.getInputs();
  }

  public abstract void processTuple(IN tuple);

  public abstract void ping();
}
