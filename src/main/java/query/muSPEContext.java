package query;

import component.sink.Sink;

public final class muSPEContext {

  private static boolean flushingEnabled = true;

  private static QueryTerminator queryTerminator;

  public static void init(Query query) {
    queryTerminator = new QueryTerminator(query);

  }

  public static void terminated(Query query) {
    queryTerminator.disable();
  }

  public static void sinkFinished(Sink<?> sink) {
    queryTerminator.sinkFinished(sink);
  }

  public static boolean isFlushingEnabled() {
    return flushingEnabled;
  }

  public static void disableFlushing() {
    flushingEnabled = false;
  }

  private muSPEContext() {
  }

}
