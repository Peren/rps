package se.per.rps;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		UserService us = UserServiceFactory.getUserService();
		if (us.isUserLoggedIn()) {
			User user = us.getCurrentUser();

			StringBuilder sb = new StringBuilder();
			sb.append("{");
			sb.append("\"user\":{");
				sb.append("\"name\":\""+ user.getNickname() +"\",");
				sb.append("\"mail\":\""+ user.getEmail() +"\"");
				sb.append("}");
			sb.append("}");

			resp.setContentType("application/json");
			resp.getWriter().write(sb.toString());
		} else {
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
}
