package ch.makery.song.model;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/*
 * Model class for a Song.
 */
public class Song {

	private final StringProperty songTitle;
	private final StringProperty artistName;
	private final StringProperty albumTitle;
	private final IntegerProperty year;

	/*
	 * Default constructor.
	 */
	public Song() {
		this(null);
	}

	/*
	 * Constructor with some initial data.
	 *
	 * @param songTitle;
	 */
	public Song(String songTitle) {
		this.songTitle = new SimpleStringProperty(songTitle);

		//Initial dummy data for testing purposes.
		this.artistName = new SimpleStringProperty("Artist's Name");
		this.albumTitle = new SimpleStringProperty("Album Title");
		this.year = new SimpleIntegerProperty(2000);
	}

	/*
	 * Below are getter, setter, and property functions for songTitle, artistName, albumTitle, and year.
	 */

	//Song title getter method.
	public String getSongTitle() {
		return songTitle.get();
	}

	//Song title setter method.
	public void setSongTitle(String songTitle) {
		this.songTitle.set(songTitle);
	}

	//Song title property function to use to notify when song title is changed.
	public StringProperty songTitleProperty() {
		return songTitle;
	}

	//Artist name getter method.
	public String getArtistName() {
		return artistName.get();
	}

	//Artist name setter method.
	public void setArtistName(String artistName) {
		this.artistName.set(artistName);
	}

	//Artist property function to use to notify when artist name is changed.
	public StringProperty artistNameProperty() {
		return artistName;
	}

	//Album title getter method.
	public String getAlbumTitle() {
		return albumTitle.get();
	}

	//Album title setter method.
	public void setAlbumTitle(String albumTitle) {
		this.albumTitle.set(albumTitle);
	}

	//Album title property function to use to notify when album title is changed.
	public StringProperty albumTitleProperty() {
		return albumTitle;
	}

	//Year getter method.
	public Integer getYear() {
		return year.get();
	}

	//Year setter method.
	public void setYear(Integer year) {
		this.year.set(year);
	}

	//Year property function to use to notify when year is changed.
	public IntegerProperty yearProperty() {
		return year;
	}
}
