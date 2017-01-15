package de.tum.in.niedermr.ta.extensions.analysis.workflows.methodsignature.operation;

import java.util.List;
import java.util.Optional;

public class SampleClass2 {

	public int m_parameter1;
	public String m_parameter2;
	public double m_parameter3;
	public Object m_parameter4;
	public Optional<String> m_parameter5;
	public String m_parameter6;
	public List<String> m_parameter7;
	public String[] m_array1;
	public int[] m_array2;
	public Object[] m_array3;
	public Double[][] m_array4;

	public SampleClass2(int parameter1, String parameter2, double parameter3) {
		m_parameter1 = parameter1;
		m_parameter2 = parameter2;
		m_parameter3 = parameter3;
	}

	public SampleClass2(Object parameter4, Optional<String> parameter5) {
		m_parameter4 = parameter4;
		m_parameter5 = parameter5;
	}

	public SampleClass2(String name, List<String> parameters) {
		m_parameter6 = name;
		m_parameter7 = parameters;
	}

	public SampleClass2(String[] array1, int[] array2, Object[] array3, Double[][] array4) {
		m_array1 = array1;
		m_array2 = array2;
		m_array3 = array3;
		m_array4 = array4;
	}

	public static SampleClass2 getInstance() {
		return new SampleClass2(1, "2", 3.0);
	}
}
