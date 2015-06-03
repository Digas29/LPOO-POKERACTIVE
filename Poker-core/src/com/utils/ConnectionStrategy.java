package com.utils;

import com.shephertz.app42.gaming.multiplayer.client.WarpClient;

public abstract class ConnectionStrategy {
	private static String ApiKey = "c577bd1bb63822bd93ce0df0ee9c647c30df46e9804e4d0de1d38995b6f10329";
	private static String SecretKey = "bd49bd80e5c0848bacdf18ccf116c048f2ac4f3a37c47a07fd34231991045566";
	protected WarpClient warpClient;
	
	public abstract void connect(String name);
	public abstract void disconnect();
	public void init(){
		try {
			WarpClient.setRecoveryAllowance(10);
			WarpClient.initialize(ApiKey, SecretKey);
			warpClient = WarpClient.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
