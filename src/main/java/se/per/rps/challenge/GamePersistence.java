package se.per.rps.challenge;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

public class GamePersistence {
	public List<Game> listGames(int limit) {
		List<Game> games = ObjectifyService.ofy()
			.load()
			.type(Game.class)
			.limit(limit)
			.list();

		return games;
	}

	public Game getGame(Long id) throws MissingGameException {
		Game game = ObjectifyService.ofy()
			.load()
			.type(Game.class)
			.id(id)
			.now();

		if (game == null) {
			throw new MissingGameException();
		}

		return game;
	}

	public Key<Game> setGame(Game game) {
		return ObjectifyService.ofy()
			.save()
			.entity(game)
			.now();
	}

	public void delGame(Long id) throws MissingGameException {
		ObjectifyService.ofy()
			.delete()
			.type(Game.class)
			.id(id)
			.now();
	}
}
