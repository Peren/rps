package se.per.rps.challenge;

import com.google.appengine.api.users.User;

public class CurrentUser {
	private final User user;
	public final boolean isAdmin;

	public CurrentUser(User user, boolean isAdmin) {
		this.user = user;
		this.isAdmin = isAdmin;
	}

	public boolean isPlayer(Player player) {
		return player.mail.equalsIgnoreCase(getEmail());
	}

	public String getEmail() {
		return user.getEmail();
	}
}
