package se.per.rps.challenge;

public class Player {
	public String mail;
	public GameAction action;
	public Integer points;

	protected Player() {
		this.points = 0;
	}

	public Player(String mail) {
		this();
		this.mail = mail;
		this.action = null;
	}

	@Override
	public String toString() {
		return "[Player "+ mail +" "+ action +" "+ points +"]";
	}

	public String toJson() {
		return "{"+
				"\"mail\":"+ Game.stringOrNull(mail) +","+
				"\"action\":"+ Game.actionOrNull(action) +","+
				"\"points\":"+ points+
			"}";
	}
}
