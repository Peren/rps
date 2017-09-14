package se.per.rps;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.appengine.api.users.User;

import se.per.rps.challenge.CurrentUser;
import se.per.rps.challenge.Game;
import se.per.rps.challenge.GameAction;
import se.per.rps.challenge.GameException;
import se.per.rps.challenge.GameManager;
import se.per.rps.challenge.GamePersistence;

public class GameManagerTest {

	@Test
	public void testCreateGame() {
		GameManager gm = new GameManager();
		gm.gp = new GamePersistence() {
			public com.googlecode.objectify.Key<Game> setGame(Game game) {
				return null;
			}
		};

		Game game = gm.createGame("challenge", "defender", new CurrentUser(new User("me@mail.com", ""), false));
		assertNotNull(game);
		
	}

	@Test
	public void testDoAction() throws GameException {
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
