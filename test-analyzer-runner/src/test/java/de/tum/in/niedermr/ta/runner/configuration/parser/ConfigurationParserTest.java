package de.tum.in.niedermr.ta.runner.configuration.parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.configuration.ConfigurationLoader;
import de.tum.in.niedermr.ta.runner.configuration.ConfigurationLoaderTest;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKey;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKeyNamespace;
import de.tum.in.niedermr.ta.runner.configuration.property.ResultPresentationProperty;

/** Test {@link ConfigurationParser}. */
public class ConfigurationParserTest {

	private static final DynamicConfigurationKey DYNAMIC_PROPERTY_1 = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "data.compress", false);

	@After
	public void after() {
		ConfigurationLoader.setFastFail(false);
	}

	@Test
	public void testParse1() throws IOException, ConfigurationException {
		final Configuration expected = new Configuration();
		expected.getConfigurationVersion().setConfigurationVersionOfProgram();
		expected.getClasspath().setValue("a.jar;b.jar;");
		expected.getExecuteCollectInformation().setValue(true);
		expected.getExecuteMutateAndTest().setValue(false);
		expected.getWorkingFolder().setValue("E:/");
		expected.getDynamicValues().setRawValue(DYNAMIC_PROPERTY_1, Boolean.TRUE.toString());

		Configuration result = new Configuration();

		TestConfigurationParser1 parser = new TestConfigurationParser1(result,
				ConfigurationLoader.toFileLines(expected, false));

		parser.parse();

		ConfigurationLoaderTest.assertConfigurationEquals(expected, result);
	}

	@Test(expected = ConfigurationException.class)
	public void testParse2() throws IOException, ConfigurationException {
		ConfigurationLoader.setFastFail(true);

		Configuration stub = new Configuration();

		List<String> configLines = new LinkedList<>();
		configLines.add(stub.getClasspath().getName() + ConfigurationLoader.KEY_VALUE_SEPARATOR_SET + "a.jar;");
		configLines.add(stub.getClasspath().getName() + ConfigurationLoader.KEY_VALUE_SEPARATOR_SET + "b.jar;");

		TestConfigurationParser1 parser = new TestConfigurationParser1(stub, configLines);
		parser.parse();
	}

	@Test
	public void testParseWithInheritance1() throws IOException, ConfigurationException {
		final Configuration expected = new Configuration();
		expected.getConfigurationVersion().setConfigurationVersionOfProgram();
		expected.getClasspath().setValue("a.jar;b.jar;");
		expected.getNumberOfThreads().setValue(4);
		expected.getResultPresentation().setValue(ResultPresentationProperty.RESULT_PRESENTATION_TEXT);

		Configuration result = new Configuration();

		AbstractConfigurationParser parser = new TestConfigurationParser2(result, expected);
		parser.parse("A");

		ConfigurationLoaderTest.assertConfigurationEquals(expected, result);
	}

	@Test(expected = ConfigurationException.class)
	public void testParseWithInheritance2() throws IOException, ConfigurationException {
		Configuration result = new Configuration();

		AbstractConfigurationParser parser = new AbstractConfigurationParser(result) {
			@Override
			protected List<String> getFileContent(String pathToConfigFile) throws IOException {
				if (pathToConfigFile.equals("A")) {
					List<String> lines = new LinkedList<>();
					lines.add(ConfigurationLoader.KEYWORD_EXTENDS + " B");
					return lines;
				} else {
					throw new FileNotFoundException();
				}
			}
		};

		parser.parse("A");
	}

	private static class TestConfigurationParser1 extends AbstractConfigurationParser {
		private final List<String> m_contentToReturn;

		public TestConfigurationParser1(Configuration result, List<String> contentToReturn) {
			super(result);
			this.m_contentToReturn = contentToReturn;
		}

		@Override
		protected List<String> getFileContent(String pathToConfigFile) throws IOException {
			return m_contentToReturn;
		}

		public void parse() throws IOException, ConfigurationException {
			super.parse("");
		}
	}

	private static class TestConfigurationParser2 extends AbstractConfigurationParser {

		private Configuration m_expected;

		protected TestConfigurationParser2(Configuration result, Configuration expected) {
			super(result);
			m_expected = expected;
		}

		@Override
		protected List<String> getFileContent(String pathToConfigFile) throws IOException {
			List<String> lines = new LinkedList<>();

			if (pathToConfigFile.equals("A")) {
				lines.add(ConfigurationLoader.KEYWORD_EXTENDS + " B");
				lines.add(m_expected.getClasspath().getName() + ConfigurationLoader.KEY_VALUE_SEPARATOR_APPEND
						+ "b.jar;");
			} else if (pathToConfigFile.equals("B")) {
				lines.add(ConfigurationLoader.KEYWORD_EXTENDS + " C");
				lines.add(
						m_expected.getNumberOfThreads().getName() + ConfigurationLoader.KEY_VALUE_SEPARATOR_SET + "4");
				lines.add(m_expected.getResultPresentation().getName() + ConfigurationLoader.KEY_VALUE_SEPARATOR_SET
						+ ResultPresentationProperty.RESULT_PRESENTATION_TEXT);
				lines.add(m_expected.getClasspath().getName() + ConfigurationLoader.KEY_VALUE_SEPARATOR_SET + "a.jar;");
			} else if (pathToConfigFile.equals("C")) {
				lines.add(ConfigurationLoader.KEYWORD_EXTENDS + " D");
			} else if (pathToConfigFile.equals("D")) {
				lines.add(
						m_expected.getNumberOfThreads().getName() + ConfigurationLoader.KEY_VALUE_SEPARATOR_SET + "2");
				lines.add(m_expected.getClasspath().getName() + ConfigurationLoader.KEY_VALUE_SEPARATOR_SET + "c.jar;");
				lines.add(ConfigurationLoader.COMMENT_START_SEQ_1 + "comment");
				lines.add(ConfigurationLoader.COMMENT_START_SEQ_2 + "comment");
			}

			return lines;
		}
	}
}
