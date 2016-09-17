package de.tum.in.niedermr.ta.extensions.analysis.workflows.methodsignature.operation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class SampleClass {

	public String getStringValue() {
		return "";
	}

	public ArrayList<?> getArrayList() {
		return new ArrayList<>();
	}

	public Collection<?> getCollection() {
		return getArrayList();
	}

	public int getInt() {
		return 3;
	}

	public Integer getInteger() {
		return 4;
	}

	public Serializable getSerializable() {
		return "";
	}

	public void voidMethod() {
		// NOP
	}
}
