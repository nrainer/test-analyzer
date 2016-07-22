package de.tum.in.ma.simpleproject.special;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	HierarchyTestsInherited.class, 
	HierarchyTestsSuper.class,
	IgnoredTestClassTests.class,
	Java8Test.class,
	JUnitRulesTest.class,
	MiscellaneousTests.class, 
	SetUpAndTearDownTests.class, 
	// HasFailingTest.class
	})
public class AllSpecialTests
{

}
