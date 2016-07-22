package de.tum.in.ma.simpleproject.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CalculationDefaultTests.class, CalculationStringTests.class, CalculationTrivialTests.class })
public class AllCalculationTests
{

}
