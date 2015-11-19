public static String getCurrentClasspath(boolean onlyJars) {
	StringBuilder sB = new StringBuilder();

	ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();

	java.net.URL[] urlArray = ((java.net.URLClassLoader) sysClassLoader).getURLs();

	for (java.net.URL url : urlArray) {
		if (onlyJars && !url.getFile().endsWith(".jar")) {
			continue;
		}
		
		sB.append(url.getFile().replace("%20", ""));
		sB.append(";");
	}

	return sB.toString();
}

public static String mergeClasspaths(String paths1, String paths2) {
	java.util.Set<String> set = new java.util.HashSet<>();
	set.addAll(java.util.Arrays.asList(paths1.split(";")));
	set.addAll(java.util.Arrays.asList(paths2.split(";")));

	java.lang.StringBuilder builder = new java.lang.StringBuilder();

	for (String s : set) {
		builder.append(s);
		builder.append(";");
	}

	return builder.toString();
}