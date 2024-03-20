package component.operator.in1;

import component.ComponentType;
import component.operator.AbstractOperator;
import java.util.List;
import stream.Stream;

/**
 * Default abstract implementation of {@link Operator1In}.
 *
 * @param <IN>  The type of input tuples.
 * @param <OUT> The type of output tuples.
 * @author palivosd
 */
public abstract class BaseOperator1In<IN, OUT> extends AbstractOperator<IN, OUT>
    implements Operator1In<IN, OUT> {

  /**
   * Construct.
   *
   * @param id The unique id of this component.
   */
  public BaseOperator1In(String id) {
    super(id, ComponentType.OPERATOR);
  }

  @Override
  protected final void process() {
    if (isFlushed()) {
      return;
    }

    Stream<IN> input = getInput();
    Stream<OUT> output = getOutput();

    IN inTuple = input.getNextTuple(getIndex());

    if (isStreamFinished(inTuple, input)) {
      flush();
      return;
    }

    if (inTuple != null) {
      increaseTuplesRead();
      List<OUT> outTuples = processTupleIn1(inTuple);
      if (outTuples != null) {
        for (OUT t : outTuples) {
          increaseTuplesWritten();
          output.addTuple(t, getIndex());
        }
      }
    }

  }

}
