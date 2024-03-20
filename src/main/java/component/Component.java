package component;

import common.Active;
import common.Named;

/**
 * Base interface for all stream components such as Sources, Sinks and Operators.
 *
 * @author palivosd
 * @see Active
 * @see Runnable
 * @see Named
 * @see ConnectionsNumber
 */
public interface Component extends Active, Runnable, Named/* , Task*/ {

  /**
   * The input {@link ConnectionsNumber} of this component. Used to enforce invariants during
   * query construction.
   *
   * @return The input {@link ConnectionsNumber} of this component.
   */
  ConnectionsNumber inputsNumber();

  /**
   * The output {@link ConnectionsNumber} of this component. Used to enforce invariants during
   * query construction.
   *
   * @return The output {@link ConnectionsNumber} of this component.
   */
  ConnectionsNumber outputsNumber();

  int getTopologicalOrder();

  double getCost();

  double getSelectivity();

  double getRate();

  default long getOutputQueueSize() {
   return 0;
  }

  /**
   * Update the metrics  (e.g. cost and selectivity) based on the execution statistics of the
   * operator.
   * <br/>
   * <b>WARNING: The variables for the metrics are available only the execution happens with
   * {@link #runFor(int)}
   * !</b> <br/>
   * <b>WARNING: This is not thread safe! It should either be run from the operator thread or
   * from another thread while the operator is stopped. The results are visible to all threads.</b>
   */
  void updateMetrics();

  ComponentType getType();

}
