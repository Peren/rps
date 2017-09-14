package se.per.rps.challenge;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class GameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(GameServlet.class.getName());

	private GameManager gm = new GameManager();

	private CurrentUser GetCurrentUser() throws GameServletException {
		UserService us = UserServiceFactory.getUserService();
		if (!us.isUserLoggedIn()) {
			throw new GameServletException(HttpServletResponse.SC_UNAUTHORIZED);
		}

		User user = us.getCurrentUser();
		return new CurrentUser(user, us.isUserAdmin());
	}

	@Override
	protected final void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			CurrentUser user = GetCurrentUser();
			StringBuilder sb = new StringBuilder();
			sb.append("{");

			String id = req.getParameter("id");
			if (id == null) {
				List<Game> games = gm.listGames(99, user);
				sb.append("\"games\":[");
				boolean first = true;
				for (Game game : games) {
					if (first) { first = false; }
					else { sb.append(","); }

					sb.append(game.toJson());
				}
				sb.append("]");
			} else {
				try {
					Game game = gm.getGame(Long.parseLong(id), user);
					sb.append("\"game\":"+ game.toJson());
				} catch (NumberFormatException e) {
					throw new GameServletException(HttpServletResponse.SC_BAD_REQUEST);
				} catch (GameException e) {
					throw new GameServletException(HttpServletResponse.SC_FORBIDDEN);
				}
			}

			sb.append("}");
			resp.setContentType("application/json");
			logger.info(sb.toString());
			resp.getWriter().write(sb.toString());
		} catch(GameServletException e) {
			resp.sendError(e.error);
		}
	}

	@Override
	protected final void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			CurrentUser user = GetCurrentUser();

			String challenge = req.getParameter("challenge");
			String defender = req.getParameter("defender");

			Game game = gm.createGame(challenge, defender, user);

			StringBuilder sb = new StringBuilder();
			sb.append("{");
			sb.append("\"game\":"+ game.toJson());
			sb.append("}");

			resp.setStatus(HttpServletResponse.SC_CREATED);
			resp.setContentType("application/json");
			resp.getWriter().write(sb.toString());
		} catch(GameServletException e) {
			resp.sendError(e.error);
		}
	}

	@Override
	protected final void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			CurrentUser user = GetCurrentUser();

			String id = req.getParameter("id");
			String action = req.getParameter("action");

			GameAction ga = null;
			if (action == null) {
				throw new GameServletException(HttpServletResponse.SC_BAD_REQUEST);
			} else if (action.equalsIgnoreCase("rock")) {
				ga = GameAction.Rock;
			} else if (action.equalsIgnoreCase("paper")) {
				ga = GameAction.Paper;
			} else if (action.equalsIgnoreCase("scissors")) {
				ga = GameAction.Scissors;
			} else {
				throw new GameServletException(HttpServletResponse.SC_BAD_REQUEST);
			}

			try {
				gm.doAction(Long.parseLong(id), ga, user);
			} catch(NumberFormatException e) {
				throw new GameServletException(HttpServletResponse.SC_BAD_REQUEST);
			} catch (GameException e) {
				throw new GameServletException(HttpServletResponse.SC_FORBIDDEN);
			}

			resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
		} catch(GameServletException e) {
			resp.sendError(e.error);
		}
	}

	@Override
	protected final void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			CurrentUser user = GetCurrentUser();

			String id = req.getParameter("id");

			logger.info("Delete '"+ id +"'");

			try {
				gm.delGame(Long.parseLong(id), user);
			} catch (NumberFormatException e) {
				throw new GameServletException(HttpServletResponse.SC_BAD_REQUEST);
			} catch (GameException e) {
				throw new GameServletException(HttpServletResponse.SC_FORBIDDEN);
			}

			resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
		} catch(GameServletException e) {
			resp.sendError(e.error);
		}
	}
}
