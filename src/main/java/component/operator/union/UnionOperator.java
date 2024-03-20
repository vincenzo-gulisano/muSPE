package component.operator.union;

import component.ComponentType;
import component.operator.AbstractOperator;
import stream.Stream;

/**
 * Operator that unites multiple input streams into one. No guarantee on the ordering of the output
 * tuples.
 *
 * @param <T> The type of input/output tuples.
 */
public class UnionOperator<T> extends AbstractOperator<T, T> {

  /**
   * Construct.
   *
   * @param id The unique ID of the component.operator.
   */
  public UnionOperator(String id) {
    super(id, ComponentType.UNION);
  }

  @Override
  protected final void process() {
    if (isFlushed()) {
      return;
    }
    Stream<T> output = getOutput();
    int finishedInputs = 0;
    for (Stream<T> in : getInputs()) {
      T inTuple = in.getNextTuple(getIndex());
      finishedInputs += isStreamFinished(inTuple, in) ? 1 : 0;
      if (inTuple != null) {
        increaseTuplesRead();
        increaseTuplesWritten();
        output.addTuple(inTuple, getIndex());
      }
    }
    if (finishedInputs == getInputs().size()) {
      flush();
      output.flush();
      return;
    }
  }

  @Override
  public void addInput(Stream<T> stream) {
    state.addInput(stream);
  }

  /**
   * Not meaningful in this component.operator, use {@link #getInputs()} instead.
   *
   * @throws UnsupportedOperationException always, since {@link UnionOperator} has multiple inputs.
   */
  @Override
  public Stream<T> getInput() {
    throw new UnsupportedOperationException(
        String.format("'%s': Unions have multiple inputs!", state.getId()));
  }

}
