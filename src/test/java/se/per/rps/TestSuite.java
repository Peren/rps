package se.per.rps;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	HelloAppEngineTest.class,
	GameTest.class,
	GameManagerTest.class,
})

public class TestSuite {
}
