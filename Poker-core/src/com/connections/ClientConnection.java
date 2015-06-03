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
import com.utils.ConnectionStrategy;

public class ClientConnection extends ConnectionStrategy {
	private ArrayList<RoomData> rooms = new ArrayList<RoomData>();
	@Override
	public void connect(String name) {
		init();
		warpClient.addConnectionRequestListener(new ConnectionRequestListener(){

			@Override
			public void onConnectDone(ConnectEvent arg0) {
				if(arg0.getResult() == WarpResponseResultCode.SUCCESS)
					System.out.println("connected");
				warpClient.getAllRooms();
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
				
			}

			@Override
			public void onDeleteRoomDone(RoomEvent arg0) {
				
			}

			@Override
			public void onGetAllRoomsDone(AllRoomsEvent arg0) {
				String [] array = arg0.getRoomIds();
				for(int i = 0; i < array.length; i++){
					warpClient.getLiveRoomInfo(array[i]);
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
		warpClient.addRoomRequestListener(new RoomRequestListener(){

			@Override
			public void onGetLiveRoomInfoDone(LiveRoomInfoEvent arg0) {
				rooms.add(arg0.getData());
				System.out.println(arg0.getData().getName());
			}

			@Override
			public void onJoinRoomDone(RoomEvent arg0) {
				
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
		
		warpClient.connectWithUserName(name);
	}

	@Override
	public void disconnect() {
		
	}

}
