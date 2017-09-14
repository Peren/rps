package se.per.rps;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.googlecode.objectify.ObjectifyService;

import se.per.rps.challenge.Game;

public class OfyHelper implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ObjectifyService.register(Game.class);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
