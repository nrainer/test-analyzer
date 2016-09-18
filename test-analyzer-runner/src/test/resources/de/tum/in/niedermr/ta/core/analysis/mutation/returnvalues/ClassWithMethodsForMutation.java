package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues;

import java.io.File;

public class ClassWithMethodsForMutation {

	public int getIntValue() {
		return -100;
	}

	public String getStringValue() {
		return "original string value";
	}

	public boolean getTrue() {
		return true;
	}

	public boolean getFalse() {
		return false;
	}

	public long getLong() {
		return 10000L;
	}

	public float getFloat() {
		return 3.14F;
	}

	public double getDouble() {
		return 1.1;
	}

	public char getChar() {
		return '%';
	}

	public Boolean getTrueBooleanWrapper() {
		return Boolean.TRUE;
	}

	public Boolean getFalseBooleanWrapper() {
		return Boolean.FALSE;
	}

	public Byte getByteWrapper() {
		return -100;
	}

	public Short getShortWrapper() {
		return -100;
	}

	public Integer getIntegerWrapper() {
		return -100;
	}

	public Character getCharacterWrapper() {
		return '%';
	}

	public Long getLongWrapper() {
		return -100L;
	}

	public Float getFloatWrapper() {
		return -100.0F;
	}

	public Double getDoubleWrapper() {
		return -100.0;
	}

	public int[] getIntArray() {
		return new int[] { 4 };
	}

	public Object[] getObjectArray() {
		return new Object[] { 4 };
	}

	public File getFile() {
		return new File("./a.txt");
	}

	public StringBuilder getStringBuilder() {
		return new StringBuilder("abc");
	}

	public void voidMethod() {
		for (int i = 0; i < 3; i++) {
			getStringValue();
		}
	}
}
