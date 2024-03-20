package component.operator;

import component.AbstractComponent;
import component.ComponentType;

import java.util.Collection;
import stream.Stream;

/**
 * Abstract implementation of {@link Operator} that handles basic changes to the state of the
 * component.
 *
 * @param <IN> The type of input tuples.
 * @param <OUT> The type of output tuples.
 */
public abstract class AbstractOperator<IN, OUT> extends AbstractComponent<IN, OUT>
    implements Operator<IN, OUT> {

  private final int INPUT_KEY = 0;
  private final int OUTPUT_KEY = 0;

  public AbstractOperator(
      String id, ComponentType type) {
    super(id, type);
  }

  @Override
  public ComponentType getType() {
    return state.getType();
  }

  @Override
  public void addOutput(Stream<OUT> stream) {
    state.addOutput(OUTPUT_KEY, stream);
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
  public Stream<OUT> getOutput() {
    return state.getOutput(OUTPUT_KEY);
  }

  public Collection<? extends Stream<IN>> getInputs() {
    return state.getInputs();
  }

  public Collection<? extends Stream<OUT>> getOutputs() {
    return state.getOutputs();
  }

  @Override
  protected void flushAction() {
    getOutputs().stream().forEach(output -> output.flush());
  }
}
