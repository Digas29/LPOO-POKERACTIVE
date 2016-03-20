package com.connections;


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
import com.utils.ConnectionStrategy;
import com.utils.StateMachine;
import com.utils.StateMachine.States;

public class ServerConnection extends ConnectionStrategy {

	private RoomData data;
	
	@Override
	public void connect(String name) {
		final String room = name;
		init();
		getWarpClient().addConnectionRequestListener(new ConnectionRequestListener(){

			@Override
			public void onConnectDone(ConnectEvent arg0) {
				if(arg0.getResult() == WarpResponseResultCode.SUCCESS){
					getWarpClient().createRoom(room, "admin", 7, null);
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
				if(arg0.getResult() == WarpResponseResultCode.SUCCESS){
					data = arg0.getData();
					getWarpClient().joinRoom(data.getId());
				}

			}

			@Override
			public void onDeleteRoomDone(RoomEvent arg0) {
				if(arg0.getResult() == WarpResponseResultCode.SUCCESS)
					StateMachine.getStateMachine().switchState(States.STRATEGY);
			}

			@Override
			public void onGetAllRoomsDone(AllRoomsEvent arg0) {

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
				
			}

			@Override
			public void onJoinRoomDone(RoomEvent arg0) {
				if(arg0.getResult() == WarpResponseResultCode.SUCCESS){
					getWarpClient().subscribeRoom(arg0.getData().getId());
				}
				else{
					getWarpClient().deleteRoom(arg0.getData().getId());
				}
			}

			@Override
			public void onLeaveRoomDone(RoomEvent arg0) {
				
			}

			@Override
			public void onLockPropertiesDone(byte arg0) {
				
			}

			@Override
			public void onSetCustomRoomDataDone(LiveRoomInfoEvent arg0) {
				
			}

			@Override
			public void onSubscribeRoomDone(RoomEvent arg0) {
				if(arg0.getResult() == WarpResponseResultCode.SUCCESS){
					StateMachine.getStateMachine().switchState(States.BOARD);
				}
				else{
					getWarpClient().subscribeRoom(arg0.getData().getId());
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

		getWarpClient().connectWithUserName(name + " server");
	}

	@Override
	public void disconnect() {
		getWarpClient().deleteRoom(data.getId());
	}

}
