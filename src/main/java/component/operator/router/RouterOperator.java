package component.operator.router;

import component.operator.Operator;
import java.util.Collection;
import stream.Stream;

/**
 * {@link Operator} that copies an input tuple to multiple output streams.
 *
 * @param <T> The type of input/output tuples.
 */
public interface RouterOperator<T> extends Operator<T, T> {

  Collection<? extends Stream<T>> chooseOutputs(T tuple);

}
