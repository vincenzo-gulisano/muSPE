package component.operator;

import component.StreamConsumer;
import component.StreamProducer;

/**
 * An component.operator is a processing unit that both consumes and produces tuples.
 *
 * @param <IN> The type of input tuples.
 * @param <OUT> The type of output tuples.
 */
public interface Operator<IN, OUT> extends StreamConsumer<IN>,
    StreamProducer<OUT> {

}
