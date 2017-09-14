package se.per.rps.challenge;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class GameManager {
	private static final Logger logger = Logger.getLogger(GameManager.class.getName());

	public GamePersistence gp = new GamePersistence();

	public static Game filterGame(Game game, CurrentUser user) {
		if (user.isAdmin) {
			return game;
		}

		String email = user.user.getEmail();
		if (!email.equals(game.attacker)) {
			game.attacker.action = null;
		}
		if (!email.equals(game.defender)) {
			game.defender.action = null;
		}

		return game;
	}

	private static boolean isPlayer(Player p, String mail) {
		if (p == null) return false;
		if (p.mail == null) return false;
		return p.mail.equals(mail);
	}

	public List<Game> listGames(int limit, CurrentUser user) {
		List<Game> games = null;
		if (user.isAdmin) {
			games = gp.listGames(limit);
		} else {
			games = new ArrayList<Game>();

			List<Game> temp = gp.listGames(limit);
			String mail = user.user.getEmail();
			for (Game game : temp) {
				if (isPlayer(game.attacker, mail) ||
					isPlayer(game.defender, mail)) {
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
		} else if (isPlayer(game.attacker, user.user.getEmail()) ||
			isPlayer(game.defender, user.user.getEmail())) {
			game = filterGame(game, user);
		} else {
			throw new GameException();
		}

		return game;
	}

	public void delGame(Long id, CurrentUser user) throws GameException {
		if (user.isAdmin) {
		} else {
			throw new GameException();
		}

		gp.delGame(id);
	}

	public Game createGame(String challenge, String defender, CurrentUser user) {
		Game game = new Game(user.user.getEmail(), defender);
		game.challenge = challenge;

		logger.info(game.toString());

		gp.setGame(game);
		// TODO: return id instead
//		Key<Game> key = 
//		key.getId();
		return game;
	}

	public void doAction(Long id, GameAction action, CurrentUser user) throws GameException {
		Game game = gp.getGame(id);

		String email = user.user.getEmail();
		if (!email.equals(game.attacker.mail) &&
			!email.equals(game.defender.mail)) {
			throw new GameException();
		}

		if (email.equals(game.attacker.mail)) {
			game.attacker.action = action;
		}
		if (email.equals(game.defender.mail)) {
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

		logger.info(game.toString());

		gp.setGame(game);
	}

	private static boolean isWinner(GameAction a, GameAction b) {
		return a == GameAction.Rock && b == GameAction.Scissors ||
			a == GameAction.Paper && b == GameAction.Rock ||
			a == GameAction.Scissors && b == GameAction.Paper;
	}
}
