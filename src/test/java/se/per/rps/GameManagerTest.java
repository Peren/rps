package se.per.rps;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.users.User;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.Key;

import se.per.rps.challenge.CurrentUser;
import se.per.rps.challenge.Game;
import se.per.rps.challenge.GameAction;
import se.per.rps.challenge.GameException;
import se.per.rps.challenge.GameManager;
import se.per.rps.challenge.GamePersistence;
import se.per.rps.challenge.MissingGameException;

public class GameManagerTest {
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

	@Before
	public void setUp() {
		helper.setUp();
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testCreateGame() throws GameException {
		GameManager gm = new GameManager();
		gm.gp = new GamePersistence() {
			public Key<Game> setGame(Game game) {
				return Key.create(Game.class, 1);
			}
		};

		Game game = gm.createGame("challenge", "defender", new CurrentUser(new User("me@mail.com", ""), false));
		assertNotNull(game);
		assertEquals("challenge", game.challenge);
		assertEquals("defender", game.defender.mail);
		assertEquals("me@mail.com", game.attacker.mail);
	}

	@Test
	public void testDoAction() throws MissingGameException, GameException {
		MockGamePersistence gp = new MockGamePersistence(new Game("me@mail.com", "you@mail.com"));
		GameManager gm = new GameManager();
		gm.gp = gp;

		assertEquals(null, gp.game.attacker.action);

		gm.doAction(null, GameAction.Rock, new CurrentUser(new User("me@mail.com", ""), false));

		assertEquals(GameAction.Rock, gp.game.attacker.action);
		assertEquals(0, gp.game.attacker.points.intValue());
		assertEquals(null, gp.game.defender.action);
		assertEquals(0, gp.game.defender.points.intValue());

		gm.doAction(null, GameAction.Paper, new CurrentUser(new User("you@mail.com", ""), false));

		assertEquals(null, gp.game.attacker.action);
		assertEquals(0, gp.game.attacker.points.intValue());
		assertEquals(null, gp.game.defender.action);
		assertEquals(1, gp.game.defender.points.intValue());
	}
}
