package de.tum.in.niedermr.ta.core.common.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;

/** Utility to read and write from text files. */
public class TextFileUtility {

	public static void writeToFile(String fileName, Collection<String> lines) throws IOException {
		writeToFileInternal(fileName, false, lines);
	}

	public static void appendToFile(String fileName, Collection<String> lines) throws IOException {
		writeToFileInternal(fileName, true, lines);
	}

	private static void writeToFileInternal(String fileName, boolean append, Collection<String> lines)
			throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, append));

		try {
			for (String l : lines) {
				writer.write(l + CommonConstants.NEW_LINE);
			}
		} finally {
			writer.close();
		}
	}

	/** Read a list of lines from a file. */
	public static List<String> readFromFile(String fileName) throws IOException {
		List<String> result = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line;

			while ((line = reader.readLine()) != null) {
				result.add(line);
			}
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException(new File(fileName).getAbsolutePath());
		}

		return result;
	}

	public static void mergeFiles(String resultFile, String inputFileWithIndexParam, int numberOfInputFiles)
			throws IOException {
		String[] inputFiles = new String[numberOfInputFiles];

		for (int i = 0; i < numberOfInputFiles; i++) {
			inputFiles[i] = String.format(inputFileWithIndexParam, i);
		}

		mergeFiles(resultFile, inputFiles);
	}

	public static void mergeFiles(String resultFile, String... inputFiles) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, false));
		BufferedReader reader = null;

		try {
			for (String inFile : inputFiles) {
				File file = new File(inFile);

				if (!file.exists()) {
					continue;
				}

				reader = new BufferedReader(new FileReader(file));

				String readLine;

				while ((readLine = reader.readLine()) != null) {
					writer.write(readLine + CommonConstants.NEW_LINE);
				}

				reader.close();
			}
		} finally {
			writer.close();

			if (reader != null) {
				reader.close();
			}
		}
	}
}
