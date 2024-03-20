package component.sink;

import org.apache.commons.lang3.Validate;

/**
 * Base implementation of {@link Sink} that applies a given {@link SinkFunction}
 * to each input
 * tuple.
 *
 * @param <IN> The type of input tuples.
 */
public class BaseSink<IN> extends AbstractSink<IN> {

  private final SinkFunction<IN> function;

  /**
   * Construct.
   * 
   * @param id       The unique ID of this component.
   * @param function The function to be applied to each input tuple.
   */
  public BaseSink(String id, SinkFunction<IN> function) {
    super(id);
    Validate.notNull(function, "function");
    this.function = function;
  }

  @Override
  public void processTuple(IN tuple) {
    if (function.isEnabled()) {
      function.accept(tuple);
    }
  }

  @Override
  public void enable() {
    function.enable();
    super.enable();
  }

  @Override
  public void disable() {
    System.out.println("Deactivating " + this.state.getId());
    super.disable();
    function.disable();
  }

  @Override
  public void ping() {
  }
}
