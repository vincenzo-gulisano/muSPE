package common.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * General utilities
 */
public class Util {

  private static final Logger LOGGER = LogManager.getLogger();

  private Util() {

  }

  /**
   * Sleep for the specified amount of time. This function catches any {@link
   * InterruptedException}s, logs the important part of the stack trace and sets {@code
   * Thread.currentThread().interrupt()}. <br/>
   *
   * <emph>Note: Be careful when using this function because it will reset the interrupted
   * status of the thread. In many cases this is a good default. However, in case you want to clear
   * that, please use {@link Thread#sleep(long)} and handle any {@link InterruptedException}s
   * manually* </emph>
   *
   * @param millis The time to sleep, in milliseconds.
   */
  public static void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      LOGGER.debug("Sleep interrupted: {}", e.getStackTrace()[2]);
      Thread.currentThread().interrupt();
    }
  }

  public static <T extends Serializable> T deepCopy(T in) throws Exception
  {
    //Serialization of object
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bos);
    out.writeObject(in);

    //De-serialization of object
    ByteArrayInputStream bis = new   ByteArrayInputStream(bos.toByteArray());
    ObjectInputStream copy = new ObjectInputStream(bis);
    return (T) copy.readObject();

  }
}
