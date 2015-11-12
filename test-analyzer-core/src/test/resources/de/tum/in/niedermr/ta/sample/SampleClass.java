package de.tum.in.niedermr.ta.sample;

public class SampleClass {
	private int m_x;
	private double m_y;
	private Object m_z;

	public SampleClass() {

	}

	public int getX() {
		return m_x;
	}

	public void setX(int x) {
		this.m_x = x;
	}

	public double getY() {
		return m_y;
	}

	public void setY(double y) {
		this.m_y = y;
	}

	public Object getZ() {
		return m_z;
	}

	public void setZ(Object z) {
		this.m_z = z;
	}

	public void setXWithCheck(int x) {
		if (x > 0) {
			this.m_x = x;
		}
	}

	public int get0() {
		return 0;
	}

	public double getXAndY() {
		return m_x + m_y;
	}

	public int[] getArray() {
		return new int[0];
	}

	public void empty() {
		// NOP
	}

	/**
	 * @param param
	 */
	public void empty(int param) {
		// NOP
	}

	public void throwException() {
		throw new Error();
	}

	@Override
	public String toString() {
		return "toString";
	}
}