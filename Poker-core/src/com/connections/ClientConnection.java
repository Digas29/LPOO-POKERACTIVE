package com.connections;

import java.util.ArrayList;

import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.AllRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.AllUsersEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveUserInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.MatchedRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.RoomRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.ZoneRequestListener;
import com.states.ChooseRoomState;
import com.utils.ConnectionStrategy;
import com.utils.StateMachine;
import com.utils.StateMachine.States;

public class ClientConnection extends ConnectionStrategy {
	private static String name;
	private static ArrayList<RoomData> rooms  = new ArrayList<RoomData>();
	private static ArrayList<Integer> nrPlayers  = new ArrayList<Integer>();
	private int totalRooms;
	
	@Override
	public void connect(String name) {
		ClientConnection.name = name;
		totalRooms = 0;
		init();
		getWarpClient().addConnectionRequestListener(new ConnectionRequestListener(){

			@Override
			public void onConnectDone(ConnectEvent arg0) {
				if(arg0.getResult() == WarpResponseResultCode.SUCCESS){
					getWarpClient().getAllRooms();
				}
				else{
					StateMachine.getStateMachine().switchState(States.STRATEGY);
				}
				
			}

			@Override
			public void onDisconnectDone(ConnectEvent arg0) {
				
			}

			@Override
			public void onInitUDPDone(byte arg0) {
				
			}
			
		});
		getWarpClient().addZoneRequestListener(new ZoneRequestListener(){

			@Override
			public void onCreateRoomDone(RoomEvent arg0) {
				if(StateMachine.getStateMachine().getCurrentState().getClass() == ChooseRoomState.class){
					StateMachine.getStateMachine().recreate();
				}
			}

			@Override
			public void onDeleteRoomDone(RoomEvent arg0) {
				if(StateMachine.getStateMachine().getCurrentState().getClass() == ChooseRoomState.class){
					StateMachine.getStateMachine().recreate();
				}
			}

			@Override
			public void onGetAllRoomsDone(AllRoomsEvent arg0) {
				String [] array = arg0.getRoomIds();
				if(array != null){
					totalRooms = array.length;
					for(int i = 0; i < array.length; i++){
						getWarpClient().getLiveRoomInfo(array[i]);
					}
				}
				else{
					StateMachine.getStateMachine().switchState(States.CHOOSE_ROOM);
				}
			}

			@Override
			public void onGetLiveUserInfoDone(LiveUserInfoEvent arg0) {
				
			}

			@Override
			public void onGetMatchedRoomsDone(MatchedRoomsEvent arg0) {
				
			}

			@Override
			public void onGetOnlineUsersDone(AllUsersEvent arg0) {
				
			}

			@Override
			public void onSetCustomUserDataDone(LiveUserInfoEvent arg0) {
				
			}
			
		});
		getWarpClient().addRoomRequestListener(new RoomRequestListener(){

			@Override
			public void onGetLiveRoomInfoDone(LiveRoomInfoEvent arg0) {
				rooms.add(arg0.getData());
				if(arg0.getJoinedUsers() == null){
					nrPlayers.add(0);
				}
				else{
					nrPlayers.add(arg0.getJoinedUsers().length);
				}
				if(rooms.size() == totalRooms){
					StateMachine.getStateMachine().switchState(States.CHOOSE_ROOM);
				}
			}

			@Override
			public void onJoinRoomDone(RoomEvent arg0) {
				if(arg0.getResult() == WarpResponseResultCode.SUCCESS)
					getWarpClient().subscribeRoom(arg0.getData().getId());
				else
					StateMachine.getStateMachine().switchState(States.CHOOSE_ROOM);
				
			}

			@Override
			public void onLeaveRoomDone(RoomEvent arg0) {
				StateMachine.getStateMachine().switchState(States.CHOOSE_ROOM);
			}

			@Override
			public void onLockPropertiesDone(byte arg0) {
				
			}

			@Override
			public void onSetCustomRoomDataDone(LiveRoomInfoEvent arg0) {
				
			}

			@Override
			public void onSubscribeRoomDone(RoomEvent arg0) {
				if(arg0.getResult() == WarpResponseResultCode.SUCCESS)
					StateMachine.getStateMachine().switchState(States.PLAYER);
				else{
					getWarpClient().leaveRoom(arg0.getData().getId());
				}
			}

			@Override
			public void onUnSubscribeRoomDone(RoomEvent arg0) {
				
			}

			@Override
			public void onUnlockPropertiesDone(byte arg0) {
				
			}

			@Override
			public void onUpdatePropertyDone(LiveRoomInfoEvent arg0) {
				
			}
			
		});
		getWarpClient().connectWithUserName(name);
	}

	@Override
	public void disconnect() {
		
	}
	
	public static ArrayList<RoomData> getRooms() {
		return rooms;
	}
	
	public static ArrayList<Integer> getNrPlayers() {
		return nrPlayers;
	}
	
	public static String getName() {
		return name;
	}
}
