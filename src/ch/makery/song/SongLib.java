package ch.makery.song;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import ch.makery.song.model.Song;
import ch.makery.song.model.SongListWrapper;
import ch.makery.song.view.RootLayoutController;
import ch.makery.song.view.SongEditDialogController;
import ch.makery.song.view.SongOverviewController;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;



public class SongLib extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;

		Callback<Song,Observable[]> cb =(Song song) -> new Observable[]{
	       song.songTitleProperty(),
	   };

	//The data as an observable list of Songs.
	private ObservableList<Song> songData = FXCollections.observableArrayList(cb);

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
			primaryStage.setScene(scene);

			//Give the controller access to the Song Library.
			RootLayoutController controller = loader.getController();
			controller.setSongLib(this);

			primaryStage.show();
		} catch (IOException exceptionIO) {
			exceptionIO.printStackTrace();
		}

		//Try to load last opened song file.
		File file = getSongFilePath();
		if(file != null) {
			loadSongDataFromFile(file);
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

	/*
	 * Returns the song file preference. Read from the OS specific registry.
	 * If no preference can be found, null is returned.
	 *
	 * @return
	 */
	public File getSongFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(SongLib.class);
		String filePath = prefs.get("filePath", null);
		if(filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}

	/*
	 * Sets the file path of the currently loaded file. The path is
	 * persisted in the OS specific registry.
	 *
	 * @param file the file or null to remove the path
	 */
	public void setSongFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(SongLib.class);
		if(file != null) {
			prefs.put("filePath", file.getPath());
			//Update the stage title.
			primaryStage.setTitle("Song Library: " + file.getName());
		} else {
			prefs.remove("filePath");
			//File path not found. Load new stage.
			//Update the stage title.
			primaryStage.setTitle("Song Library");
		}
	}

	/*
	 * Loads song data from the specified file. The current song data will
	 * be replaced.
	 *
	 * @param file
	 */
	public void loadSongDataFromFile(File file) {
		try{
			JAXBContext context = JAXBContext.newInstance(SongListWrapper.class);
			Unmarshaller um = context.createUnmarshaller();

			//Reading XML from the file and unmarshalling.
			SongListWrapper wrapper = (SongListWrapper) um.unmarshal(file);
			//Empty existing song data and update with new song data.
			songData.clear();
			songData.addAll(wrapper.getSongs());

			//Save the file path to the registry.
			setSongFilePath(file);

			//catch exception error
		} catch(Exception exception) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Loading error");
				alert.setHeaderText("Could not load data");
				alert.setContentText("Could not load data from file:\n" + file.getPath());

				alert.showAndWait();
		}
	}

	/*
	 * Saves the current song data to the specified file.
	 *
	 * @param file
	 */
	public void saveSongDataToFile(File file) {
		try{
			JAXBContext context = JAXBContext.newInstance(SongListWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			//Wrapping song data.
			SongListWrapper wrapper = new SongListWrapper();
			wrapper.setSongs(songData);

			//Marshalling and saving XML to the file.
			m.marshal(wrapper, file);

			//Save the file path to the registry.
			setSongFilePath(file);

		//catch exception errors
		} catch (Exception exception) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Save Error");
			alert.setHeaderText("Save unsuccessful");
			alert.setContentText("Save unsuccessful at file:\n" + file.getPath());

			alert.showAndWait();
		}
	}

}
