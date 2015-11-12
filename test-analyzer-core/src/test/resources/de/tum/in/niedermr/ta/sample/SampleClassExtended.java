package de.tum.in.niedermr.ta.sample;

public class SampleClassExtended {
	private final int m_x = 0;
	private static final int Y = 8889;

	public int getX() {
		return m_x;
	}

	public int getDefinedConstY() {
		return Y;
	}

	public boolean getBooleanConst() {
		return true;
	}

	public int getIntConst1() {
		return 0;
	}

	public int getIntConst2() {
		return 10;
	}

	public short getShortConst1() {
		return 0;
	}

	public short getShortConst2() {
		return 10;
	}

	public long getLongConst1() {
		return 0;
	}

	public long getLongConst2() {
		return 10;
	}

	public double getDoubleConst() {
		return 11.11;
	}

	public char getCharConst1() {
		return 0;
	}

	public char getCharConst2() {
		return 'A';
	}

	public String getStringConst() {
		return "x";
	}

	public long getCalculation() {
		return getLongConst1() + getLongConst2();
	}
}