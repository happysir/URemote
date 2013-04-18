package org.es.uremote.utils;

/**
 * Class that hosts the intent keys of the application.
 * @author Cyril
 *
 */
public class IntentKeys {
	// Intent constants
	/** The request type key. */
	public static final String REQUEST_TYPE = "REQUEST_TYPE";

	/** The request code key. */
	public static final String REQUEST_CODE = "REQUEST_CODE";

	/** Device Address key. */
	public static String EXTRA_DEVICE_ADDRESS	= "device_address";
	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME		= "device_name";
	public static final String TOAST		= "toast";


	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE	= 1;
	public static final int MESSAGE_READ			= 2;
	public static final int MESSAGE_WRITE			= 3;
	public static final int MESSAGE_DEVICE_NAME		= 4;
	public static final int MESSAGE_TOAST			= 5;

	// Constantes pour les Broadcasts
	//	public static final String BROADCAST_CHECK_SERVER_STATE = "server_state";
	//	public static final String BROADCAST_SERVER_IS_ON = "server_is_on";
	//	public static final String BROADCAST_SERVER_IS_OFF = "server_is_off";
	//
	//	public static final String DIR_PATH		= "directory_path";
	//	public static final String DIR_CONTENT	= "directory_content";
}