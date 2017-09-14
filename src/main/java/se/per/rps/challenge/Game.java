package se.per.rps.challenge;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Game {
	@Id public Long id;
	public String challenge;
	public Player attacker;
	public Player defender;
	public String winner;
	public List<String> plays;
	@Index public Date date;

	protected Game() {
		this.plays = new ArrayList<String>();
		this.date = new Date();
	}

	public Game(String attacker, String defender) {
		this();
		this.attacker = new Player(attacker);
		this.defender = new Player(defender);
	}

	@Override
	public String toString() {
		return "[Game "+ id +" "+ defender +" "+ attacker +"]";
	}

	public static String stringOrNull(String s) {
		if (s == null) return "null";
		return "\""+ s +"\"";
	}

	public static String actionOrNull(GameAction ga) {
		if (ga == null) return "null";
		return "\""+ ga.name() +"\"";
	}

	public static String dateOrNull(Date d) {
		if (d == null) return "null";
		return "\""+ new SimpleDateFormat().format(d) +"\"";
	}

	public static String listToJson(List<String> list) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		boolean first = true;
		for (String s : list) {
			if (first) { first = false; }
			else sb.append(",");
			sb.append("\""+ s +"\"");
		}
		sb.append("]");
		return sb.toString();
	}

	public String toJson() {
		return "{"+
			"\"id\":"+ id +","+
			"\"challenge\":"+ stringOrNull(challenge) +","+
			"\"defender\":"+ defender.toJson() +","+
			"\"attacker\":"+ attacker.toJson() +","+
			"\"winner\":"+ stringOrNull(winner) +","+
			"\"plays\":"+ listToJson(plays) +","+
			"\"date\":"+ dateOrNull(date) +""+
		"}";
	}
}
