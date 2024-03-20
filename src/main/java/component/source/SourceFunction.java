package component.source;

import component.ComponentFunction;
// import scheduling.haren.HarenFeatureTranslator;
import java.util.function.Supplier;

/**
 * Supplier of new tuples for a {@link BaseSource}.
 *
 * @param <OUT> The type of supplied tuples.
 */
public interface SourceFunction<OUT> extends ComponentFunction, Supplier<OUT> {

  default boolean isInputFinished() {
    return false;
  }
}
