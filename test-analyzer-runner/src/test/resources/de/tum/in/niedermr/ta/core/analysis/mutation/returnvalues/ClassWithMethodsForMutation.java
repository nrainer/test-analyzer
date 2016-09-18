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

	public Integer getInteger() {
		return -100;
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
			getInteger();
		}
	}
}
