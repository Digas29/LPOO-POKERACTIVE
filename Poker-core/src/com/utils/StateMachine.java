package com.utils;

import com.states.InitialState;
import com.states.StrategyState;



public class StateMachine {
	
	public enum States{
		PLAY,
		MAIN_MENU,
		INITIAL,
		STRATEGY
	}
	
	private GameState currentState;
	private InitialState initial = new InitialState();
	private StrategyState strategy = new StrategyState();
	private static StateMachine machine = null;
	
	protected StateMachine(){
		currentState = initial;
	}
	
	public static StateMachine getStateMachine(){
		if(machine == null)
			machine = new StateMachine();
		return machine;
	}
	
	public void switchState(States state){
		switch(state){
		case INITIAL:
			currentState = initial;
			break;
		case STRATEGY:
			currentState = strategy;
			break;
		case PLAY:
			break;
		case MAIN_MENU:
			break;
		default:
			break;
		}
		currentState.create();
	}
	
	public GameState getCurrentState() {
		return currentState;
	}
}
