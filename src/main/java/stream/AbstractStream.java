package stream;

public abstract class AbstractStream<T> implements Stream<T> {

  public static final String METRIC_IN = "IN";
  public static final String METRIC_OUT = "OUT";
  protected final String id;
  protected final int index;
  protected boolean enabled;

  protected AbstractStream(String id, int index) {
    this.id = id;
    this.index = index;
  }

  @Override
  public final void addTuple(T tuple, int producerIndex) {
    doAddTuple(tuple, producerIndex);
  }

  @Override
  public final T getNextTuple(int consumerIndex) {
    return doGetNextTuple(consumerIndex);
  }

  protected abstract T doGetNextTuple(int consumerIndex);

  protected abstract void doAddTuple(T tuple, int producerIndex);

  @Override
  public void enable() {
    this.enabled = true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public void disable() {
    this.enabled = false;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public int getIndex() {
    return index;
  }
}
