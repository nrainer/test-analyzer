package de.tum.in.niedermr.ta.core.analysis.jars.iteration;

import java.io.InputStream;
import java.util.jar.JarEntry;

import org.objectweb.asm.ClassReader;

import de.tum.in.niedermr.ta.core.analysis.jars.content.JarFileContent;
import de.tum.in.niedermr.ta.core.code.iteration.IArtifactIterator;
import de.tum.in.niedermr.ta.core.code.operation.ICodeOperation;

public abstract class AbstractJarIterator<OP extends ICodeOperation> implements IArtifactIterator<OP> {
	private final String m_inputJarPath;

	public AbstractJarIterator(String inputJarPath) {
		this.m_inputJarPath = inputJarPath;
	}

	@Override
	public final void execute(OP jarOperation) throws Exception {
		beforeAll();

		JarFileContent classContainer = JarFileContent.fromJarFile(m_inputJarPath);

		processClassEntryList(jarOperation, classContainer);
		processResourceEntryList(jarOperation, classContainer);

		afterAll();
	}

	private void processClassEntryList(OP jarOperation, JarFileContent classContainer) throws Exception {
		for (JarEntry entry : classContainer.getClassEntryList()) {
			InputStream inStream = classContainer.getInputStream(entry);
			ClassReader cr = new ClassReader(inStream);

			try {
				handleEntry(jarOperation, cr, entry.getName());
			} catch (Throwable t) {
				onExceptionInHandleEntry(t, cr.getClassName());
			} finally {
				inStream.close();
			}
		}
	}

	private void processResourceEntryList(OP jarOperation, JarFileContent classContainer) throws Exception {
		for (JarEntry entry : classContainer.getResourceEntryList()) {
			InputStream inStream = classContainer.getInputStream(entry);

			try {
				handleResource(jarOperation, entry, inStream);
			} catch (Throwable t) {
				onExceptionInHandleResource(t, entry.getName());
			} finally {
				inStream.close();
			}
		}
	}

	protected abstract void beforeAll() throws Exception;

	protected abstract void handleEntry(OP jarOperation, ClassReader cr, String originalClassPath) throws Exception;

	protected abstract void handleResource(OP jarOperation, JarEntry resourceEntry, InputStream inStream)
			throws Exception;

	protected abstract void afterAll() throws Exception;

	protected abstract void onExceptionInHandleEntry(Throwable t, String className) throws Exception;

	protected abstract void onExceptionInHandleResource(Throwable t, String resourcePath) throws Exception;
}
