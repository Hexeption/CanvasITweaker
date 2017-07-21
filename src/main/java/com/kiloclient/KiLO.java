package com.kiloclient;

import com.kiloclient.event.EventManager;
import com.kiloclient.event.base.Listener;
import com.kiloclient.event.boot.EventShutdown;
import com.kiloclient.event.boot.EventStartup;
import com.kiloclient.event.ui.EventTick;
import com.kiloclient.addons.AddonRenderer;
import com.kiloclient.api.APIHelper;
import com.kiloclient.users.ColorSchemeHandler;
import com.kiloclient.notification.NotificationManager;
import com.kiloclient.music.MusicHandler;
import com.kiloclient.friend.party.PartyManager;
import com.kiloclient.ui.UIHandler;
import com.kiloclient.users.PlayerHandler;
import com.kiloclient.users.UserControl;
import com.kiloclient.resource.IOHelper;
import com.kiloclient.resource.ResourceHelper;
import com.kiloclient.utilities.Authenticator;

//TODO: DO NOT FORGET TO IMPLEMENT CAPES

public class KiLO {

	private static final KiLO INSTANCE = new KiLO();  
	
	private final String NAME = "KiLO";
	
	private final String VERSION = "2.0";
	
	private final EventManager eventManager = EventManager.get();
	
	private UserControl userControl;
	
	private UIHandler uiHandler = new UIHandler();
	
	private MusicHandler musicHandler;
	
	private ColorSchemeHandler colorSchemeHandler;
	
	private PlayerHandler playerHandler;
	
	private AddonRenderer addonRenderer;
	
	private PartyManager partyManager;
	
	private boolean canCheat = true;
	
	private boolean outOfDate = false; 
	
	public KiLO() {
		eventManager.register(new Listener<EventStartup>(){
			@Override
			public void call(EventStartup event) {
				try {
					ResourceHelper.loadTextures();
				} catch (Exception e) {
					e.printStackTrace();
				}

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
				
				userControl = new UserControl("", "User", "", "false", "", "verified", "", "30", "4");
				
				IOHelper.loadConfigurationFiles();
				IOHelper.saveConfigurationFiles();

				Authenticator.instance().username("email").password("password").login();

			}
			
		});
		
		eventManager.register(new Listener<EventShutdown>() {
			@Override
			public void call(EventShutdown event) {
				IOHelper.saveConfigurationFiles();
		        APIHelper.getUpdates(KiLO.getKiLO().getUserControl().clientID, "offline", "");
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
	
	public static final KiLO getKiLO() {
		return INSTANCE;
	}
	
	public final String getName() {
		return this.getKiLO().NAME;
	}
	
	public final String getVersion() {
		return this.getKiLO().VERSION;
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
