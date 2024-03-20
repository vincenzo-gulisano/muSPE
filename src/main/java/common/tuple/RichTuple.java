package common.tuple;

public interface RichTuple {

  long getTimestamp();

  String getKey();

  default long getStimulus() {
    throw new UnsupportedOperationException();
  }
}
