package se.per.rps;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;

import se.per.rps.challenge.Game;
import se.per.rps.challenge.GameException;
import se.per.rps.challenge.GamePersistence;

public class MockGamePersistence extends GamePersistence {
	public Game game = null;

	public MockGamePersistence(Game game) {
		this.game = game;
	}

	@Override
	public List<Game> listGames(int limit) {
		List<Game> gs = new ArrayList<Game>();
		if (game != null) gs.add(game);
		return gs;
	}

	@Override
	public Game getGame(Long id) throws GameException {
		if (game == null) throw new GameException();
		return game;
	}

	@Override
	public Key<Game> setGame(Game game) {
		this.game = game;
		return null;
	}

	@Override
	public void delGame(Long id) throws GameException {
		if (id != game.id) throw new GameException();
	}
}
