package component.source;

import component.ComponentType;

import java.util.Collection;
import query.muSPEContext;
import stream.Stream;

public abstract class AbstractSource<OUT> extends component.AbstractComponent<Void, OUT>
    implements Source<OUT> {

  private int priority;
  private static final int OUTPUT_KEY = 0;

  public AbstractSource(String id) {
    super(id, ComponentType.SOURCE);
  }

  @Override
  public void addOutput(Stream<OUT> stream) {
    state.addOutput(OUTPUT_KEY, stream);
  }

  @Override
  public Stream<OUT> getOutput() {
    return state.getOutput(OUTPUT_KEY);
  }

  @Override
  protected final void process() {

    if (isFlushed()) {
      return;
    }

    OUT tuple = getNextTuple();
    Stream<OUT> output = getOutput();
    if (isInputFinished()) {
      flush();
      return;
    }
    if (tuple != null) {
      increaseTuplesRead();
      increaseTuplesWritten();
      output.addTuple(tuple, getIndex());
    }
  }

  protected abstract boolean isInputFinished();

  @Override
  protected void flushAction() {
    if (muSPEContext.isFlushingEnabled()) {
      getOutput().flush();
    }
  }

  @Override
  public Collection<? extends Stream<OUT>> getOutputs() {
    return state.getOutputs();
  }

  public boolean canRun() {
    return getOutput().remainingCapacity() > 0;
  }

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }
}
