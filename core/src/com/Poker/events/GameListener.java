package com.Poker.events;

import java.util.EventListener;

public interface GameListener extends EventListener{
	
	public void gameEvent(GameEvent e);

}
