package de.tum.in.niedermr.ta.core.analysis.result.receiver;

import java.util.List;

/** Rolling file receiver. */
public class MultiFileResultReceiver implements IResultReceiver {

	/** Default number of desired lines per file. */
	public static final int DEFAULT_DESIRED_LINES_PER_FILE = 50000;

	/** First index. */
	public static final int FIRST_INDEX = 1;

	/** File name. */
	private final String m_baseFileName;

	/** Maximum number of lines per file. */
	private final int m_desiredLinesPerFile;

	/** Buffer size in lines. */
	private final int m_bufferSize;

	/** Current file result receiver. */
	private FileResultReceiver m_currentFileResultReceiver;

	/** Number of lines in the current file. */
	private int m_linesInCurrentFile;

	/** Number of created files. */
	private int m_fileCount;

	/** Constructor. */
	public MultiFileResultReceiver(String baseFileName) {
		this(baseFileName, DEFAULT_DESIRED_LINES_PER_FILE);
	}

	/** Constructor. */
	public MultiFileResultReceiver(String baseFileName, int desiredLinesPerFile) {
		this(baseFileName, desiredLinesPerFile, FileResultReceiver.DEFAULT_BUFFER_SIZE);
	}

	/** Constructor. */
	public MultiFileResultReceiver(String baseFileName, int desiredLinesPerFile, int bufferSize) {
		m_baseFileName = baseFileName;
		m_desiredLinesPerFile = desiredLinesPerFile;
		m_bufferSize = bufferSize;
		m_fileCount = FIRST_INDEX;

		createNewFileResultReceiver();
	}

	private synchronized void createNewFileResultReceiver() {
		if (m_currentFileResultReceiver != null) {
			m_currentFileResultReceiver.markResultAsComplete();
			m_fileCount++;
		}

		m_currentFileResultReceiver = new FileResultReceiver(getFileName(m_fileCount), true, m_bufferSize);
		m_linesInCurrentFile = 0;
	}

	/** {@inheritDoc} */
	@Override
	public synchronized void append(String line) {
		m_currentFileResultReceiver.append(line);
		m_linesInCurrentFile++;
	}

	/** {@inheritDoc} */
	@Override
	public synchronized void append(List<String> lines) {
		m_currentFileResultReceiver.append(lines);
		m_linesInCurrentFile += lines.size();
	}

	/** {@inheritDoc} */
	@Override
	public void markResultAsComplete() {
		m_currentFileResultReceiver.markResultAsComplete();
	}

	/** {@inheritDoc} */
	@Override
	public void markResultAsPartiallyComplete() {
		createNewFileResultReceiverIfNeeded();
	}

	protected synchronized void createNewFileResultReceiverIfNeeded() {
		if (m_linesInCurrentFile > m_desiredLinesPerFile) {
			createNewFileResultReceiver();
		}
	}

	/** {@link #m_baseFileName} */
	public String getBaseFileName() {
		return m_baseFileName;
	}

	/**
	 * Get the file with the given index. The first index is
	 * {@value #FIRST_INDEX}.
	 */
	public String getFileName(int index) {
		if (index < FIRST_INDEX || index > m_fileCount) {
			throw new IllegalArgumentException("Invalid index: " + index + " (File count is: " + m_fileCount + ")");
		}

		return getFileName(m_baseFileName, index);
	}

	/**
	 * Get the file with the given index. The index is <b>not zero-based</b>.
	 */
	public static String getFileName(String fileName, int index) {
		int lastIndexOfFileExtensionSeparator = fileName.lastIndexOf(".");

		if (lastIndexOfFileExtensionSeparator < 0) {
			return fileName + index;
		}

		return fileName.substring(0, lastIndexOfFileExtensionSeparator) + index
				+ fileName.substring(lastIndexOfFileExtensionSeparator);
	}

	/** {@link #m_fileCount} */
	public int getFileCount() {
		return m_fileCount;
	}
}
