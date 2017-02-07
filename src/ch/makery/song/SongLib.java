package ch.makery.song;

import java.io.IOException;

import ch.makery.song.model.Song;
import ch.makery.song.view.SongEditDialogController;
import ch.makery.song.view.SongOverviewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SongLib extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;

	//The data as an observable list of Songs.
	private ObservableList<Song> songData = FXCollections.observableArrayList();

	public SongLib() {
		//Sample data.
		songData.add(new Song("Happy"));
		songData.add(new Song("Sad"));
		songData.add(new Song("Angry"));
		songData.add(new Song("Bored"));
		songData.add(new Song("Content"));
		songData.add(new Song("Cynical"));
	}

	public ObservableList<Song> getSongData() {
		return songData;
	}
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Song Library");

		initRootLayout();

		showSongOverview();
	}

	/*
	 * Initializes the root layout.
	 */
	public void initRootLayout() {
		try {
			//Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(SongLib.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			//Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);;
			primaryStage.show();
		} catch (IOException exceptionIO) {
			exceptionIO.printStackTrace();
		}
	}
	/*
	 * Shows the song overview inside the root layout.
	 */
	public void showSongOverview() {
		try {
			//Load song overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(SongLib.class.getResource("view/SongOverview.fxml"));
			AnchorPane songOverview = (AnchorPane) loader.load();

			//Set song overview into the center of root layout.
			rootLayout.setCenter(songOverview);

			//Give the controller access to the main app.
			SongOverviewController controller = loader.getController();
			controller.setSongLib(this);
		} catch (IOException exceptionIO) {
			exceptionIO.printStackTrace();
		}
	}

	/*
	 * Opens a dialog to edit details for the specified song. If the user
	 * clicks OK, the changes are saved into the provided song object and
	 * true is returned.
	 *
	 * @param song the song object to be edited
	 * @return true if the use clicked OK, false otherwise
	 */
	public boolean showSongEditDialog(Song song) {
		try{
			//Load song edit dialog and create a new stage for it.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(SongLib.class.getResource("view/SongEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			//Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Song");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			//Set the song into the controller.
			SongEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setSong(song);

			//Show the dialog and wait until the user closes it.
			dialogStage.showAndWait();

			return controller.isOkClicked();

		} catch (IOException exceptionIO) {
			exceptionIO.printStackTrace();
			return false;
		}
	}

	/*
	 * Returns the main stage.
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
