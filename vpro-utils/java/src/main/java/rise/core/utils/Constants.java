package rise.core.utils;

public class Constants {

	/** The file containing setup/config */
	public static final String DEFAULT_ROOT_fOLDER = "..\\..\\";

	/** The file containing setup/config */
	public static final String DEFAULT_SHARE_IP_FILE = "share/ipaddresses.properties";

	/** The file containing setup/config */
	public static final String IP_ADDRESSES_FILE_NAME_PROP = "config.ipaddresses";

	/**
	 * the property name to the directory for storing interaction data
	 */
	public static final String USERLOGS_DIR_PROP = "userlogs.dir";

	/**
	 * the property name to the directory containing data that contains data
	 * that persists over time, such as game history, possibly also game db
	 * indexes, and, ideally, also the user model data
	 */
	public static final String PERSISTENT_DIR_PROP = "persistent.dir";
	/**
	 * The property name for the tablet ip to send questions and answers to
	 */
	public static final String TABLET_IP_PROP = "tablet.ipaddress";

	public static final String TABLET_GREENTIME = "tablet.greentime";

	/**
	 * The property name for the touch display server port
	 */
	public static final String SORTINGDISPLAY_IP_PROP = "touchserver.ipaddress";

	public static final String SORTINGDISPLAY_PORT_PROP = "touchserver.port";

	/**
	 * The property name for the NAO
	 */
	public static final String NAO_IP_PROP = "nao.ipaddress";

	public static final String NAO_PORT_PROP = "nao.port";

	/**
	 * The property name for the TECS server (port not used, yet)
	 */
	public static final String TECSSERVER_IP_PROP = "tecsserver.ipaddress";

	public static final String TECSSERVER_PORT_PROP = "tecsserver.port";

	/**
	 * The property name for the TECS Database server (port not used, yet)
	 */
	public static final String TECSDB_SERVER_IP_PROP = "tecsdbserver.ipaddress";

	public static final String TECSDB_SERVER_PORT_PROP = "tecsdbserver.port";

}
