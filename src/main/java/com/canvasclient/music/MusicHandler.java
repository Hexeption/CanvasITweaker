package com.canvasclient.music;

import com.canvasclient.event.base.Listener;
import com.canvasclient.event.ui.EventTick;
import com.canvasclient.Canvas;
import com.canvasclient.api.APIHelper;
import com.canvasclient.ui.interactable.slotlist.part.Playlist;
import com.canvasclient.ui.interactable.slotlist.part.Song;
import jaco.mp3.player.MP3Player;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MusicHandler {

	private MP3Player musicPlayer;
	
	private Song currentSong;
	
	private boolean isPlaying = false;
	
	private List<Song> currentSongList = new ArrayList();
	
	private float musicVolume;	

	private List<Playlist> songPlaylists = new ArrayList();	

	public MusicHandler() {
		this.musicVolume = 100F;
		Canvas.getCanvas().getEventManager().register(new Listener<EventTick>(){
			@Override
			public void call(EventTick event) {
				if (getMusicPlayer() != null && getMusicPlayer().isStopped()) {
					if (getCurrentSongList() != null)
						skipToNext();
					else
						stopPlaying();
				}
			}
		});
	}
	
	public void playMusic(final List<Song> songList, final Song song) {
		Canvas.getCanvas().getMusicHandler().stopPlaying();
		new Thread() {
			@Override
			public void run() {
				try {
					String s = APIHelper.getSongStream(song.id);
					if (s ==  null) {
						return;
					}
					URL u = new URL(s);
					musicPlayer = new MP3Player(u);
					musicPlayer.setRepeat(false);
					setCurrentSong(song);
					setCurrentSongList(songList);
					setVolume(musicVolume);
					musicPlayer.play();
					setIsPlaying(true);
				} catch (Exception e) {
					setCurrentSong(null);
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public void pauseCurrentSong() {
		if (musicPlayer != null) {
			musicPlayer.pause();
			setIsPlaying(false);
		}
	}
	
	public void resumeCurrentSong() {
		if (musicPlayer != null) {
			setVolume(musicVolume);
			musicPlayer.play();
			setIsPlaying(true);
		}
	}
	
	public MP3Player getMusicPlayer() {
		return this.musicPlayer;
	}
	
	public void setMusicPlayer(MP3Player musicPlayer) {
		this.musicPlayer = musicPlayer;
	}
	
	public Song getCurrentSong() {
		return this.currentSong;
	}
	
	public void setCurrentSong(Song newSong) {
		this.currentSong = newSong;
	}
	
	public List<Song> getCurrentSongList() {
		return this.currentSongList;
	}
	
	public void setCurrentSongList(List<Song> songList) {
		this.currentSongList = songList;
	}
	
	public boolean isPlaying() {
		return this.isPlaying;
	}
	
	public void setIsPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
	
	public void skipToNext() {
		if (musicPlayer != null) {
			int curInd = this.getCurrentSongList().indexOf(currentSong);
			if (curInd == this.getCurrentSongList().size()-1) {
				stopPlaying();
				return;
			}
			playMusic(this.getCurrentSongList(), this.getCurrentSongList().get(curInd+1));
		}
	}
	
	public void skipToPrevious() {
		if (musicPlayer != null) {
			int curInd = this.getCurrentSongList().indexOf(currentSong);
			if (curInd == 0) {
				stopPlaying();
				return;
			}
			playMusic(this.getCurrentSongList(), this.getCurrentSongList().get(curInd-1));
		}
	}
	
	public void stopPlaying() {
		if (musicPlayer != null) {
			musicPlayer.stop();
			musicPlayer = null;
			this.setCurrentSong(null);
			setIsPlaying(false);
		}
	}
	
	public float getVolume() {
		return this.musicVolume;
	}
	
	public void setVolume(float volume) {
		musicVolume = volume;
		if (musicPlayer != null) {
			musicPlayer.setVolume((int)musicVolume);
		}
	}

	public List<Playlist> getPlaylists() {
		return songPlaylists;
	}
	
	public void setPlaylists(List<Playlist> newList) {
		this.songPlaylists = newList;
	}

	public void addPlaylist(String n, String i) {
		for(Playlist p : songPlaylists) {
			if (p.id.equalsIgnoreCase(i)) {
				return;
			}
		}
		songPlaylists.add(new Playlist(n, i));
	}

	public void addPlaylist(Playlist f) {
		songPlaylists.add(f);
	}

	public void addPlaylist(int index, Playlist f) {
		songPlaylists.add(index, f);
	}

	public void removePlaylist(Playlist f) {
		songPlaylists.remove(f);
	}

	public void removePlaylist(int index) {
		songPlaylists.remove(songPlaylists.get(index));
	}

	public Playlist getPlaylist(String i) {
		for(Playlist p : songPlaylists) {
			if (p.id.equalsIgnoreCase(i)) {
				return p;
			}
		}
		return null;
	}
	
}
