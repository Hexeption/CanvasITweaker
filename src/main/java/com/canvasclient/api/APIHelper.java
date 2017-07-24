package com.canvasclient.api;

import com.canvasclient.Canvas;
import com.canvasclient.notification.Notification;
import com.canvasclient.notification.NotificationManager;
import com.canvasclient.maps.MapHandler;
import com.canvasclient.friend.Message;
import com.canvasclient.friend.party.Party;
import com.canvasclient.friend.party.Party.PartyMessage;
import com.canvasclient.render.utilities.TextureImage;
import com.canvasclient.resource.IOHelper;
import com.canvasclient.resource.ResourceHelper;
import com.canvasclient.ui.interactable.slotlist.part.*;
import com.canvasclient.users.User;
import com.canvasclient.utilities.ActivityType;
import com.canvasclient.utilities.IMinecraft;
import jaco.mp3.player.MP3Player;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
* Essentially the user is assigned a random client ID when they create an account and that is used for all API functions
* IGN = In game name
*/
public class APIHelper implements IMinecraft {
	
	public static final String API = "http://api.canvasclient.com/";
	public static final String API_STATIC = "http://static.canvasclient.com";

	public static final String SERVER_IMAGE = API + "/multiplayer/server-image/%s";
	
	public static final String LOGIN = API + "/login/%s/%s";
	public static final String CREATE_ACCOUNT = API + "/create-account/?1234567=%s&2234567=%s&3234567=%s&f_name=%s&info=%s";
	
	public static final String CREATE_PASSPORT = API + "/create-passport/index.php?premium_ign=%s&f_name=%s&email=%s&passwordmd5=%s";
	
	public static final String CLIENT_DETAILS = API + "/details/%s/";
	public static final String CLIENT_SERVERS = API + "/multiplayer/list/%s";
	public static final String CLIENT_FRIENDS = API + "/friends/view/?client_id=%s";
	public static final String CLIENT_MESSAGES = API + "/messages/view/?client_id=%s";
	public static final String CLIENT_PLAYLISTS = API + "/music/playlists.php?client_id=%s&action=list";

	public static final String PLAYLIST_ADD = API + "/music/playlists.php?client_id=%s&action=create&name=%s";
	public static final String PLAYLIST_REMOVE = API + "/music/playlists.php?client_id=%s&action=delete&playlist=%s";
	public static final String PLAYLIST_RENAME = API + "/music/playlists.php?client_id=%s&action=rename&playlist=%s&newname=%s";
	public static final String PLAYLIST_SONGS = API + "/music/playlists.php?client_id=%s&action=view&playlist=%s";
	public static final String PLAYLIST_ADD_SONG = API + "/music/playlists.php?client_id=%s&action=addsong&playlist=%s&songid=%s";
	public static final String PLAYLIST_REMOVE_SONG = API + "/music/playlists.php?client_id=%s&action=deletesong&playlist=%s&songid=%s";

	public static final String MUSIC_GET_STARRED = API + "/music/star.php?client_id=%s&action=view";
	public static final String MUSIC_ADD_STARRED = API + "/music/star.php?client_id=%s&action=star&song=%s";
	public static final String MUSIC_REMOVE_STARRED = API + "/music/star.php?client_id=%s&action=unstar&song=%s";
	public static final String MUSIC_PLAY = API + "/music/get_stream.php?id=%s";
	public static final String MUSIC_HOME_IMAGE = API + "/music/home.png";
	public static final String MUSIC_CHARTS = API + "/music/charts.php?client_id=%s";
	public static final String MUSIC_SEARCH = API + "/music/search.php?client_id=%s&t=%s";
	
	public static final String PARTIES_CHATS = API + "/parties/my_chats/?client_id=%s";
	public static final String PARTIES_VIEW = API + "/parties/view_chat/?client_id=%s&chat_id=%s";
	public static final String PARTIES_NEW_MESSAGE = API + "/parties/view_chat/new_message/?client_id=%s&chat_id=%s&message=%s";
	
	public static final String ADD_SERVER = API + "/add-multiplayerserver/?client_id=%s&server_ip=%s&server_port=%s";
	public static final String DELETE_SERVER = API + "/del-multiplayerserver/?client_id=%s&ip=%s";
	public static final String MOVE_SERVER = API + "/reorder_multiplayer/?client_id=%s&ip=%s&method=%s";

	public static final String ADD_FRIEND = API + "/friends/add/?client_id=%s&username=%s&message=%s";
	public static final String ACCEPT_FRIEND = API + "/friends/accept/?client_id=%s&username=%s";
	public static final String VIEW_FRIEND = API + "/profiles/index.php?client_id=%s&friend_name=%s";

	public static final String NEW_MESSAGE = API + "/profiles/chat/new_message.php?client_id=%s&friend_name=%s&new_message=%s";
	public static final String VIEW_MESSAGES = API + "/profiles/chat/view_chat.php?client_id=%s&friend_name=%s";
	
	public static final String INVITE = API + "/invite/?client_id=%s&username=%s&ip=%s&message=%s";
	
	public static final String STATUS = API + "/my-status/?client_id=%s";
	public static final String HIDE_STATUS = API + "/hide-status/?client_id=%s";
	
	public static final String SERVER_CAN_CHEAT = API + "/session/server.php?clientid=%s&server=%s";
	
	public static final String LATEST_ACTIVITY = API + "/latest-activity/?client_id=%s";
	public static final String LATEST_ACTIVITY_DELETE = API + "/latest-activity/delete-activity.php?client_id=%s&id=%s";
	public static final String UPDATES = API + "/updates/?client_id=%s&status=%s&ip=%s";
	
	public static final String PODS = "https://pods.canvasclient.com/";
	public static final String FORUMS = "https://forums.canvasclient.com";
	public static final String HELP = "https://help.canvasclient.com/";
	public static final String ACCOUNT = "https://canvasclient.com/account";
	public static final String ACCOUNT_ABOUT = "https://canvasclient.com/about/my";
	public static final String TERMS_OF_SERVICE = "https://canvasclient.com/about/tos";
	public static final String MAP_STORE = "https://store.canvasclient.com/maps/";
	public static final String PREMIUM_AD = "https://canvasclient.com/buy-premium/?upgrade";
	public static final String WIDGETS = "https://canvasclient.com/";
	public static final String FRIEND = "https://canvasclient.com/";
	
	public static final String SUPPORT_EMAIL = "support@canvasclient.com";
	public static final String ADDONS_MANAGE = "https://canvasclient.com/account/addons";
	public static final String ORDER = "http://store.canvasclient.com/order/";
	public static final String ADDONS_MANAGE_LOGIN = "https://canvasclient.com/account/addons/?client_id=%s";
	public static final String PROFILE = "https://canvasclient.com/account/friends/profile/%s";
	public static final String PARTY = "http://canvasclient.com";

	public static final String PLAYER_HEAD = API + "/heads/?u=%s&s=%s";
	public static final String PLAYER_HEAD_ROUNDED = API + "/heads/rounded.php?u=%s&bg=%s";
	public static final String PLAYER_MODEL = API + "/singleplayer-skin/?username=%s";
	public static final String PLAYER_CAPE = API_STATIC + "/capes/%s.png";
	public static final String PLAYER_EXTRAS = API + "/extras/?names=all";
	
	public static final String SET_COLOR_SCHEME = API + "/create-passport/colour.php?client_id=%s&colour_scheme=%s";
	
	public static final String FRIEND_COVER_IMAGE = API_STATIC + "/ingame_cover/?u=%s";
	
	public static final String NOTIFICATION_SOUND = API + "/assets/notification.mp3";
	
	public static final String CUSTOMISE = "https://canvasclient.com/me/";

	public static String hideStatus;

	public static String[] login(String username, String password) {
		String encrypt = encrypt(password);
		if (encrypt.equalsIgnoreCase(password)) {
			return null;
		}
		return loginSecure(username, encrypt);
	}

	private static String[] loginSecure(String u, String e) {
		try {
			URL apiURL = new URL(String.format(LOGIN, u, e));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
	
			JSONObject jsonObject = new JSONObject(line);
			
			String clientID = jsonObject.getString("client_id");
			String message = jsonObject.getString("message");
			return new String[] {clientID, message};
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}

	@Deprecated
	public static String[] createAccount(String a, String u, String e, String p, boolean i) {
		a = a.replace(" ", "%20");
		u = u.replace(" ", "%20");
		p = encrypt(p);
		try {
			URL apiURL = new URL(String.format(CREATE_ACCOUNT, a, e, p, u, String.valueOf(i)));
	
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
			if (line.length() == 0) {
				return null;
			}
	
			JSONObject jsonObject = new JSONObject(line);
			
			String clientID = String.valueOf(jsonObject.get("client_id"));
			String message = jsonObject.getString("message");
			return new String[] {clientID, message};
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}
	
	//Creates account
	public static String[] createPassport(String ign, String name, String email, String password) {
		ign = ign.replace(" ", "%20");
		name = name.replace(" ", "%20");
		email = email.replace(" ", "%20");
		password = encrypt(password);
		
		try {
			URL apiURL = new URL(String.format(CREATE_PASSPORT, ign, name, email, password));
	
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
			if (line.length() == 0) {
				return null;
			}
	
			JSONObject jsonObject = new JSONObject(line);
			
			String clientID = String.valueOf(jsonObject.get("client_id"));
			String message = jsonObject.getString("message");
			return new String[] {clientID, message};
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}

	static String encrypt(String s) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(s.getBytes());
	
			byte byteData[] = md.digest();
	
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
	
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				String hex = Integer.toHexString(0xff & byteData[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			
			return hexString.toString();
		} catch (Exception e) {
			return s;
		}
	}

	//Gets data from API about user from clientID
	public static String[] getClientDetails(String clientID) {
		try {
			URL apiURL = new URL(String.format(CLIENT_DETAILS, clientID));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
	
			JSONObject obj = new JSONObject(line);
			if (obj.length() == 11) {
				String canvasName = isString(obj.get("name"));
				String minecraftName = isString(obj.get("ingame_name"));
				String premium = isString(obj.get("premium"));
				String email = isString(obj.get("email"));
				String status = isString(obj.get("status"));
				String nametag = isString(obj.get("nametag")).replace("&", "\u00a7");
				String checkTime = isString(obj.get("check_time"));
				String colorScheme = isString(obj.get("colour_scheme"));
				String latestVersion = isString(obj.get("latest_version"));
				String latestVersionURL = isString(obj.get("latest_version_url"));
				
				boolean canCheat = obj.getBoolean("cheats_enabled");
				String cd = Boolean.toString(canCheat);
				
				return new String[] {clientID, canvasName, minecraftName, premium, email, status, nametag, checkTime, colorScheme, latestVersion, latestVersionURL, cd};
			} else {
				String message = obj.getString("message");
				return new String[] {message};
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}

	//Gets the user's list of servers from their client ID
	public static List<Server> getServers(String clientID) {
		List<Server> list = new ArrayList<Server>();
		try {
			URL apiURL = new URL(String.format(CLIENT_SERVERS, clientID));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
	
			JSONArray jsonArray = new JSONArray(line);
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String ip = null, port = null, serverName = null, serverDescription = null, serverWebsite = null, serverWebstore = null;
				int maxPlayers = -1;
				boolean extraDetails;
				
				ip = jsonObject.getString("ip");
				extraDetails = Boolean.parseBoolean(jsonObject.getString("extra_details"));
				if (extraDetails) {
					serverName = jsonObject.getString("server_name");
					serverDescription = jsonObject.getString("server_description");
					serverWebsite = jsonObject.getString("server_website");
					serverWebstore = jsonObject.getString("server_webstore");
				}
				list.add(new Server(ip, serverName, serverDescription, serverWebsite, serverWebstore));
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return list;
	}

	//Adds a server to the users list of servers
	public static String addServer(String i, String p) {
		i = i.replace(" ", "%20");
		p = p.replace(" ", "%20");
		if (Canvas.getCanvas().getUserControl() == null) {
			return null;
		}
		try {
			URL apiURL = new URL(String.format(ADD_SERVER, Canvas.getCanvas().getUserControl().clientID, i, p));
	
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
			if (line.length() == 0) {
				return null;
			}
	
			JSONObject jsonObject = new JSONObject(line);
			
			String response = String.valueOf(jsonObject.get("response"));
			return response;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}

	//Deletes a server to the users list of servers
	public static String deleteServer(String i) {
		i = i.replace(" ", "%20");
		if (Canvas.getCanvas().getUserControl() == null) {
			return null;
		}
		try {
			URL apiURL = new URL(String.format(DELETE_SERVER, Canvas.getCanvas().getUserControl().clientID, i));
	
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
			if (line.length() == 0) {
				return null;
			}
	
			JSONObject jsonObject = new JSONObject(line);
			
			String response = String.valueOf(jsonObject.get("response"));
			return response;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}

	//moves the server in the list
	public static void moveServer(String i, String method) {
		i = i.replace(" ", "%20");
		if (Canvas.getCanvas().getUserControl() == null) {
			return;
		}
		try {
			URL apiURL = new URL(String.format(MOVE_SERVER, Canvas.getCanvas().getUserControl().clientID, i, method));
			apiURL.openStream();
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	static String isString(Object o) {
		if (o instanceof String) {
			return (String)o;
		} else {
			return "";
		}
	}
	
	//Gets the user's list of parties from the client ID
	//Parties are a premium feature (essentially group IM)
	public static ArrayList<Party> getParties(String clientID) {
		ArrayList<Party> list = new ArrayList();
		try {
			URL apiURL = new URL(String.format(PARTIES_CHATS, clientID));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
	
			int partyID;
			String partyName, description;
			JSONArray jsonArray = new JSONArray(line);

			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				partyID = Integer.parseInt(jsonObject.getString("party_id"));
				partyName = jsonObject.getString("party_name");
				description = jsonObject.getString("description");
				
				ArrayList<PartyMessage> messages = new ArrayList();
				URL apiURLP = new URL(String.format(PARTIES_VIEW, clientID, partyID));
				
				BufferedReader bufferedReaderP = new BufferedReader(new InputStreamReader(apiURLP.openStream()));
				
				String inputLineP;
				String lineP = "";
				while ((inputLineP = bufferedReaderP.readLine()) != null) {
					lineP = inputLineP;
				}
				bufferedReaderP.close();
				
				JSONArray jsonArrayP = new JSONArray(lineP);
				for(int i$ = 0; i$ < jsonArrayP.length(); i$++) {
					JSONObject jsonObjectP = jsonArrayP.getJSONObject(i$);
					
					String user = jsonObjectP.getString("username");
					String message = jsonObjectP.getString("message");
					String timeStamp = jsonObjectP.getString("timestamp");
					messages.add(new PartyMessage(user, message, timeStamp));
				}
					
				Party party = new Party(partyID, partyName, messages, description);
				party.partyImage = ResourceHelper.downloadTexture(jsonObject.getString("party_icon"));
				list.add(party);
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		
		return list;
	}
	
	//Sends a message in someone's party
	public static boolean sendPartyMessage(String clientID, int partyID, String message) {
		message = message.replace(" ", "%20");
		if (Canvas.getCanvas().getUserControl() == null) {
			return false;
		}
		try {
			URL apiURL = new URL(String.format(PARTIES_NEW_MESSAGE, clientID, partyID, message));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
			
			return line.length()>0;
	
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return false;
	}

	//Gets a list of the user's friends
	public static List<Friend> getFriends(String clientID) {
		List<Friend> list = new ArrayList<Friend>();
		try {
			URL apiURL = new URL(String.format(CLIENT_FRIENDS, clientID));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
	
			JSONArray jsonArray = new JSONArray(line);
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String canvasName = null, mcname = null, status = null, ip = null;
				
				canvasName = jsonObject.getString("nickname");
				mcname = jsonObject.getString("username");
				status = jsonObject.getString("ingame_status");
				ip = jsonObject.getString("server_ip");
				
				list.add(new Friend(canvasName, mcname, status, ip));
			}
			
			for (Friend f : list) {
				URL apiURLD = new URL(String.format(VIEW_FRIEND, clientID, f.mcname));
				BufferedReader bufferedReaderD = new BufferedReader(new InputStreamReader(apiURLD.openStream()));
		
				String inputLineD;
				String lineD = "";
				while ((inputLineD = bufferedReaderD.readLine()) != null) {
					lineD = inputLineD;
				}
				bufferedReaderD.close();
				
				JSONObject jsonObjectD = new JSONObject(lineD);
				String location = null, skype = null, steam = null, website = null, description = null;
				
				if (jsonObjectD.getString("location_shown") != "F")
					location = jsonObjectD.getString("location");
				else
					location = "";
				if (jsonObjectD.getString("skype_shown") != "F")
					skype = jsonObjectD.getString("skype");
				else
					skype = "";
				if (jsonObjectD.getString("steam_shown") != "F")
					steam = jsonObjectD.getString("steam");
				else 
					steam = "";
				if (jsonObjectD.getString("website_shown") != "F")
					website = jsonObjectD.getString("website");
				else
					website = "";
				
				
				description = jsonObjectD.getString("description");
				
				f.location = location;
				f.skype = skype;
				f.steam = steam;
				f.website = website;
				f.description = description;
			}
		} catch (Exception localException) {
			//localException.printStackTrace();
		}
		return list;
	}

	//Gets the user's latest activity (notifications for sidebar)
	public static List<Activity> getLatestActivity(String clientID) {
		List<Activity> list = new ArrayList<Activity>();
		try {
			URL apiURL = new URL(String.format(LATEST_ACTIVITY, clientID));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
	
			JSONArray jsonArray = new JSONArray(line);
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String id = null, picture = null, firstname = null, ingamename = null, ip = null, message = null, partyID = null;
	
				if (jsonObject.has("id")) { id = jsonObject.getString("id"); }
				if (jsonObject.has("picture")) { picture = jsonObject.getString("picture"); }
				if (jsonObject.has("first_name")) { firstname = jsonObject.getString("first_name"); }
				if (jsonObject.has("ingamename")) { ingamename = jsonObject.getString("ingamename"); }
				if (jsonObject.has("ip")) { ip = jsonObject.getString("ip"); }
				if (jsonObject.has("message")) { message = jsonObject.getString("message"); }
	
				String type = jsonObject.getString("type").toUpperCase();
				
				ActivityType nType = null;
				if (type.equalsIgnoreCase("new_message"))
					nType = ActivityType.NEW_MESSAGE;
				if (type.equalsIgnoreCase("new_group_message"))
					nType = ActivityType.NEW_MESSAGE_GROUP;
				if (type.equalsIgnoreCase("friend_accepted"))
					nType = ActivityType.FRIEND_ACCEPTED;
				if (type.equalsIgnoreCase("friend_request"))
					nType = ActivityType.FRIEND_REQUEST;
				if (type.equalsIgnoreCase("map_download"))
					nType = ActivityType.MAP_DOWNLOAD;
				if (type.equalsIgnoreCase("server_join"))
					nType = ActivityType.SERVER_LINK;
				if (type.equalsIgnoreCase("invite"))
					nType = ActivityType.INVITE;
				list.add(new Activity(id, nType, picture, firstname, ingamename, ip, message, partyID));
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return list;
	}

	/**
	* Lets a user manage their addons from the web
	*/
	public static void manageAddonsLogin() {
		if (Canvas.getCanvas().getUserControl() == null) {
			return;
		}
		try {
			URL apiURL = new URL(String.format(ADDONS_MANAGE_LOGIN, Canvas.getCanvas().getUserControl().clientID));
			apiURL.openStream();
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	//Sets it so that nobody can see the user's online status
	public static void hideStatus() {
		hideStatus = null;
		if (Canvas.getCanvas().getUserControl() == null) {
			hideStatus = "Hide Status: Off";
			return;
		}
		try {
			URL apiURL = new URL(String.format(HIDE_STATUS, Canvas.getCanvas().getUserControl().clientID));
	
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
			if (line.length() == 0) {
				hideStatus = "Hide Status: Off";
				return;
			}
			
			if (line.equalsIgnoreCase("set shown")) {
				hideStatus = "Hide Status: Off";
				return;
			} else {
				hideStatus = "Hide Status: On";
				return;
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		hideStatus = "Hide Status: Off";
	}

	public static void getStatus() {
		hideStatus = null;
		if (Canvas.getCanvas().getUserControl() == null) {
			hideStatus = "Hide Status: Off";
			return;
		}
		try {
			URL apiURL = new URL(String.format(STATUS, Canvas.getCanvas().getUserControl().clientID));
	
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
			if (line.length() == 0) {
				hideStatus = "Hide Status: Off";
				return;
			}
			
			if (line.equalsIgnoreCase("set shown")) {
				hideStatus = "Hide Status: On";
				return;
			} else {
				hideStatus = "Hide Status: Off";
				return;
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		hideStatus = "Hide Status: Off";
	}

	//Gets a user's messages for the sidebar
	public static List<com.canvasclient.ui.interactable.slotlist.part.Message> getMessages(String clientID) {
		List<com.canvasclient.ui.interactable.slotlist.part.Message> list = new ArrayList<com.canvasclient.ui.interactable.slotlist.part.Message>();
		try {
			URL apiURL = new URL(String.format(CLIENT_MESSAGES, clientID));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
	
			JSONArray jsonArray = new JSONArray(line);
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String username = null, message = null;
	
				if (jsonObject.has("username")) { username = jsonObject.getString("username"); }
				if (jsonObject.has("message")) { message = jsonObject.getString("message"); }
	
				list.add(new com.canvasclient.ui.interactable.slotlist.part.Message(username, message, String.format(PLAYER_HEAD, username, "128")));
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return list;
	}
	
	//Gets a user's messages for their individual conversation windows
	public static ArrayList<Message> getMessagesWithFriend(String clientID, String friendName) {
		ArrayList<Message> messages = new ArrayList();
		try {
			URL apiURL = new URL(String.format(VIEW_MESSAGES, clientID, friendName));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
			
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
	
			JSONArray jsonArray = new JSONArray(line);
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String type = null, message = null;
	
				if (jsonObject.has("type")) { 
					type = jsonObject.getString("type"); 
				}
				
				if (type.contains("1")) {
					if (jsonObject.has("message")) {
						messages.add(new Message(jsonObject.getString("message"), 1));
					}
				} else if (type.contains("2")) {
					if (jsonObject.has("message")) {
						messages.add(new Message(jsonObject.getString("message"), 2));
					}
				}
			}
			
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return messages;
	}

	//Gets notifications for popups
	public static List<Notification> getUpdates(String clientID, String status, String ip) {
		List<Notification> list = NotificationManager.list;
		try {
			URL apiURL = new URL(String.format(UPDATES, clientID, status, ip));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
			JSONArray jsonArray = new JSONArray(line);
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String type = null, picture = null, canvasName = null, minecraftName = null, message = null, ipp = null;
	
				if (jsonObject.has("type")) { type = jsonObject.getString("type"); }
				if (jsonObject.has("picture")) { picture = jsonObject.getString("picture"); }
				if (jsonObject.has("first_name")) { canvasName = jsonObject.getString("first_name"); }
				if (jsonObject.has("ingamename")) { minecraftName = jsonObject.getString("ingamename"); }
				if (jsonObject.has("message")) { message = jsonObject.getString("message"); }
				if (jsonObject.has("ip")) { ipp = jsonObject.getString("ip"); }
				
				String text = "";
				ActivityType nType = null;
				if (type.equalsIgnoreCase("new_message"))
					nType = ActivityType.NEW_MESSAGE;
				if (type.equalsIgnoreCase("new_group_message"))
					nType = ActivityType.NEW_MESSAGE_GROUP;
				if (type.equalsIgnoreCase("friend_accepted"))
					nType = ActivityType.FRIEND_ACCEPTED;
				if (type.equalsIgnoreCase("friend_request"))
					nType = ActivityType.FRIEND_REQUEST;
				if (type.equalsIgnoreCase("map_download"))
					nType = ActivityType.MAP_DOWNLOAD;
				if (type.equalsIgnoreCase("server_join"))
					nType = ActivityType.SERVER_LINK;
				if (type.equalsIgnoreCase("invite"))
					nType = ActivityType.INVITE;
				switch(nType) {
				case NEW_MESSAGE:
					text = String.format("\u00a73%s \u00a77sent you a message", minecraftName);
					break;
				case NEW_MESSAGE_GROUP:
					System.out.println("group message");
					String groupName = null;
					if (jsonObject.has("group_name")) { 
						groupName = jsonObject.getString("group_name"); 
					}
					text = String.format("New message from group \u00a73%s", groupName);
					break;
				case FRIEND_ACCEPTED:
					text = String.format("\u00a73%s \u00a77accepted your friend request", minecraftName);
					break;
				case FRIEND_REQUEST:
					text = String.format("\u00a73%s \u00a77sent you a friend request", minecraftName);
					break;
				case INVITE:
					text = String.format("\u00a73%s \u00a77invited you to join a server", minecraftName);
					break;
				case MAP_DOWNLOAD:
					picture = null;
					final File savesDir = new File(IOHelper.getMinecraftDirectory() + File.separator + "saves");
					boolean downloaded = false;
					if (savesDir.isDirectory()) {
						for (File f : savesDir.listFiles()) {
							if (FilenameUtils.removeExtension(f.getName()).equalsIgnoreCase(jsonObject.getString("MAP_NAME")))
								downloaded = true;
						}
					}
					
					if (downloaded) {
						text = null;
						break;
					}
					
					
					if (jsonObject.has("MAP_NAME")) {
						text = String.format("Downloading map \u00a73%s\u00a77...", jsonObject.getString("MAP_NAME"));
					}
					if (jsonObject.has("MAP_URL")) {
						MapHandler.downloadMap(jsonObject.getString("MAP_URL"), jsonObject.getString("MAP_NAME"));
					}
					break;
				case SERVER_LINK:
					mc.world.sendQuittingDisconnectingPacket();
		            mc.loadWorld(null);
		            Canvas.getCanvas().getPlayerHandler().playerMap.clear();
					mc.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), mc, new ServerData("", ipp, false)));
				default:
					break;
				}
				
				if (nType != ActivityType.SERVER_LINK && text != null)
					list.add(new Notification(picture, text, false));
			}
				
				
		} catch (Exception localException) {
			//localException.printStackTrace();
		}
		if (list.size() > 1) {
			int size = list.size();
			list.clear();
			list.add(new Notification(null, "You have "+size+" notifications", true));
		}
		if (!list.isEmpty()) {
			if (ResourceHelper.soundNotification != null) {
				MP3Player sound = new MP3Player(ResourceHelper.soundNotification);
				sound.play();
			}
		}
		return list;
	}

	//Removes an activity from sidebar
	public static void removeActivity(String clientID, String id) {
		if (Canvas.getCanvas().getUserControl() == null) {
			return;
		}
		try {
			URL apiURL = new URL(String.format(LATEST_ACTIVITY_DELETE, clientID, id));
			apiURL.openStream();
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	//Sends a message to a friend
	public static boolean sendMessage(String clientID, String to, String message) {
		message = message.replace(" ", "%20");
		if (Canvas.getCanvas().getUserControl() == null) {
			return false;
		}
		try {
			URL apiURL = new URL(String.format(NEW_MESSAGE, clientID, to, message));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
			
			return line.length()>0;
	
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return false;
	}


	public static boolean friendAccept(String clientID, String username) {
		if (Canvas.getCanvas().getUserControl() == null) {
			return false;
		}
		try {
			URL apiURL = new URL(String.format(ACCEPT_FRIEND, clientID, username));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
			
			return line.length()>0;
	
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return false;
	}

	public static boolean friendAdd(String clientID, String username, String message) {
		message = message.replace(" ", "%20");
		if (Canvas.getCanvas().getUserControl() == null) {
			return false;
		}
		try {
			URL apiURL = new URL(String.format(ADD_FRIEND, clientID, username, message));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
			
			return true;//line.length()>0;
	
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return false;
	}

	public static boolean sendServerInvite(String clientID, String ip, String username, String message) {
		message = message.replace(" ", "%20");
		if (Canvas.getCanvas().getUserControl() == null) {
			return false;
		}
		try {
			URL apiURL = new URL(String.format(INVITE, clientID, ip, username, message));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
			
			return line.length()>0;
	
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return false;
	}

	//Gets addons a user has access to
	public static List<User> getUserExtras() {
		if (Canvas.getCanvas().getUserControl() == null) {
			return null;
		}
		try {
			URL apiURL = new URL(String.format(PLAYER_EXTRAS));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
			
			List<User> list = new ArrayList<User>();
			
			JSONArray jsonArray = new JSONArray(line);
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String minecraftName = null, ircTag = "", earSize = "l";
				boolean sizeEnabled = false, earsEnabled = false, flipEnabled = false, sunglassesEnabled = false, braceletEnabled = false, topHatEnabled = false, wingsEnabled = false;;
				float size = 1;
	
				if (jsonObject.has("ingame_name")) { minecraftName = jsonObject.getString("ingame_name"); }
				if (jsonObject.has("size_enable")) { sizeEnabled = Boolean.parseBoolean(jsonObject.getString("size_enable")); }
				if (jsonObject.has("ears_enable")) { earsEnabled = Boolean.parseBoolean(jsonObject.getString("ears_enable")); }
				if (jsonObject.has("size_variable")) { size = Float.parseFloat(jsonObject.getString("size_variable")); }
				if (jsonObject.has("ears_variable")) { earSize = jsonObject.getString("ears_variable").toLowerCase(); }
				if (jsonObject.has("upsidedown_enable")) { flipEnabled = Boolean.parseBoolean(jsonObject.getString("upsidedown_enable")); }
				if (jsonObject.has("sunglasses_enable")) { System.out.println("sungl"); sunglassesEnabled = Boolean.parseBoolean(jsonObject.getString("sunglassesEnabled")); }
				if (jsonObject.has("bracelet_enable")) { System.out.println("bracl"); braceletEnabled = Boolean.parseBoolean(jsonObject.getString("bracelet_enable")); }
				if (jsonObject.has("wings_enable")) { System.out.println("wings"); wingsEnabled = Boolean.parseBoolean(jsonObject.getString("wings_enable")); }
				if (jsonObject.has("tophat_enable")) { System.out.println("topha"); topHatEnabled = Boolean.parseBoolean(jsonObject.getString("tophat_enable")); }
				
				
				
				if (minecraftName != null) {
					list.add(new User(minecraftName, sizeEnabled, earsEnabled, flipEnabled, sunglassesEnabled, braceletEnabled, wingsEnabled, topHatEnabled, size, earSize));
				}
			}
			return list;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}

	public static List<Playlist> getPlaylists(String clientID) {
		List<Playlist> list = new ArrayList<Playlist>();
		try {
			URL apiURL = new URL(String.format(CLIENT_PLAYLISTS, clientID));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
	
			JSONArray jsonArray = new JSONArray(line);
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String name = null, id = null;
	
				id = jsonObject.getString("id");
				name = jsonObject.getString("name");
	
				list.add(new Playlist(name, id));
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return list;
	}

	public static List<Song> getPlaylistSongs(String clientID, String playlistID) {
		List<Song> list = new ArrayList<Song>();
		try {
			URL apiURL = new URL(String.format(PLAYLIST_SONGS, clientID, playlistID));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
			
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
	
			JSONArray jsonArray = new JSONArray(line);
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				int id, duration;
				String title = null, artwork = null;
				boolean starred = false;
				
				id = (int)jsonObject.getLong("id");
				title = jsonObject.getString("title");
				if (!String.valueOf(jsonObject.get("artwork")).equalsIgnoreCase("null")) {
					artwork = jsonObject.getString("artwork");
				}
				duration = (int)jsonObject.getLong("duration");
				if (jsonObject.has("starred")) {
					starred = jsonObject.getBoolean("starred");
				}
	
				list.add(new Song(id, title, artwork, duration, starred));
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return list;
	}

	//Gets a search result for music
	public static List<Song> getSearchSongs(String clientID, String tags) {
		tags = tags.replace(" ", "+");
		List<Song> list = new ArrayList<Song>();
		try {
			URL apiURL = new URL(String.format(MUSIC_SEARCH, clientID, tags));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
	
			JSONArray jsonArray = new JSONArray(line);
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				int id, duration;
				String title = null, artwork = null;
				boolean starred = false;
				
				id = (int)jsonObject.getLong("id");
				title = jsonObject.getString("title");
				if (!String.valueOf(jsonObject.get("artwork")).equalsIgnoreCase("null")) {
					artwork = jsonObject.getString("artwork");
				}
				duration = (int)jsonObject.getLong("duration");
				if (jsonObject.has("starred")) {
					starred = jsonObject.getBoolean("starred");
				}
	
				list.add(new Song(id, title, artwork, duration, starred));
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return list;
	}

	//Gets the top charts for a song
	public static List<Song> getSongCharts(String clientID) {
		List<Song> list = new ArrayList<Song>();
		try {
			URL apiURL = new URL(String.format(MUSIC_CHARTS, clientID));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
	
			JSONArray jsonArray = new JSONArray(line);
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				int id, duration;
				String title = null, artwork = null;
				boolean starred = false;
				
				id = (int)jsonObject.getLong("id");
				title = jsonObject.getString("title");
				if (!String.valueOf(jsonObject.get("artwork")).equalsIgnoreCase("null")) {
					artwork = jsonObject.getString("artwork");
				}
				duration = (int)jsonObject.getLong("duration");
				if (jsonObject.has("starred")) {
					starred = jsonObject.getBoolean("starred");
				}
				
				list.add(new Song(id, title, artwork, duration, starred));
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return list;
	}

	//Gets a user's starred songs
	public static List<Song> getSongStars(String clientID) {
		List<Song> list = new ArrayList<Song>();
		try {
			URL apiURL = new URL(String.format(MUSIC_GET_STARRED, clientID));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
	
			JSONArray jsonArray = new JSONArray(line);
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				int id, duration;
				String title = null, artwork = null;
				boolean starred = true;
				
				id = (int)jsonObject.getLong("id");
				title = jsonObject.getString("title");
				if (!String.valueOf(jsonObject.get("artwork")).equalsIgnoreCase("null")) {
					artwork = jsonObject.getString("artwork");
				}
				duration = (int)jsonObject.getLong("duration");
	
				list.add(new Song(id, title, artwork, duration, starred));
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return list;
	}

	public static String getSongStream(int sid) {
		try {
			URL apiURL = new URL(String.format(MUSIC_PLAY, String.valueOf(sid)));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
			
			JSONObject jsonObject = new JSONObject(line);
			String u = null, status = null;
			
			if (jsonObject.getString("status").equalsIgnoreCase("error")) {
				return null;
			} else {
				u = jsonObject.getString("url");
				return u;
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}

	public static boolean playlistAdd(String clientID, String n) {
		n = n.replace(" ", "%20");
		try {
			URL apiURL = new URL(String.format(PLAYLIST_ADD, clientID, n));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
			
			if (line.equalsIgnoreCase("Created")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return false;
	}

	public static boolean playlistRemove(String clientID, String playlistID) {
		try {
			URL apiURL = new URL(String.format(PLAYLIST_REMOVE, clientID, playlistID));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
			return true;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return false;
	}

	public static boolean playlistRename(String clientID, String playlistID, String playlistName) {
		playlistName = playlistName.replace(" ", "%20");
		try {
			URL apiURL = new URL(String.format(PLAYLIST_RENAME, clientID, playlistID, playlistName));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
			return true;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return false;
	}

	public static boolean songAddStar(String clientID, String sid) {
		try {
			URL apiURL = new URL(String.format(MUSIC_ADD_STARRED, clientID, sid));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
			return true;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return false;
	}

	public static boolean songRemoveStar(String clientID, String sid) {
		try {
			URL apiURL = new URL(String.format(MUSIC_REMOVE_STARRED, clientID, sid));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
			return true;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return false;
	}

	public static boolean playlistAddSong(String clientID, String playlistID, String sid) {
		try {
			URL apiURL = new URL(String.format(PLAYLIST_ADD_SONG, clientID, playlistID, sid));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
			return true;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return false;
	}

	public static boolean playlistRemoveSong(String clientID, String playlistID, String sid) {
		try {
			URL apiURL = new URL(String.format(PLAYLIST_REMOVE_SONG, clientID, playlistID, sid));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
			return true;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return false;
	}

	@Deprecated //no cheats in this new version
	public static boolean getCanCheat(String clientID, String ip) {
		ip = ip.replace(" ", "%20");
		try {
			URL apiURL = new URL(String.format(SERVER_CAN_CHEAT, clientID, ip));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
			
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
			
			JSONObject jsonObject = new JSONObject(line);
			return jsonObject.getBoolean("cheats");
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return true;
	}

	//Sets a user's color scheme (by far one of my favorite parts of the client)
	public static String[] setColorScheme(String clientID, int colorScheme) {
		try {
			URL apiURL = new URL(String.format(SET_COLOR_SCHEME, clientID, colorScheme));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apiURL.openStream()));
	
			String inputLine;
			String line = "";
			while ((inputLine = bufferedReader.readLine()) != null) {
				line = inputLine;
			}
			bufferedReader.close();
	
			JSONObject jsonObject = new JSONObject(line);
			
			String clientIDReturn = jsonObject.getString("client_id");
			String message = jsonObject.getString("message");
			return new String[] {clientIDReturn, message};
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}
	
	//Gets a rounded head texture for the messages
	public static TextureImage getRoundedHead(String user, String bgColor) {
		try {
			URL headURL = new URL(String.format(PLAYER_HEAD_ROUNDED, user, bgColor));
			return ResourceHelper.downloadTexture(headURL.toString());
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}
}
