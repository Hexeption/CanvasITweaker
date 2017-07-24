package com.canvasclient.ui;

import com.canvasclient.Canvas;
import com.canvasclient.api.APIHelper;
import com.canvasclient.render.FontHandler;
import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.render.utilities.TextureImage;
import com.canvasclient.resource.ResourceHelper;
import com.canvasclient.ui.interactable.*;
import com.canvasclient.ui.interactable.slotlist.SlotList;
import com.canvasclient.ui.interactable.slotlist.part.Playlist;
import com.canvasclient.ui.interactable.slotlist.part.Song;
import com.canvasclient.ui.interactable.slotlist.slot.PlaylistSlot;
import com.canvasclient.ui.interactable.slotlist.slot.Slot;
import com.canvasclient.ui.interactable.slotlist.slot.SongSlot;
import com.canvasclient.ui.popup.UIPopupPlaylistCreate;
import com.canvasclient.utilities.Utilities;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class UIMusic extends UI {

	private enum Mode {
		home,
		charts,
		starred,
		search,
		searchEmpty,
		playlist
	}
	
	public UI popup, popupTo;
	private boolean popupFade, loading, loadCharts, loadStars, setVolume;
	private float fX, fY, fW, fH, popupOpacity;
	private String searchTags, displayTags;

	public SlotList psl, ssl;
	
	private Playlist activePlaylist;
	private TextureImage homeImage;
	
	private Mode mode;
	
	private boolean queue;
	
	public UIMusic(boolean queue) {
		super(null);
		this.queue = queue;
	}
	
	@Override
	public void init() {
		if (queue && !Canvas.getCanvas().getUserControl().isPremium) {
			Canvas.getCanvas().getUIHandler().setCurrentUI(new UIInGameMenuQueue(this));
		}
		
		title = "Music";
		
		homeImage = ResourceHelper.downloadTexture(APIHelper.MUSIC_HOME_IMAGE);
		
		fX = Display.getWidth()/2;
		fY = (Display.getHeight()/2);
		fW = 900;
		fH = 578;

		mode = Mode.home;
		
		psl = new SlotList(4f);
		ssl = new SlotList(7f);
		
		interactables.clear();
		
		interactables.add(new IconButton(this, fX+(fW/2)-44, fY-(fH/2)+12, 24, 24, 0x00000000, ResourceHelper.iconClose[2]));
		interactables.add(new IconButton(this, fX-(fW/2)+200-32, fY-(fH/2)+16, 16, 16, 0xFF3c3c3c, ResourceHelper.iconSearch[1]));
		
		interactables.add(new IconButton(this, fX-(fW/2)+200-16, fY-(fH/2)+132+32+4, 8, 8, Canvas.getCanvas().getColorSchemeHandler().getCurrentForeground(), ResourceHelper.iconPlus[0]));
		
		interactables.add(new TextBox(this, "Search...", fX-(fW/2)+18, fY-(fH/2)+12, 200-50, 24, FontHandler.ROUNDED_BOLD.get(12), ColorHelper.DARK_GREY.getColorCode(), Align.LEFT, Align.CENTER));
		
		interactables.add(new ButtonMusic(this, "Home", fX-(fW/2)+12, fY-(fH/2)+60, 200-24, 24, FontHandler.ROUNDED_BOLD.get(12), 0x00000000, ColorHelper.GREY.getColorCode(), ResourceHelper.iconHome[0], 8));
		interactables.add(new ButtonMusic(this, "Charts", fX-(fW/2)+12, fY-(fH/2)+84, 200-24, 24, FontHandler.ROUNDED_BOLD.get(12), 0x00000000, ColorHelper.GREY.getColorCode(), ResourceHelper.iconCharts[0], 8));
		interactables.add(new ButtonMusic(this, "Starred", fX-(fW/2)+12, fY-(fH/2)+132, 200-24, 24, FontHandler.ROUNDED_BOLD.get(12), 0x00000000, ColorHelper.GREY.getColorCode(), ResourceHelper.iconStar[0], 8));

		interactables.add(new IconButton(this, fX-(fW/2)+80+150+48, fY+(fH/2)-24-8, 16, 16, ColorHelper.WHITE.getColorCode(), ResourceHelper.iconPrev[1]));
		interactables.add(new IconButton(this, fX-(fW/2)+80+150, fY+(fH/2)-24-16, 32, 32, Canvas.getCanvas().getColorSchemeHandler().getCurrentForeground(), ResourceHelper.iconPlay[3]));
		interactables.add(new IconButton(this, fX-(fW/2)+80+150+48+24, fY+(fH/2)-24-8, 16, 16, ColorHelper.WHITE.getColorCode(), ResourceHelper.iconNext[1]));
		
		interactables.add(new IconButton(this, fX+(fW/2)-16-24, fY+(fH/2)-24-12, 24, 24, ColorHelper.WHITE.getColorCode(), ResourceHelper.iconVolume[0][2]));
	}
	
	public void update(int mx, int my) {
		if (popup == null) {
			super.update(mx, my);
		}
		
		activePlaylist = null;
		for(Slot s : psl.slots) {
			if (Canvas.getCanvas().getMusicHandler().getPlaylists().size() == 0)
				continue;
			if (s.active) {
				activePlaylist = Canvas.getCanvas().getMusicHandler().getPlaylists().get(((PlaylistSlot)s).index);
				break;
			}
		}
		
		if (activePlaylist != null && mode != Mode.playlist) {
			mode = Mode.playlist;
			ssl.slots.clear();
		}
		
		int icon = (int)(Canvas.getCanvas().getMusicHandler().getVolume()*3f/100f);
		icon = Math.min(Math.max(0, icon), 2);
		((IconButton)interactables.get(10)).icon = ResourceHelper.iconVolume[icon][2];
		
		psl.x = fX-(fW/2)+12;
		psl.y = fY-(fH/2)+132+56-psl.oy;
		psl.w = 176;
		psl.h = fH-(132+56)-12-48;
		
		ssl.x = fX-(fW/2)+200+24;
		ssl.y = fY-(fH/2)+48+24+32-ssl.oy;
		ssl.w = fW-248;
		ssl.h = fH-96-32-48;
		
		if (psl.slots.size() != Canvas.getCanvas().getMusicHandler().getPlaylists().size()) {
			psl.slots.clear();
			int i = 0;
			for(Playlist f : Canvas.getCanvas().getMusicHandler().getPlaylists()) {
				psl.slots.add(new PlaylistSlot(psl, Canvas.getCanvas().getMusicHandler().getPlaylists().indexOf(f), psl.x, psl.y, psl.w, 24, 0, i*24));
				i++;
			}
		}
		
		switch(mode) {
		case search:
			if (loading && searchTags.length() > 0) {
				displayTags = searchTags;
				searchTags = "";
				new Thread() {
					@Override
					public void run() {
						List<Song> s = APIHelper.getSearchSongs(Canvas.getCanvas().getUserControl().clientID, displayTags.toUpperCase());
						if (s.isEmpty()) {
							ssl.slots.clear();
							loading = false;
							mode = Mode.searchEmpty;
						} else {
							ssl.slots.clear();
							int i = 0;
							for(Song a : s) {
								ssl.slots.add(new SongSlot(ssl, a, ssl.x, ssl.y, ssl.w, 80, 0, i*80));
								i++;
							}
							loading = false;
						}
					}
				}.start();
			}
			break;
		case charts:
			if (loading && loadCharts) {
				loadCharts = false;
				new Thread() {
					@Override
					public void run() {
						List<Song> s = APIHelper.getSongCharts(Canvas.getCanvas().getUserControl().clientID);
						if (s.isEmpty()) {
							ssl.slots.clear();
							loading = false;
							mode = Mode.home;
						} else {
							ssl.slots.clear();
							int i = 0;
							for(Song a : s) {
								ssl.slots.add(new SongSlot(ssl, a, ssl.x, ssl.y, ssl.w, 80, 0, i*80));
								i++;
							}
							loading = false;
						}
					}
				}.start();
			}
			break;
		case starred:
			if (loading && loadStars) {
				loadStars = false;
				new Thread() {
					@Override
					public void run() {
						List<Song> s = APIHelper.getSongStars(Canvas.getCanvas().getUserControl().clientID);
						if (s.isEmpty()) {
							ssl.slots.clear();
							loading = false;
							mode = Mode.home;
						} else {
							ssl.slots.clear();
							int i = 0;
							for(Song a : s) {
								ssl.slots.add(new SongSlot(ssl, a, ssl.x, ssl.y, ssl.w, 80, 0, i*80));
								i++;
							}
							loading = false;
						}
					}
				}.start();
			}
			break;
		case playlist:
			if (activePlaylist != null) {
				if (ssl.slots.size() != activePlaylist.getSize()) {
					ssl.slots.clear();
					int i = 0;
					for(Song a : activePlaylist.getList()) {
						ssl.slots.add(new SongSlot(ssl, a, ssl.x, ssl.y, ssl.w, 80, 0, i*80));
						i++;
					}
				}
			} else {
				for(Slot s : ssl.slots) {
					s.active = false;
				}
				mode = Mode.home;
			}
			break;
		default:
			break;
		}
		
		if (Canvas.getCanvas().getMusicHandler().getMusicPlayer() != null && Canvas.getCanvas().getMusicHandler().isPlaying()) {
			((IconButton)interactables.get(8)).icon = ResourceHelper.iconPause[3];
		} else {
			((IconButton)interactables.get(8)).icon = ResourceHelper.iconPlay[3];
		}
		
		if (Canvas.getCanvas().getMusicHandler().getMusicPlayer() == null || Canvas.getCanvas().getMusicHandler().getCurrentSong() == null || Canvas.getCanvas().getMusicHandler().getCurrentSongList() == null) {
			interactables.get(7).enabled = false;
			interactables.get(9).enabled = false;
		} else {
			interactables.get(7).enabled = true;
			interactables.get(9).enabled = true;
		}
		
		if (Canvas.getCanvas().getMusicHandler().getCurrentSong() == null && !Canvas.getCanvas().getMusicHandler().isPlaying() && Canvas.getCanvas().getMusicHandler().getMusicPlayer() == null) {
			SongSlot selected = null;
			for(Slot s : ssl.slots) {
				if (s.active) {
					selected = (SongSlot)s;
					break;
				}
			}
			interactables.get(8).enabled = selected != null;
		} else {
			interactables.get(8).enabled = true;
		}
		
		psl.update(mx, my);
		ssl.update(mx, my);
		
		if (popup != null) {
			popup.update(mx, my);
		}
		popupOpacity+= popupFade?-0.2f:0.2f;//((uiFadeIn?1:0)-uiFade)/4f;
		popupOpacity = Math.min(Math.max(0f, popupOpacity), 1f);
		if (popupFade) {
			if (popupOpacity < 0.05f) {
				popup = popupTo;
				popupFade = false;
				if (popup != null) {
					popup.init();
				}
			}
		}
		
	}
	
	@Override
	public void handleInteraction(final Interactable i) {
		switch(interactables.indexOf(i)) {
		case 0:
			mc.displayGuiScreen(null);
			break;
		case 1:
			if (!loading) {
				searchTags = ((TextBox)interactables.get(3)).text;
				if (searchTags.length() > 0) {
					mode = Mode.search;
					loading = true;
				}
				for(Slot s : psl.slots) {
					s.active = false;
				}
			}
			break;
		case 2:
			changePopup(new UIPopupPlaylistCreate(this));
			break;
		case 4:
			mode = Mode.home;
			for(Slot s : psl.slots) {
				s.active = false;
			}
			break;
		case 5:
			if (!loading) {
				mode = Mode.charts;
				loading = true;
				loadCharts = true;
				for(Slot s : psl.slots) {
					s.active = false;
				}
			}
			break;
		case 6:
			if (!loading) {
				mode = Mode.starred;
				loading = true;
				loadStars = true;
				for(Slot s : psl.slots) {
					s.active = false;
				}
			}
			break;
		case 7:
			Canvas.getCanvas().getMusicHandler().skipToPrevious();
			break;
		case 8:
			if (Canvas.getCanvas().getMusicHandler().getCurrentSong() != null && Canvas.getCanvas().getMusicHandler().isPlaying()) {
				Canvas.getCanvas().getMusicHandler().pauseCurrentSong();
			} else if (Canvas.getCanvas().getMusicHandler().getCurrentSong() != null) {
				Canvas.getCanvas().getMusicHandler().resumeCurrentSong();
			} else {
				SongSlot selected = null;
				for(Slot s : ssl.slots) {
					if (s.active) {
						selected = (SongSlot)s;
						break;
					}
				}
				if (selected != null) {
					selected.play();
				}
			}
			break;
		case 9:
			Canvas.getCanvas().getMusicHandler().skipToNext();
			break;
		case 10:
			interactables.add(new VolumeSlider(this, fX+(fW/2)+4, fY+(fH/2)-100-10, 12, 100));
			break;
		}
	}
	
	public void mouseClick(int mx, int my, int b) {
		if (popup == null) {
			super.mouseClick(mx, my, b);
			psl.mouseClick(mx, my, b);
			ssl.mouseClick(mx, my, b);
		} else {
			popup.mouseClick(mx, my, b);
		}
	}
	
	public void mouseRelease(int mx, int my, int b) {
		if (popup == null) {
			super.mouseRelease(mx, my, b);
			psl.mouseRelease(mx, my, b);
			ssl.mouseRelease(mx, my, b);
		} else {
			popup.mouseRelease(mx, my, b);
		}
	}
	
	public void mouseScroll(int s) {
		if (popup == null) {
			super.mouseScroll(s);
			psl.mouseScroll(s);
			ssl.mouseScroll(s);
		} else {
			popup.mouseScroll(s);
		}
	}
	
	public void keyboardPress(int key) {
		if (popup == null) {
			super.keyboardPress(key);
			switch (key) {
			case Keyboard.KEY_RETURN:
				if (interactables.get(3).active) {
					if (!loading) {
						searchTags = ((TextBox)interactables.get(3)).text;
						if (searchTags.length() > 0) {
							mode = Mode.search;
							loading = true;
						}
						for(Slot s : psl.slots) {
							s.active = false;
						}
					}
				}
				break;
			case Keyboard.KEY_ESCAPE:
				mc.displayGuiScreen(null);
				break;
			}
		} else {
			popup.keyboardPress(key);
		}
	}
	
	public void keyTyped(int key, char keyChar) {
		if (popup == null) {
			super.keyTyped(key, keyChar);
		} else {
			popup.keyTyped(key, keyChar);
		}
	}
	
	public void changePopup(UI u) {
		popupTo = u;
		popupFade = true;
	}
	
	public void setVolume(int volume) {
		Canvas.getCanvas().getMusicHandler().setVolume(volume);
	}
	
	public void render(float opacity) {
		GlStateManager.disableDepth();
		GuiHelper.drawRectangle(0, 0, Display.getWidth(), Display.getHeight(), Utilities.reAlpha(ColorHelper.BLACK.getColorCode(), 0.7f*opacity));
		
		GuiHelper.drawRectangle(fX-(fW/2), fY-(fH/2)+48, fX-(fW/2)+200, fY+(fH/2)-48, Utilities.reAlpha(0xFF1c1c1c, 1f * opacity));
		GuiHelper.drawRectangle(fX-(fW/2)+200, fY-(fH/2)+48, fX+(fW/2), fY+(fH/2)-48, Utilities.reAlpha(0xFF242424, 1f * opacity));
		GuiHelper.drawRectangle(fX-(fW/2), fY+(fH/2)-48, fX+(fW/2), fY+(fH/2), Utilities.reAlpha(0xFF1A1A1A, 1f*opacity));
		
		GuiHelper.drawRectangle(fX-(fW/2), fY-(fH/2), fX+(fW/2), fY-(fH/2)+48, Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentBackground(), 1f*opacity));
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(25), fX, fY-(fH/2)+24, title, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.CENTER, Align.CENTER);
		
		GuiHelper.drawRectangle(fX-(fW/2)+12, fY-(fH/2)+12, fX-(fW/2)+200-12, fY-(fH/2)+48-12, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
		
		GuiHelper.drawRectangle(fX-(fW/2)+12, fY-(fH/2)+48+12+24+24+12, fX-(fW/2)+200-12, fY-(fH/2)+48+12+24+24+13, Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentBackground(), 0.5f*opacity));
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), fX-(fW/2)+16, fY-(fH/2)+132+32, "PLAYLISTS", Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 0.5f*opacity));
		
		String timeFormat = "00:00:00";
		
		GuiHelper.drawRectangle(fX-(fW/2)+80+150+48+24+38+ FontHandler.STANDARD.get(12).getWidth(timeFormat)+3, fY+(fH/2)-24-3, fX+(fW/2)-16-24-12- FontHandler.STANDARD.get(12).getWidth(timeFormat)-3, fY+(fH/2)-24+3, Utilities.reAlpha(0xFF0A0A0A, 1f*opacity));
		GuiHelper.drawCircle(fX-(fW/2)+80+150+48+24+38+ FontHandler.STANDARD.get(12).getWidth(timeFormat)+3, fY+(fH/2)-24, 3, Utilities.reAlpha(0xFF0A0A0A, 1f*opacity));
		GuiHelper.drawCircle(fX+(fW/2)-16-24-12- FontHandler.STANDARD.get(12).getWidth(timeFormat)-3, fY+(fH/2)-24, 3, Utilities.reAlpha(0xFF0A0A0A, 1f*opacity));
		if (Canvas.getCanvas().getMusicHandler().getMusicPlayer() != null && Canvas.getCanvas().getMusicHandler().getCurrentSong() != null) {
			Song song = Canvas.getCanvas().getMusicHandler().getCurrentSong();
			if (song != null) {

				int pos = Canvas.getCanvas().getMusicHandler().getMusicPlayer().getPosition();
				float barPos = (1f/(float)song.duration)*(float)(pos+1);
				float barWidth = (fX+(fW/2)-16-24-12- FontHandler.STANDARD.get(12).getWidth(timeFormat)-3)-(fX-(fW/2)+80+150+48+24+32+ FontHandler.STANDARD.get(12).getWidth(timeFormat)+3);
				
				GuiHelper.drawRectangle(fX-(fW/2)+80+150+48+24+38+ FontHandler.STANDARD.get(12).getWidth(timeFormat)+3, fY+(fH/2)-24-3, fX-(fW/2)+80+150+48+24+38+ FontHandler.STANDARD.get(12).getWidth(timeFormat)+3+(barWidth*barPos), fY+(fH/2)-24+3, Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentForeground(), 1f*opacity));
				GuiHelper.drawCircle(fX-(fW/2)+80+150+48+24+38+ FontHandler.STANDARD.get(12).getWidth(timeFormat)+3, fY+(fH/2)-24, 3, Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentForeground(), 1f*opacity));
				GuiHelper.drawCircle(fX-(fW/2)+80+150+48+24+38+ FontHandler.STANDARD.get(12).getWidth(timeFormat)+3+(barWidth*barPos), fY+(fH/2)-24, 3, Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentForeground(), 1f*opacity));

				String time = "0:00";
				if (pos >= 3600000) {
					String format = "%2d:%02d:%02d";
					time = String.format(format,
							TimeUnit.MILLISECONDS.toHours(pos),
						    TimeUnit.MILLISECONDS.toMinutes(pos) % TimeUnit.HOURS.toMinutes(1),
						    TimeUnit.MILLISECONDS.toSeconds(pos) % TimeUnit.MINUTES.toSeconds(1));
				} else if (pos >= 60000) {
					String format = "%2d:%02d";
					time = String.format(format,
							TimeUnit.MILLISECONDS.toMinutes(pos) % TimeUnit.HOURS.toMinutes(1),
						    TimeUnit.MILLISECONDS.toSeconds(pos) % TimeUnit.MINUTES.toSeconds(1));
				} else if (pos >= 1000) {
					String format = "0:%02d";
					time = String.format(format,
						    TimeUnit.MILLISECONDS.toSeconds(pos) % TimeUnit.MINUTES.toSeconds(1));
				}
				GuiHelper.drawStringFromTTF(FontHandler.STANDARD.get(12), fX-(fW/2)+80+150+48+24+32+ FontHandler.STANDARD.get(12).getWidth(timeFormat), fY+(fH/2)-24, time, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.RIGHT, Align.CENTER);
				
				String duration = "0:00";
				if (song.duration >= 3600000) {
					String format = "%2d:%02d:%02d";
					duration = String.format(format,
							TimeUnit.MILLISECONDS.toHours(song.duration),
						    TimeUnit.MILLISECONDS.toMinutes(song.duration) % TimeUnit.HOURS.toMinutes(1),
						    TimeUnit.MILLISECONDS.toSeconds(song.duration) % TimeUnit.MINUTES.toSeconds(1));
				} else if (song.duration >= 60000) {
					String format = "%2d:%02d";
					duration = String.format(format,
							TimeUnit.MILLISECONDS.toMinutes(song.duration) % TimeUnit.HOURS.toMinutes(1),
						    TimeUnit.MILLISECONDS.toSeconds(song.duration) % TimeUnit.MINUTES.toSeconds(1));
				} else if (song.duration >= 1000) {
					String format = "0:%02d";
					duration = String.format(format,
						    TimeUnit.MILLISECONDS.toSeconds(song.duration) % TimeUnit.MINUTES.toSeconds(1));
				}
				GuiHelper.drawStringFromTTF(FontHandler.STANDARD.get(12), fX+(fW/2)-16-24-2- FontHandler.STANDARD.get(12).getWidth(timeFormat)-3, fY+(fH/2)-24, duration, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.LEFT, Align.CENTER);
				
				if (song.image != null && song.image.getTexture() != null) {
					GuiHelper.drawTexturedRectangle(fX-(fW/2), fY+(fH/2)-48, 48, 48, song.image.getTexture());
				} else {
					GuiHelper.drawLoaderAnimation(fX-(fW/2)+24, fY+(fH/2)-24, 8, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
				}
				String title = song.title;
				for(int i = 0; i < title.length(); i++) {
					if (FontHandler.ROUNDED_BOLD.get(12).getWidth(title.substring(0, i)) > 150- FontHandler.ROUNDED_BOLD.get(12).getWidth("...")) {
						title = title.substring(0, i)+"...";
						break;
					}
				}
				GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), fX-(fW/2)+64, fY+(fH/2)-24, title, Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentForeground(), 1f*opacity), Align.LEFT, Align.CENTER);
			} else {
				GuiHelper.drawLoaderAnimation(fX-(fW/2)+24, fY+(fH/2)-24, 8, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
			}
		}
		
		switch(mode) {
		case charts:
			if (!loading) {
				GuiHelper.drawRectangle(fX-(fW/2)+224, fY-(fH/2)+48+24+32-2, fX+(fW/2)-24, fY-(fH/2)+48+24+32, Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 0.5f*opacity));
				GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(16), fX-(fW/2)+200+24, fY-(fH/2)+48+24+4, "Charts", Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentForeground(), 1f*opacity), Align.LEFT, Align.CENTER);
				GuiHelper.startClip(fX-(fW/2)+224, fY-(fH/2)+48+24+32, fX+(fW/2)-24+ssl.sbw, fY+(fH/2)-48-24);
				ssl.render(opacity);
				GuiHelper.endClip();
			} else {
				GuiHelper.drawLoaderAnimation(fX+100, fY, 32, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
			}
			break;
		case home:
			if (homeImage != null && homeImage.getTexture() != null) {
				GuiHelper.drawTexturedRectangle(fX-(fW/2)+200, fY-(fH/2)+48, fW-200, fH-96, homeImage.getTexture());
			}
			break;
		case playlist:
			if (activePlaylist != null) {
				GuiHelper.drawRectangle(fX-(fW/2)+224, fY-(fH/2)+48+24+32-2, fX+(fW/2)-24, fY-(fH/2)+48+24+32, Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 0.5f*opacity));
				String title = activePlaylist.name;
				for(int i = 0; i < title.length(); i++) {
					if (FontHandler.ROUNDED_BOLD.get(12).getWidth(title.substring(0, i)) > (fW-296)-128- FontHandler.ROUNDED_BOLD.get(12).getWidth("...")) {
						title = title.substring(0, i)+"...";
						break;
					}
				}
				GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(16), fX-(fW/2)+200+24, fY-(fH/2)+48+24+5, title, Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentForeground(), 1f*opacity), Align.LEFT, Align.CENTER);
				GuiHelper.startClip(fX-(fW/2)+224, fY-(fH/2)+48+24+32, fX+(fW/2)-24+ssl.sbw, fY+(fH/2)-48-24);
				ssl.render(opacity);
				GuiHelper.endClip();
			}
			break;
		case search:
			if (!loading) {
				GuiHelper.drawRectangle(fX-(fW/2)+224, fY-(fH/2)+48+24+32-2, fX+(fW/2)-24, fY-(fH/2)+48+24+32, Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 0.5f*opacity));
				String title = "Search - "+displayTags;
				for(int i = 0; i < title.length(); i++) {
					if (FontHandler.ROUNDED_BOLD.get(12).getWidth(title.substring(0, i)) > (fW-296)-128- FontHandler.ROUNDED_BOLD.get(12).getWidth("...")) {
						title = title.substring(0, i)+"...";
						break;
					}
				}
				GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(16), fX-(fW/2)+200+24, fY-(fH/2)+48+24+5, title, Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentForeground(), 1f*opacity), Align.LEFT, Align.CENTER);
				GuiHelper.startClip(fX-(fW/2)+224, fY-(fH/2)+48+24+32, fX+(fW/2)-24+ssl.sbw, fY+(fH/2)-48-24);
				ssl.render(opacity);
				GuiHelper.endClip();
			} else {
				GuiHelper.drawLoaderAnimation(fX+100, fY, 32, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
			}
			break;
		case searchEmpty:
			GuiHelper.drawRectangle(fX-(fW/2)+224, fY-(fH/2)+48+24+32-2, fX+(fW/2)-24, fY-(fH/2)+48+24+32, Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 1f*opacity));
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(16), fX-(fW/2)+200+24, fY-(fH/2)+48+24+16, "No results found", Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentForeground(), 1f*opacity), Align.LEFT, Align.CENTER);
			break;
		case starred:
			if (!loading) {
				GuiHelper.drawRectangle(fX-(fW/2)+224, fY-(fH/2)+48+24+32-2, fX+(fW/2)-24, fY-(fH/2)+48+24+32, Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 0.5f*opacity));
				GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(16), fX-(fW/2)+200+24, fY-(fH/2)+48+24+5, "Starred", Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentForeground(), 1f*opacity), Align.LEFT, Align.CENTER);
				GuiHelper.startClip(fX-(fW/2)+224, fY-(fH/2)+48+24+32, fX+(fW/2)-24+ssl.sbw, fY+(fH/2)-48-24);
				ssl.render(opacity);
				GuiHelper.endClip();
			} else {
				GuiHelper.drawLoaderAnimation(fX+100, fY, 32, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
			}
			break;
		}
		
		GuiHelper.startClip(fX-(fW/2)+8, fY-(fH/2)+132+56, fX-(fW/2)+192+psl.sbw, fY+(fH/2)-48-12);
		psl.render(opacity);
		GuiHelper.endClip();
		
		super.render(opacity);
		GuiHelper.drawGradientRectangle(fX-(fW/2)+200, fY+(fH/2)-48-32, fX+(fW/2), fY+(fH/2)-48, Utilities.reAlpha(ColorHelper.BLACK.getColorCode(), 0f), Utilities.reAlpha(ColorHelper.BLACK.getColorCode(), 0.4f*opacity));
		
		if (popup != null) {
			popup.render(opacity*(Math.max(popupOpacity, 0.05f)));
		}
		GlStateManager.enableDepth();
	}
}
