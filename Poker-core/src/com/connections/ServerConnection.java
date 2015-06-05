package com.connections;

import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.AllRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.AllUsersEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveUserInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.MatchedRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.ZoneRequestListener;
import com.utils.ConnectionStrategy;

public class ServerConnection extends ConnectionStrategy {

	private RoomData data;
	@Override
	public void connect(String name) {
		final String room = name;
		init();
		warpClient.addConnectionRequestListener(new ConnectionRequestListener(){

			@Override
			public void onConnectDone(ConnectEvent arg0) {
				if(arg0.getResult() == WarpResponseResultCode.SUCCESS){
					warpClient.createTurnRoom(room, "admin", 6, null, 15);
				}
			}

			@Override
			public void onDisconnectDone(ConnectEvent arg0) {

			}

			@Override
			public void onInitUDPDone(byte arg0) {

			}

		});
		warpClient.addZoneRequestListener(new ZoneRequestListener(){

			@Override
			public void onCreateRoomDone(RoomEvent arg0) {
				if(arg0.getResult() == WarpResponseResultCode.SUCCESS){
					data = arg0.getData();
				}

			}

			@Override
			public void onDeleteRoomDone(RoomEvent arg0) {
				if(arg0.getResult() == WarpResponseResultCode.SUCCESS)
					System.out.println("Room deleted!!!");
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
		warpClient.connectWithUserName(name + " desk");
	}

	@Override
	public void disconnect() {
		warpClient.deleteRoom(data.getId());
	}

}
