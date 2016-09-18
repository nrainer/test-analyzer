package de.tum.in.niedermr.ta.core.code.visitor;

import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import de.tum.in.niedermr.ta.core.code.operation.CodeOperationException;
import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;

public class BytecodeModificationTestUtility {

	/**
	 * Execute a bytecode modification on a class and return the newly created
	 * class.
	 */
	public static Class<?> createAndLoadModifiedClass(Class<?> classToBeModified,
			ICodeModificationOperation modificationOperation)
			throws ClassNotFoundException, CodeOperationException, IOException {
		ClassReader cr = new ClassReader(classToBeModified.getName());
		ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);

		modificationOperation.modify(cr, cw);

		ClassLoader baseClassLoader = BytecodeModificationTestUtility.class.getClassLoader();
		byte[] mutatedClassByteCode = cw.toByteArray();

		DynamicClassLoader dynamicClassLoader = new DynamicClassLoader(baseClassLoader, classToBeModified.getName(),
				mutatedClassByteCode);
		Class<?> loadedClass = dynamicClassLoader.loadClass(classToBeModified.getName());

		if (loadedClass.getClassLoader().getClass() != DynamicClassLoader.class) {
			throw new IllegalStateException("Class loading did not work");
		}

		return loadedClass;
	}

	private static class DynamicClassLoader extends ClassLoader {

		/** Name of the modified class. */
		private String m_modifiedClassName;
		/** Byte code of the modified class after the modification. */
		private byte[] m_modifiedClassByteCode;

		public DynamicClassLoader(ClassLoader parentClassLoader, String modifiedClassName,
				byte[] modifiedClassByteCode) {
			super(parentClassLoader);
			m_modifiedClassName = modifiedClassName;
			m_modifiedClassByteCode = modifiedClassByteCode;
		}

		@Override
		protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
			if (m_modifiedClassName.equals(name)) {
				Class<?> cls = defineClass(name, m_modifiedClassByteCode, 0, m_modifiedClassByteCode.length);

				if (resolve) {
					resolveClass(cls);
				}

				return cls;
			}

			return super.loadClass(name, resolve);
		}
	}
}
