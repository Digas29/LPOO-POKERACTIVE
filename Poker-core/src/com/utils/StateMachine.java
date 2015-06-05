package com.utils;

import com.states.ChooseNameState;
import com.states.ChooseRoomState;
import com.states.InitialState;
import com.states.LoadingState;
import com.states.StrategyState;



public class StateMachine {
	
	public enum States{
		PLAY,
		CHOOSE_ROOM,
		CHOOSE_NAME,
		INITIAL,
		STRATEGY,
		LOADING
	}
	
	private GameState currentState;
	private InitialState initial = new InitialState();
	private StrategyState strategy = new StrategyState();
	private ChooseNameState chooseName = new ChooseNameState();
	private ChooseRoomState chooseRoom = new ChooseRoomState();
	private LoadingState loading = new LoadingState();
	private static StateMachine machine = null;
	
	protected StateMachine(){
		currentState = initial;
		currentState.create();
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
		case CHOOSE_ROOM:
			currentState = chooseRoom;
			break;
		case CHOOSE_NAME:
			currentState = chooseName;
			break;
		case LOADING:
			currentState = loading;
			break;
		default:
			break;
		}
		currentState.create();
	}
	public void recreate(){
		currentState.create();
	}
	public GameState getCurrentState() {
		return currentState;
	}
}
