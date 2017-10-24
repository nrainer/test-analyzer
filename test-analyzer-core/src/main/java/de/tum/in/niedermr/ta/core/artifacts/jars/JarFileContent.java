package de.tum.in.niedermr.ta.core.artifacts.jars;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;

public class JarFileContent {
	private final JarFile m_jarFile;
	private final List<JarEntry> m_classEntryList;
	private final List<JarEntry> m_resourceEntryList;

	private JarFileContent(JarFile jarFile) throws IOException {
		this.m_jarFile = jarFile;

		this.m_classEntryList = new ArrayList<>();
		this.m_resourceEntryList = new ArrayList<>();
	}

	public static JarFileContent fromJarFile(String pathToJarFile) throws IOException {
		JarFileContent jarFileContent = new JarFileContent(new JarFile(pathToJarFile));
		jarFileContent.initEntries();
		return jarFileContent;
	}

	public List<JarEntry> getClassEntryList() {
		return this.m_classEntryList;
	}

	public List<JarEntry> getResourceEntryList() {
		return m_resourceEntryList;
	}

	public InputStream getInputStream(JarEntry classEntry) throws IOException {
		return m_jarFile.getInputStream(classEntry);
	}

	private void initEntries() throws IOException {
		Enumeration<? extends JarEntry> entries = m_jarFile.entries();

		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();

			if (isClass(entry)) {
				m_classEntryList.add(entry);
			} else {
				m_resourceEntryList.add(entry);
			}
		}
	}

	private boolean isClass(JarEntry jarEntry) {
		return jarEntry.getName().endsWith(FileSystemConstants.FILE_EXTENSION_CLASS);
	}
}