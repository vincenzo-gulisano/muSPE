package common.tuple;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * {@link RichTuple} default implementation.
 */
public class BaseRichTuple implements RichTuple, Comparable<BaseRichTuple> {

	protected final long timestamp;
	protected final long stimulus;
	protected final String key;

	public BaseRichTuple(long stimulus, long timestamp, String key) {
		this.timestamp = timestamp;
		this.stimulus = stimulus;
		this.key = key;
	}

	public BaseRichTuple(long timestamp, String key) {
		this(System.currentTimeMillis(), timestamp, key);
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public long getStimulus() {
		return stimulus;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		BaseRichTuple that = (BaseRichTuple) o;

		return new EqualsBuilder().append(timestamp, that.timestamp)
				.append(stimulus, that.stimulus).append(key, that.key)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(timestamp).append(stimulus)
				.append(key).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("timestamp", timestamp)
				.append("stimulus", stimulus).append("key", key)
				.appendSuper(super.toString()).toString();
	}

	@Override
	public int compareTo(BaseRichTuple o) {
		if (this.timestamp == o.getTimestamp()) {
			return 0;
		} else {
			return this.timestamp > o.getTimestamp() ? 1 : -1;
		}
	}

}
