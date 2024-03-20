package common;

/**
 * Interface that marks entities that have a unique ID and numerical index.
 *
 * @author palivosd
 */
public interface Named {

  /**
   * Get the unique ID of the entity.
   *
   * @return The unique ID of the entity.
   */
  String getId();

  /**
   * Get the unique numerical index of the entity.
   *
   * @return The unique numerical index of the entity.
   */
  int getIndex();
  

  
  
}
