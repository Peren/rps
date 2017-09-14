package se.per.rps;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import se.per.rps.challenge.Game;

public class GameTest {

	@Test
	public void testToJson() {
		Game game = new Game("att", "def");
		game.date = new Date(0);
		game.toJson();

	    Assert.assertEquals(
	    		"{"+
	    			"\"id\":null,"+
	    			"\"challenge\":null,"+
	    			"\"defender\":{\"mail\":\"def\",\"action\":null,\"points\":0},"+
	    			"\"attacker\":{\"mail\":\"att\",\"action\":null,\"points\":0},"+
	    			"\"winner\":null,"+
	    			"\"plays\":[],"+
	    			"\"date\":\"1970-01-01 01:00\""+
	    		"}", game.toJson());
	}

}
