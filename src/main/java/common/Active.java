package common;

/**
 * Interface for all entities that can be enabled or disabled.
 *
 * @author palivosd
 */
public interface Active {

  /**
   * Enable the entity.
   */
  void enable();

  /**
   * Check the enabled status of the entity.
   *
   * @return {@code true} if the entity is enabled
   */
  boolean isEnabled();

  /**
   * Disable the entity
   */
  void disable();
}
