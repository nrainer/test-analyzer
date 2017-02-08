package de.tum.in.niedermr.ta.runner.configuration.parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import de.tum.in.niedermr.ta.core.common.util.ClasspathUtility;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.configuration.ConfigurationManager;
import de.tum.in.niedermr.ta.runner.configuration.ConfigurationManagerTest;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKey;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKeyNamespace;
import de.tum.in.niedermr.ta.runner.configuration.property.ResultPresentationProperty;

/** Test {@link ConfigurationParser}. */
public class ConfigurationParserTest {

	private static final DynamicConfigurationKey DYNAMIC_PROPERTY_1 = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "data.compress", false);

	/** After. */
	@After
	public void after() {
		// reset
		AbstractConfigurationParser.setFastFail(false);
	}

	/** Test. */
	@Test
	public void testParse1() throws IOException, ConfigurationException {
		final Configuration expected = new Configuration();
		expected.getConfigurationVersion().setConfigurationVersionOfProgram();
		expected.getClasspath().setValue("a.jar" + ClasspathUtility.getClasspathSeparator() + "b.jar"
				+ ClasspathUtility.getClasspathSeparator());
		expected.getOperateFaultTolerant().setValue(true);
		expected.getRemoveTempData().setValue(false);
		expected.getWorkingFolder().setValue("/E:/");
		expected.getDynamicValues().setRawValue(DYNAMIC_PROPERTY_1, Boolean.TRUE.toString());

		TestConfigurationParser1 parser = new TestConfigurationParser1(
				ConfigurationManager.toFileLines(expected, false));

		parser.parse();

		ConfigurationManagerTest.assertConfigurationEquals(expected, parser.getConfiguration());
	}

	/** Test. */
	@Test(expected = ConfigurationException.class)
	public void testParse2() throws IOException, ConfigurationException {
		AbstractConfigurationParser.setFastFail(true);

		Configuration stub = new Configuration();

		List<String> configLines = new LinkedList<>();
		configLines.add(stub.getClasspath().getName() + IConfigurationTokens.KEY_VALUE_SEPARATOR_SET + "a.jar;");
		configLines.add(stub.getClasspath().getName() + IConfigurationTokens.KEY_VALUE_SEPARATOR_SET + "b.jar;");

		TestConfigurationParser1 parser = new TestConfigurationParser1(configLines);
		parser.parse();
	}

	/** Test. */
	@Test
	public void testParseWithInheritance1() throws IOException, ConfigurationException {
		final Configuration expected = new Configuration();
		expected.getConfigurationVersion().setConfigurationVersionOfProgram();
		expected.getClasspath().setValue("a.jar" + ClasspathUtility.getClasspathSeparator() + "b.jar"
				+ ClasspathUtility.getClasspathSeparator());
		expected.getNumberOfThreads().setValue(4);
		expected.getResultPresentation().setValue(ResultPresentationProperty.RESULT_PRESENTATION_TEXT);

		TestConfigurationParser2 parser = new TestConfigurationParser2(expected);
		parser.parse("A");

		ConfigurationManagerTest.assertConfigurationEquals(expected, parser.getConfiguration());
	}

	/** Test. */
	@Test(expected = ConfigurationException.class)
	public void testParseWithInheritance2() throws IOException, ConfigurationException {
		AbstractConfigurationParser<Configuration> parser = new AbstractConfigurationParser<Configuration>() {
			/** {@inheritDoc} */
			@Override
			protected List<String> getFileContent(String pathToConfigFile) throws IOException {
				if (pathToConfigFile.equals("A")) {
					List<String> lines = new LinkedList<>();
					lines.add(IConfigurationTokens.KEYWORD_EXTENDS + " B");
					return lines;
				} else {
					throw new FileNotFoundException();
				}
			}

			/** {@inheritDoc} */
			@Override
			protected Configuration createNewConfiguration() {
				return new Configuration();
			}
		};

		parser.parse("A");
	}

	private static class TestConfigurationParser1 extends AbstractConfigurationParser<Configuration> {
		private final List<String> m_contentToReturn;

		public TestConfigurationParser1(List<String> contentToReturn) {
			m_contentToReturn = contentToReturn;
		}

		/** {@inheritDoc} */
		@Override
		protected List<String> getFileContent(String pathToConfigFile) throws IOException {
			return m_contentToReturn;
		}

		/** {@inheritDoc} */
		@Override
		protected Configuration createNewConfiguration() {
			return new Configuration();
		}

		public void parse() throws IOException, ConfigurationException {
			super.parse("");
		}
	}

	private static class TestConfigurationParser2 extends AbstractConfigurationParser<Configuration> {

		private Configuration m_expected;

		protected TestConfigurationParser2(Configuration expected) {
			m_expected = expected;
		}

		/** {@inheritDoc} */
		@Override
		protected Configuration createNewConfiguration() {
			return new Configuration();
		}

		/** {@inheritDoc} */
		@Override
		protected List<String> getFileContent(String pathToConfigFile) throws IOException {
			List<String> lines = new LinkedList<>();

			if (pathToConfigFile.equals("A")) {
				lines.add(IConfigurationTokens.KEYWORD_EXTENDS + " B");
				lines.add(m_expected.getClasspath().getName() + IConfigurationTokens.KEY_VALUE_SEPARATOR_APPEND
						+ "b.jar;");
			} else if (pathToConfigFile.equals("B")) {
				lines.add(IConfigurationTokens.KEYWORD_EXTENDS + " C");
				lines.add(
						m_expected.getNumberOfThreads().getName() + IConfigurationTokens.KEY_VALUE_SEPARATOR_SET + "4");
				lines.add(m_expected.getResultPresentation().getName() + IConfigurationTokens.KEY_VALUE_SEPARATOR_SET
						+ ResultPresentationProperty.RESULT_PRESENTATION_TEXT);
				lines.add(
						m_expected.getClasspath().getName() + IConfigurationTokens.KEY_VALUE_SEPARATOR_SET + "a.jar;");
			} else if (pathToConfigFile.equals("C")) {
				lines.add(IConfigurationTokens.KEYWORD_EXTENDS + " D");
			} else if (pathToConfigFile.equals("D")) {
				lines.add(
						m_expected.getNumberOfThreads().getName() + IConfigurationTokens.KEY_VALUE_SEPARATOR_SET + "2");
				lines.add(
						m_expected.getClasspath().getName() + IConfigurationTokens.KEY_VALUE_SEPARATOR_SET + "c.jar;");
				lines.add(IConfigurationTokens.COMMENT_START_SEQ_1 + "comment");
				lines.add(IConfigurationTokens.COMMENT_START_SEQ_2 + "comment");
			}

			return lines;
		}
	}
}
