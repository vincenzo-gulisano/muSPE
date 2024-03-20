package component.operator.in1;

import component.operator.Operator;
import java.util.List;

/**
 * {@link Operator} with one input and one output. Can produce multiple output tuples for every
 * input tuples.
 *
 * @param <IN> The type of input tuples.
 * @param <OUT> The type of output tuples.
 */
public interface Operator1In<IN, OUT> extends Operator<IN, OUT> {

  /**
   * Apply a function to the input tuple, transforming it into zero or more output tuples.
   *
   * @param tuple The tuple to be processed.
   * @return A list of zero or more output tuples.
   */
  List<OUT> processTupleIn1(IN tuple);
}
