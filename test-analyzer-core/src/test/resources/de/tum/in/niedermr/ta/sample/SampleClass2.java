package de.tum.in.niedermr.ta.sample;

public class SampleClass2 {

	private String x;

	public SampleClass2(int x) {
		this(String.valueOf(x));
	}

	public SampleClass2(String x) {
		this.x = x;
	}

	@Override
	public String toString() {
		return x;
	}
}