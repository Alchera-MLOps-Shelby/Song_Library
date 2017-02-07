/*
 * Dialog to edit details of a song.
 */
package ch.makery.song.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ch.makery.song.model.Song;

public class SongEditDialogController {

	@FXML
	private TextField songTitleField;
	@FXML
	private TextField artistNameField;
	@FXML
	private TextField albumTitleField;
	@FXML
	private TextField yearField;

	private Stage dialogStage;
	private Song song;
	private boolean okClicked = false;

	/*
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
	}

	/*
	 * Sets the stage of this dialog.
	 *
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/*
	 * Sets the song to be edited in the dialog
	 *
	 * @param song
	 */
	public void setSong(Song song) {
		this.song = song;

		songTitleField.setText(song.getSongTitle());
		artistNameField.setText(song.getArtistName());
		albumTitleField.setText(song.getAlbumTitle());
		yearField.setText(Integer.toString(song.getYear()));
	}

	/*
	 * Returns true if the user clicked OK, false otherwise.
	 *
	 * @return
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	/*
	 * Called when the user clicks ok.
	 */

	@FXML
	private void handleOk() {
		if (isInputValid()) {
			song.setSongTitle(songTitleField.getText());
			song.setArtistName(artistNameField.getText());
			song.setAlbumTitle(albumTitleField.getText());
			song.setYear(Integer.parseInt(yearField.getText()));

			okClicked = true;
			dialogStage.close();
		}
	}

	//Called when the user clicks Cancel.
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	/*
	 * Validates the user input in the text fields.
	 *
	 * Both song title and artist name are
	 * necessary for valid song detail and must be unique as a combination.
	 * Album title and year are not necessary.
	 *
	 * @return true if the input is valid
	 */
	private boolean isInputValid() {
		String errorMessage = "";

		//Check for empty songTitleField.
		if(songTitleField.getText() == null || songTitleField.getText().length() == 0) {
			errorMessage += "Please enter a valid song title.";
		}
		//Check for empty artistNameField.
		if(artistNameField.getText() == null || artistNameField.getText().length() == 0) {
			errorMessage += "Please enter a valid artist name.";
		}
		//TODO: Check that album title can be left empty.

		//TODO: Check for proper integer input of year.

		//TODO: Check for uniqueness of song title and artist name.
			/*if array index of song title and array index of artist name are the same,
			 *errorMessage += "You already have that song.";
			 */

		//TODO: pre-select table element
		//TODO: table sort? sort function is already built-in when table head is clicked on
		//TODO: memory storage via XML
		//TODO: add/delete selects the next, but if there is no next, then select prev

		if(errorMessage.length() == 0) {
			return true;
		} else {
			//Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
		}
	}
}