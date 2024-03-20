package component.sink;

import component.ComponentFunction;
import java.util.function.Consumer;

/**
 * Function that works on all tuples accepted by a {@link BaseSink}.
 *
 * @param <IN> The type of input tuples.
 */
public interface SinkFunction<IN> extends ComponentFunction, Consumer<IN> {}
