package common.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum StopJvmUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
  INSTANCE;
  private final Logger LOGGER = LogManager.getLogger();

  @Override
  public void uncaughtException(Thread t, Throwable e) {
    LOGGER.error("{} crashed", t, e);
    LOGGER.error("Details: {}", e);
    LOGGER.error("Shutting down");
    System.exit(1);
  }
}
