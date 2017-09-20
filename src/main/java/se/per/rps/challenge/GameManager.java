package se.per.rps.challenge;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.googlecode.objectify.Key;

public class GameManager {
	private static final Logger logger = Logger.getLogger(GameManager.class.getName());

	public GamePersistence gp = new GamePersistence();

	public static Game filterGame(Game game, CurrentUser user) {
		if (user.isAdmin) {
			return game;
		}

		if (!user.isPlayer(game.attacker)) {
			game.attacker.action = null;
		}
		if (!user.isPlayer(game.defender)) {
			game.defender.action = null;
		}

		return game;
	}

	public List<Game> listGames(int limit, CurrentUser user) {
		List<Game> games = null;
		if (user.isAdmin) {
			games = gp.listGames(limit);
		} else {
			games = new ArrayList<Game>();

			List<Game> temp = gp.listGames(limit);
			for (Game game : temp) {
				if (user.isPlayer(game.attacker) ||
					user.isPlayer(game.defender)) {
					games.add(filterGame(game, user));
				}
			}
		}

		return games;
	}

	public Game getGame(Long id, CurrentUser user) throws GameException {
		Game game = gp.getGame(id);

		if (game == null) {
			throw new GameException();
		}

		if (user.isAdmin) {
			return game;
		}

		if (user.isPlayer(game.attacker) ||
			user.isPlayer(game.defender)) {
			return filterGame(game, user);
		} else {
			throw new GameException();
		}
	}

	public void delGame(Long id, CurrentUser user) throws GameException {
		if (user.isAdmin) {
		} else {
			throw new GameException();
		}

		gp.delGame(id);
	}

	public Game createGame(String challenge, String defender, CurrentUser user) throws GameException {
		// TODO: make a check that defender is a valid mail address
		if (defender.equalsIgnoreCase(user.getEmail())) {
			throw new GameException();
		}

		Game game = new Game(user.getEmail(), defender);
		game.challenge = challenge;

		logger.info(game.toString());

		// TODO: send email to the defender

		Key<Game> key = gp.setGame(game);

		Long id = key.getId();
		game.id = id;
		logger.info(game +" "+ id);

		// TODO: return id instead of game
		return game;
	}

	public void doAction(Long id, GameAction action, CurrentUser user) throws GameException {
		Game game = gp.getGame(id);

		if (game == null) {
			throw new GameException();
		}

		if (!user.isPlayer(game.attacker) &&
			!user.isPlayer(game.defender)) {
			throw new GameException();
		}

		if (user.isPlayer(game.attacker)) {
			game.attacker.action = action;
		}
		if (user.isPlayer(game.defender)) {
			game.defender.action = action;
		}

		logger.info(game.toString());

		if (game.attacker.action != null &&
			game.defender.action != null) {

			if (isWinner(game.attacker.action, game.defender.action)) {
				game.attacker.points++;
			}
			if (isWinner(game.defender.action, game.attacker.action)) {
				game.defender.points++;
			}

			game.plays.add(game.attacker.action +" vs "+ game.defender.action);

			game.attacker.action = null;
			game.defender.action = null;
		}

		// TODO: check for victory condition and declare a winner after x rounds

		logger.info(game.toString());

		gp.setGame(game);
	}

	private static boolean isWinner(GameAction a, GameAction b) {
		return a == GameAction.Rock && b == GameAction.Scissors ||
			a == GameAction.Paper && b == GameAction.Rock ||
			a == GameAction.Scissors && b == GameAction.Paper;
	}
}
