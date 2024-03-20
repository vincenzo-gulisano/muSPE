package common.util.backoff;

public interface Backoff {

  void backoff();

  void relax();

  Backoff newInstance();
}
