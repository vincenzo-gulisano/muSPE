package common.util.backoff;

/**
 * {@link Backoff} implementation that does nothing.
 */
public enum InactiveBackoff implements Backoff {
  INSTANCE;

  @Override
  public void backoff() {
  }

  @Override
  public void relax() {
  }

  @Override
  public Backoff newInstance() {
    return INSTANCE;
  }
}
