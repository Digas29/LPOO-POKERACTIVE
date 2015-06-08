package com.utils;

import com.states.ChooseNameState;
import com.states.ChooseRoomState;
import com.states.InitialState;
import com.states.LoadingState;
import com.states.PlayerState;
import com.states.StrategyState;



public class StateMachine {
	
	public enum States{
		PLAYER,
		CHOOSE_ROOM,
		CHOOSE_NAME,
		INITIAL,
		STRATEGY,
		LOADING,
		BOARD
	}
	
	private GameState currentState;
	private InitialState initial = new InitialState();
	private StrategyState strategy = new StrategyState();
	private ChooseNameState chooseName = new ChooseNameState();
	private ChooseRoomState chooseRoom = new ChooseRoomState();
	private LoadingState loading = new LoadingState();
	private PlayerState player = new PlayerState();
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
		case PLAYER:
			currentState = player;
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
		case BOARD:
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
