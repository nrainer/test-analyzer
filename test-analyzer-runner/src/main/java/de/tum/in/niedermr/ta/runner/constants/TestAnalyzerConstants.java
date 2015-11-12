package de.tum.in.niedermr.ta.runner.constants;

@Deprecated
public interface TestAnalyzerConstants {
	static final String NAME_WORKSPACE_MASTERARBEIT = "workspace Masterarbeit";
	static final String NAME_TESTING_AREA_MASTERARBEIT = "testarea Masterarbeit";
	static final String NAME_PROJECT_FOLDER_MAGIC_START = "0_MagicStart";
	static final String NAME_PROJECT_FOLDER_CONFIGURATIONS = "Configurations";

	public static final String PROJECT_FOLDER_MAGIC_START_FROM_OTHER_PROJECT = "../" + NAME_PROJECT_FOLDER_MAGIC_START;
	public static final String PROJECT_FOLDER_MAGIC_START_FROM_OTHER_PROJECT_IN_TESTING_AREA = "../../" + NAME_WORKSPACE_MASTERARBEIT + "/"
			+ NAME_PROJECT_FOLDER_MAGIC_START + "/";
	public static final String CONFIGURATION_FOLDER_DEFAULT = "../../" + NAME_TESTING_AREA_MASTERARBEIT + "/" + NAME_PROJECT_FOLDER_CONFIGURATIONS + "/";
}
