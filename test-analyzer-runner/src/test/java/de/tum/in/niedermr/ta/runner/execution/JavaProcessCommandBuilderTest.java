package de.tum.in.niedermr.ta.runner.execution;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.common.util.StringUtility;

/** Test {@link JavaProcessCommandBuilder}. */
public class JavaProcessCommandBuilderTest {

	/** Test. */
	@Test
	public void testCreateCommand() {
		JavaProcessCommandBuilder builder = new JavaProcessCommandBuilder();
		builder.setMainClassName(JavaProcessCommandBuilder.class.getName());
		builder.setClassPath("main.jar");
		builder.addJavaArgument("-Xms256m");
		builder.addJavaArgument("-Xmx512m");
		builder.addProgramArguments(Arrays.asList("x", "5"));
		builder.addProgramArgument("MK");

		String actualCommand = StringUtility.join(builder.complete(), " ");
		String expectedCommand = "java -Xms256m -Xmx512m -classpath main.jar de.tum.in.niedermr.ta.runner.execution.JavaProcessCommandBuilder \"x\" \"5\" \"MK\"";
		assertEquals(expectedCommand, actualCommand);
	}

	/** Test. */
	@Test(expected = IllegalStateException.class)
	public void testNoMainClass() {
		JavaProcessCommandBuilder builder = new JavaProcessCommandBuilder();
		builder.setClassPath("main.jar");
		builder.complete();
	}

	/** Test. */
	@Test(expected = IllegalStateException.class)
	public void testNoClasspath() {
		JavaProcessCommandBuilder builder = new JavaProcessCommandBuilder();
		builder.setMainClassName(JavaProcessCommandBuilder.class.getName());
		builder.complete();
	}
}
