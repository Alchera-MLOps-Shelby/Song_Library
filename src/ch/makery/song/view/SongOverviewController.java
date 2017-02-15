package ch.makery.song.view;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import ch.makery.song.SongLib;
import ch.makery.song.model.Song;

public class SongOverviewController {

	int numSongs = 6;
	int currentSong;
	/*
	 * All private fields and methods where the fxml file
	 * needs access must be annotated with "@FXML".
	 */
	@FXML
	private TableView<Song> songTable;
	@FXML
	private TableColumn<Song, String> songTitleColumn;

	@FXML
	private Label songTitleLabel;
	@FXML
	private Label artistNameLabel;
	@FXML
	private Label albumTitleLabel;
	@FXML
	private Label yearLabel;

	//Reference to the main application.
	private SongLib songLib;

	/*
	 * The constructor.
	 * The constructor is called before the initialize() method.
	 */
	public SongOverviewController() {
	}

	/*
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		//Initialize the song table with the column.
		songTitleColumn.setCellValueFactory(cellData -> cellData.getValue().songTitleProperty());

		//Clear song details.
		showSongDetails(null);
		currentSong = 0;

		//Listen for selection changes and show the person details when changed.
		songTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showSongDetails(newValue));
	}

	/*
	 * Is called by the main application to give a reference back to itself.
	 *
	 * @param songLib
	 */
	public void setSongLib(SongLib songLib) {
		this.songLib = songLib;

	//	Callback<Song,Observable[]> cb =(Song song) -> new Observable[]{
	//	        song.songTitleProperty(),
	//	    };

		// 3. Wrap the observable list in a SortedList.
       SortedList<Song> sortedData = new SortedList<>(songLib.getSongData(),
       (Song song1, Song song2) -> {
    	   if((song1.getSongTitle()).compareTo(song2.getSongTitle()) < 0){
    		   return -1;
    	   } else if ((song1.getSongTitle()).compareTo(song2.getSongTitle()) > 0 ){
    		   return 1;
    	   } else {
    		   return 0;
    	   }
       });

	  	// 4. Bind the SortedList comparator to the TableView comparator.
       	//sortedData.comparatorProperty().bind(songTable.comparatorProperty());

       	//Add observable list data to the table.
		songTable.setItems(sortedData);

		songTable.getSelectionModel().select(currentSong);
		songTable.getFocusModel().focus(currentSong);

		//Add observable list data to the table.
		songTable.setItems(songLib.getSongData());
	}

	private void showSongDetails(Song song) {
		if(song != null) {
			//Fill the labels with info from the song object.
			songTitleLabel.setText(song.getSongTitle());
			artistNameLabel.setText(song.getArtistName());
			albumTitleLabel.setText(song.getAlbumTitle());
			yearLabel.setText(Integer.toString(song.getYear()));
		} else {
			songTitleLabel.setText("");
			artistNameLabel.setText("");
			albumTitleLabel.setText("");
			yearLabel.setText("");
		}
	}

	/*
	 * Called when the user clicks on the delete button.
	 */
	@FXML
	private void handleDeleteSong() {
		int selectedIndex = songTable.getSelectionModel().getSelectedIndex();
		if(selectedIndex >= 0) {
			currentSong = selectedIndex;
			songTable.getItems().remove(selectedIndex);
			numSongs--;
			if (numSongs > 0){
				if (currentSong + 1 >= numSongs){
					songTable.getSelectionModel().select(numSongs - 1);
					songTable.getFocusModel().focus(numSongs - 1);
				} else {
				songTable.getSelectionModel().select(currentSong);
				songTable.getFocusModel().focus(currentSong);
				}
			} else return;
		} else {
			//No song selected.
			Alert noSongAlert = new Alert(AlertType.WARNING);
			noSongAlert.initOwner(songLib.getPrimaryStage());
			noSongAlert.setTitle("Warning: Error");
			noSongAlert.setHeaderText("No Song Selected");
			noSongAlert.setContentText("You did not select a song or there are no songs left in the table to delete.");
			noSongAlert.showAndWait();
		}
	}

	/*
	 * Called when the user clicks the Add button. Opens a dialog
	 * to edit details for a new song.
	 */
	@FXML
	private void handleNewSong() {
		Song tempSong = new Song();
		boolean okClicked = songLib.showSongEditDialog(tempSong);
		if(okClicked) {
			songLib.getSongData().add(tempSong);
			numSongs++;
		}
	}

	/*
	 * Called when the user clicks the Edit button. Opens a dialog
	 * to edit details for the selected person.
	 */
	@FXML
	private void handleEditSong() {
		Song selectedSong = songTable.getSelectionModel().getSelectedItem();
		if(selectedSong != null) {
			boolean okClicked = songLib.showSongEditDialog(selectedSong);
			if(okClicked) {
				showSongDetails(selectedSong);
			}
		} else {
			//No song selected.
			Alert alert = new Alert(AlertType.WARNING);
	        alert.initOwner(songLib.getPrimaryStage());
	        alert.setTitle("No Selection");
	        alert.setHeaderText("No Song Selected");
	        alert.setContentText("Please select a song in the table.");

	        alert.showAndWait();

		}
	}
}
