package net.undergroundim.server.networking;

/**
 * 
 * @author Troy
 *
 */
public enum PacketHeaders {
	LOGIN("0ª"),
	LOGIN_FAILED("1ª"),
	CONNECT("2ª"),
	PACKET_ERROR("3ª"),
	PACKET_NEW_KEY("4ª"),
	VERSION_FAIL("5ª"),
	REGISTER_USER("6ª"),
	REGISTER_FAILED("7ª"),
	FRIEND_LIST("8ª"),
	FRIEND_ADD("9ª"),
	FRIEND_DELETE("10ª"),
	FRIEND_REQUESTS("11ª"),
	FRIEND_DISCONNECT("12ª"),
	REQUEST_RESPONSE("13ª"),
	PERSONAL_MESSAGE("14ª"),
	VIEW_PROFILE("15ª"),
	UPDATE_PROFILE("16ª"),
	UPDATE_PASSWORD("17ª"),
	UPDATE_STATUS("18ª"),
	FILE_TRANSFER("19ª"),
	FILE_TRANSFER_RESPONSE("20ª"),
	FILE_START("21ª"),
	FILE_SEND("22ª"),
	FILE_END("23ª"),
	FILE_CANCEL("24ª"),
	SERVER_PERMISSIONS("25ª"),
	NUDGE("26ª"),
	USER_WRITING("27ª"),
	SESSION_CLOSED("28ª"),
	GROUP_ADD("29ª"),
	GROUP_REMOVE("30ª");
	
	private final String header;
	
	private PacketHeaders(String header){
		this.header = header;;
	}

	public String getHeader() {
		return header;
	}

}