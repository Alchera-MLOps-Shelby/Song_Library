package ch.makery.song.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import ch.makery.song.SongLib;
import ch.makery.song.model.Song;

public class SongOverviewController {
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

		//Preselect the first song in the column.

		//Clear song details.
		showSongDetails(null);

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
			songTable.getItems().remove(selectedIndex);
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
