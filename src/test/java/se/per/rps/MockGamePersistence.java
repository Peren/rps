package se.per.rps;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;

import se.per.rps.challenge.Game;
import se.per.rps.challenge.GamePersistence;
import se.per.rps.challenge.MissingGameException;

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
	public Game getGame(Long id) throws MissingGameException {
		if (game == null) throw new MissingGameException();
		return game;
	}

	@Override
	public Key<Game> setGame(Game game) {
		this.game = game;
		return null;
	}

	@Override
	public void delGame(Long id) throws MissingGameException {
		if (id != game.id) throw new MissingGameException();
	}
}
