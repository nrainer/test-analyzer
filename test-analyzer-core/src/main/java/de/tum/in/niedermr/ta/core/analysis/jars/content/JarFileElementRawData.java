package de.tum.in.niedermr.ta.core.analysis.jars.content;

public final class JarFileElementRawData {
	private final String m_entryName;
	private final byte[] m_rawData;

	/**
	 * @param entryName
	 *            class path with .class ending
	 */
	public JarFileElementRawData(String entryName, byte[] rawData) {
		this.m_entryName = entryName;
		this.m_rawData = rawData;
	}

	public String getEntryName() {
		return m_entryName;
	}

	public byte[] getRawData() {
		return m_rawData;
	}
}
