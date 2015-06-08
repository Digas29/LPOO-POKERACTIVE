package com.Poker.events;

import java.awt.AWTEvent;

@SuppressWarnings("serial")
public class GameEvent extends AWTEvent {


	public GameEvent(Object message, int id) {
		super(message, id);
	}

}
