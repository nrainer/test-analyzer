package de.tum.in.niedermr.ta.test.integration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.conqat.lib.commons.filesystem.FileSystemUtils;

import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;

/**
 * Integration test.<br/>
 * Nothing special.
 * 
 * @see "configuration file in test data"
 */
public class IntegrationTest9 extends AbstractIntegrationTest {

	/** {@inheritDoc} */
	@Override
	protected void beforeLoadConfiguration() throws IOException, ConfigurationException {
		super.beforeLoadConfiguration();

		File testDataJarFile = new File(getCommonFolderTestData() + JAR_TEST_DATA);
		assertFilesExists(MSG_PATH_TO_TEST_JAR_IS_INCORRECT, testDataJarFile);

		unzipJar(testDataJarFile.getPath(), getSpecificFolderTestWorkingArea() + "code/mutate/classes/");
		unzipJar(testDataJarFile.getPath(), getSpecificFolderTestWorkingArea() + "code/test/classes/");
	}

	/** {@inheritDoc} */
	@Override
	public void executeTestLogic() throws ConfigurationException, IOException {
		assertFilesExists(MSG_TEST_DATA_MISSING, getFileExpectedResultAsText());

		executeTestAnalyzerWithConfiguration();

		assertFilesExists(MSG_OUTPUT_MISSING, getFileOutputResultAsText());
		assertFileContentEqual(MSG_NOT_EQUAL_RESULT, true, getFileExpectedResultAsText(), getFileOutputResultAsText());
	}

	private void unzipJar(String jarFilePath, String targetFolder) throws IOException {
		JarFile jar = new JarFile(jarFilePath);
		Enumeration<JarEntry> jarFileEntries = jar.entries();

		while (jarFileEntries.hasMoreElements()) {
			JarEntry jarEntry = jarFileEntries.nextElement();

			if (jarEntry.isDirectory()) {
				continue;
			}

			File outputFile = new File(targetFolder + FileSystemConstants.PATH_SEPARATOR + jarEntry.getName());

			FileSystemUtils.ensureParentDirectoryExists(outputFile);

			try (InputStream inStream = jar.getInputStream(jarEntry);
					FileOutputStream outStream = new FileOutputStream(outputFile);) {
				while (inStream.available() > 0) {
					outStream.write(inStream.read());
				}
			}
		}

		jar.close();
	}
}
