package component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stream.Stream;

public abstract class AbstractComponent<IN, OUT> implements Component {

  private static final Logger LOG = LogManager.getLogger();
  protected final ComponentState<IN, OUT> state;

  // Exponential moving average alpha parameter
  // for cost and selectivity moving averages
  // The ALPHA we want to use for EMAs
  private static final double TARGET_ALPHA = 0.3;
  // The update period that the target alpha would be applied to, in millis
  private static final long TARGET_UPDATE_PERIOD = 1000;
  private static final long MILLIS_TO_NANOS = 1000000;
  // The actual alpha that we use, changing depending on the actual update period length
  private volatile double alpha = 0.2;
  private volatile long tuplesWritten;
  private volatile long tuplesRead;
  private volatile long processingTimeNanos;
  private volatile long lastUpdateTime = System.currentTimeMillis();
  private volatile double selectivity = 1;

  private volatile double cost = 1;
  private volatile double rate = 0;

  private boolean flushed;

  public AbstractComponent(String id, ComponentType type) {
    this.state = new ComponentState<>(id, type);
  }

  @Override
  public final void run() {
    if (isEnabled()) {
      process();
    }
  }

  protected abstract void process();

  protected final void increaseTuplesRead() {
    tuplesRead++;
  }

  protected final void increaseTuplesWritten() {
    tuplesWritten++;
  }

  /**
   * Update the cost and selectivity based on the tuples processed and the time it took. <br>
   * <b>WARNING: The variables for the metrics are available only the execution happens with {@link
   * #runFor(int)} !</b> <br>
   * <b>WARNING: This is not thread safe! It should either be run from the operator thread or from
   * another thread while the operator is stopped. The results are visible to all threads.</b>
   */
  @Override
  public final void updateMetrics() {
    updateRateAndAlpha();
    updateCostAndSelectivity();
  }

  private void updateCostAndSelectivity() {
    if (tuplesRead == 0 || processingTimeNanos == 0) {
      return;
    }
    final double currentSelectivity = tuplesWritten / (double) tuplesRead;
    final double currentCost = processingTimeNanos / (double) tuplesRead;
    this.selectivity = movingAverage(currentSelectivity, selectivity);
    this.cost = movingAverage(currentCost, cost);
    this.tuplesRead = this.tuplesWritten = this.processingTimeNanos = 0;
  }

  private void updateRateAndAlpha() {
    final long currentTime = System.currentTimeMillis();
    final long updatePeriod = currentTime - lastUpdateTime;
    if (updatePeriod == 0) {
      return;
    }
    // Update alpha value
    this.alpha = Math.min(TARGET_ALPHA, TARGET_ALPHA * updatePeriod / TARGET_UPDATE_PERIOD);
    final double currentRate = tuplesRead / (double) (MILLIS_TO_NANOS * updatePeriod);
    this.rate = movingAverage(currentRate, rate);
    this.lastUpdateTime = currentTime;
  }

  private double movingAverage(double newValue, double oldValue) {
    return (alpha * newValue) + ((1 - alpha) * oldValue);
  }

  @Override
  public final double getSelectivity() {
    return selectivity;
  }

  @Override
  public final double getCost() {
    return cost;
  }

  @Override
  public final double getRate() {
    return rate;
  }

  public ComponentType getType() {
    return state.getType();
  }

  public final ConnectionsNumber inputsNumber() {
    return state.inputsNumber();
  }

  public final ConnectionsNumber outputsNumber() {
    return state.outputsNumber();
  }

  public void enable() {
    state.enable();
  }

  public void disable() {
    state.disable();
  }

  public boolean isEnabled() {
    return state.isEnabled();
  }

  public String getId() {
    return state.getId();
  }


  public int getIndex() {
    return state.getIndex();
  }

  protected <T> boolean isStreamFinished(T tuple, Stream<T> stream) {
    return (tuple == null) && (stream.isFlushed());
  }

  protected void flush() {
    LOG.info("{} being flushed", getId());
    flushAction();
    LOG.info("{} finished processing", getId());
    this.flushed = true;
  }

  protected abstract void flushAction();


  protected boolean isFlushed() {
    return flushed;
  }

  @Override
  public String toString() {
    return getId();
  }
}
