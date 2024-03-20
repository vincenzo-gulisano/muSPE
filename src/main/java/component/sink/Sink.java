package component.sink;

import component.StreamConsumer;

/**
 * A component.sink is a terminal component that consumes tuples but does not output a new stream.
 *
 * @param <IN> The type of input tuples.
 */
public interface Sink<IN> extends StreamConsumer<IN> {
	void processTuple(IN tuple);

}
