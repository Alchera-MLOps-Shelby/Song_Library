package ch.makery.song.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * Helper class to wrap a list a song. This is used for saving the
 * list of songs to XML.
 */
@XmlRootElement(name = "songs")
public class SongListWrapper {

	private List<Song> songs;

	@XmlElement(name = "song")
	public List<Song> getSongs() {
		return songs;
	}

	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}
}
