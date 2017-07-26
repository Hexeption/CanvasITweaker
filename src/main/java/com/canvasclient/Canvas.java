package com.canvasclient;

import com.canvasclient.addons.AddonRenderer;
import com.canvasclient.api.APIHelper;
import com.canvasclient.event.EventManager;
import com.canvasclient.event.base.Listener;
import com.canvasclient.event.boot.EventShutdown;
import com.canvasclient.event.boot.EventStartup;
import com.canvasclient.event.ui.EventTick;
import com.canvasclient.friend.party.PartyManager;
import com.canvasclient.music.MusicHandler;
import com.canvasclient.notification.NotificationManager;
import com.canvasclient.resource.IOHelper;
import com.canvasclient.resource.ResourceHelper;
import com.canvasclient.ui.UIHandler;
import com.canvasclient.users.ColorSchemeHandler;
import com.canvasclient.users.PlayerHandler;
import com.canvasclient.users.UserControl;
import com.canvasclient.utilities.Authenticator;

public class Canvas {

	private static final Canvas INSTANCE = new Canvas();
	
	private final String NAME = "Canvas";
	
	private final String VERSION = "";
	
	private final EventManager eventManager = EventManager.get();
	
	private UserControl userControl;
	
	private UIHandler uiHandler ;
	
	private MusicHandler musicHandler;
	
	private ColorSchemeHandler colorSchemeHandler;
	
	private PlayerHandler playerHandler;
	
	private AddonRenderer addonRenderer;
	
	private PartyManager partyManager;
	
	private boolean outOfDate = false; 
	
	public Canvas() {
		eventManager.register(new Listener<EventStartup>(){
			@Override
			public void call(EventStartup event) {
				try {
					ResourceHelper.loadTextures();
				} catch (Exception e) {
					e.printStackTrace();
				}

				uiHandler = new UIHandler();
				musicHandler = new MusicHandler();
				colorSchemeHandler = new ColorSchemeHandler();
				playerHandler = new PlayerHandler();
				addonRenderer = new AddonRenderer();
				partyManager = new PartyManager();

				/*String clientID = IOHelper.loadClientID();
				try { 
					if (clientID.length() > 0) {
						String[] clientDetails = APIHelper.getClientDetails(clientID);
						if (clientDetails.length == 12) {
							userControl = new UserControl(clientDetails[0], clientDetails[1], clientDetails[2], clientDetails[3], clientDetails[4], clientDetails[5], clientDetails[6], clientDetails[7], clientDetails[8], clientDetails[9]));
						} else {
							userControl = null;
						}
					} else {
						userControl = null;
					}
				} catch (Exception e) {
					userControl = new UserControl(clientID, "User", "", "false", "", "verified", "", "30", "1", null, null, false);
				}*/
				
				userControl = new UserControl("", "User", "", "false", "", "verified", "", "30", "6");
				
				IOHelper.loadConfigurationFiles();
				IOHelper.saveConfigurationFiles();

				Authenticator.instance().username("email").password("password").login();
			}
			
		});
		
		eventManager.register(new Listener<EventShutdown>() {
			@Override
			public void call(EventShutdown event) {
				IOHelper.saveConfigurationFiles();
		        APIHelper.getUpdates(Canvas.getCanvas().getUserControl().clientID, "offline", "");
			}
		});
		eventManager.register(new Listener<EventTick>() {
			@Override
			public void call(EventTick event) {
				NotificationManager.update();
				
				colorSchemeHandler.setScheme(3);
			}
		});
	}
	
	public static final Canvas getCanvas() {
		return INSTANCE;
	}
	
	public final String getName() {
		return this.getCanvas().NAME;
	}
	
	public final String getVersion() {
		return this.getCanvas().VERSION;
	}
	
	public EventManager getEventManager() {
		return this.eventManager;
	}
	
	public UserControl getUserControl() {
		return this.userControl;
	}
	
	public void setUserControl(UserControl userControl) {
		this.userControl = userControl;
	}
	
	public UIHandler getUIHandler() {
		return this.uiHandler;
	}
	
	public MusicHandler getMusicHandler() {
		return this.musicHandler;
	}
	
	public ColorSchemeHandler getColorSchemeHandler() {
		return this.colorSchemeHandler;
	}
	
	public PlayerHandler getPlayerHandler() {
		return this.playerHandler;
	}
	
	public AddonRenderer getAddonRenderer() {
		return this.addonRenderer;
	}
	
	public PartyManager getPartyManager() {
		return this.partyManager;
	}
	
	public boolean outOfDate() {
		return this.outOfDate;
	}
	
	public void setOutOfDate(boolean outOfDate) {
		this.outOfDate = outOfDate;
	}
}
