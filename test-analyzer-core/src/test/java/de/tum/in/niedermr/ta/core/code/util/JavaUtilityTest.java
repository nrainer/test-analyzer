package de.tum.in.niedermr.ta.core.code.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;

public class JavaUtilityTest implements FileSystemConstants {
	private static final String CLASS_NAME_STRING = String.class.getName();
	private static final String CLASS_PATH_STRING_NO_ENDING = "java/lang/String";
	private static final String CLASS_PATH_STRING_WITH_ENDING = CLASS_PATH_STRING_NO_ENDING + ".class";

	@Test
	public void testEnsureClassFileEnding() {
		assertEquals(String.class.getName() + FILE_EXTENSION_CLASS,
				JavaUtility.ensureClassFileEnding(CLASS_NAME_STRING));
		assertEquals(String.class.getName() + FILE_EXTENSION_CLASS,
				JavaUtility.ensureClassFileEnding(CLASS_NAME_STRING + FILE_EXTENSION_CLASS));
	}

	@Test
	public void testRemoveClassFileEnding() {
		assertEquals(CLASS_NAME_STRING, JavaUtility.removeClassFileEnding(CLASS_NAME_STRING));
		assertEquals(CLASS_NAME_STRING, JavaUtility.removeClassFileEnding(CLASS_NAME_STRING + FILE_EXTENSION_CLASS));
	}

	@Test
	public void testClassPathToName() {
		assertEquals(CLASS_NAME_STRING, JavaUtility.toClassName(CLASS_PATH_STRING_WITH_ENDING));
	}

	@Test
	public void testNameToClassPathNoEnding() {
		assertEquals(CLASS_PATH_STRING_NO_ENDING, JavaUtility.toClassPathWithoutEnding(CLASS_NAME_STRING));
		assertEquals(CLASS_PATH_STRING_NO_ENDING, JavaUtility.toClassPathWithoutEnding(String.class));
	}

	@Test
	public void testClassPathAndName() {
		assertEquals(CLASS_NAME_STRING,
				JavaUtility.toClassName(JavaUtility.toClassPathWithEnding((CLASS_NAME_STRING))));
	}

	@Test
	public void testHasSuperClassOtherThanObject() throws IOException {
		ClassNode cn;

		cn = BytecodeUtility.getAcceptedClassNode(Object.class);
		assertFalse(JavaUtility.hasSuperClassOtherThanObject(cn));

		cn = BytecodeUtility.getAcceptedClassNode(String.class);
		assertFalse(JavaUtility.hasSuperClassOtherThanObject(cn));

		cn = BytecodeUtility.getAcceptedClassNode(ArrayList.class);
		assertTrue(JavaUtility.hasSuperClassOtherThanObject(cn));
	}

	@Test
	public void testInheritsClass1() throws ClassNotFoundException, IOException {
		ClassNode cn = BytecodeUtility.getAcceptedClassNode(String.class);
		assertTrue(JavaUtility.inheritsClass(cn, Object.class, getClass().getClassLoader()));
		assertTrue(JavaUtility.inheritsClass(cn, Object.class.getName(), getClass().getClassLoader()));
	}

	@Test
	public void testInheritsClass2() throws ClassNotFoundException, IOException {
		assertTrue(JavaUtility.inheritsClass(Object.class, Object.class));
		assertTrue(JavaUtility.inheritsClass(String.class, Object.class));
		assertTrue(JavaUtility.inheritsClass(LinkedList.class, AbstractList.class));
		assertFalse(JavaUtility.inheritsClass(AbstractList.class, LinkedList.class));
		assertFalse(JavaUtility.inheritsClass(LinkedList.class, ArrayList.class));
	}

	@Test
	public void testCreateInstance() throws ReflectiveOperationException {
		String className = LinkedList.class.getName();

		assertEquals(className,
				JavaUtility.createInstance(className, getClass().getClassLoader()).getClass().getName());
	}
}
