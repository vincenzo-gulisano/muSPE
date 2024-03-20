package component.source;

import org.apache.commons.lang3.Validate;

/**
 * Base implementation of {@link Source} that generates data through a {@link SourceFunction}l
 *
 * @param <OUT> The type of output tuples.
 */
public class BaseSource<OUT> extends AbstractSource<OUT> {

  private final SourceFunction<OUT> function;

  /**
   * Construct.
   *  @param id The unique ID of this component.
   * @param function The {@link SourceFunction} that generates the output tuples.
   */
  public BaseSource(String id, SourceFunction<OUT> function) {
    super(id);
    Validate.notNull(function, "function");
    this.function = function;
  }

  @Override
  public OUT getNextTuple() {
    return function.get();
  }

  @Override
  protected boolean isInputFinished() {
    return function.isInputFinished();
  }

  @Override
  public void enable() {
    function.enable();
    super.enable();
  }

  @Override
  public void disable() {
    super.disable();
    function.disable();
  }

  @Override
  public boolean canRun() {
    return function.canRun() && super.canRun();
  }
}
