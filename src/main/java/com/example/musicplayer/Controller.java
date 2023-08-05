package com.example.musicplayer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Controller implements Initializable {

    @FXML
    private Label nowPlayingLabel;
    @FXML
    private Label songPlayingLabel;
    @FXML
    private Label nextUpLabel;
    @FXML
    private Label nextSongLabel;
    @FXML
    private ListView<String> songListView;
    @FXML
    private Label welcomeText;
    @FXML
    private TableView<Song> table;
    @FXML
    private TableColumn<Song, String> titleColumn;
    @FXML
    private TableColumn<Song, String> lengthColumn;
    @FXML
    private Button playPauseButton;
    @FXML
    private Button previousButton;

    MediaPlayer player;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private boolean isPlaying = true;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        logger.info("Starting the program...");

        // get all songs
        LinkedHashMap<String, Song> allSongs = new SongRepository().fetchData();

        // initialize table view
        List<Song> songsList = allSongs.values().stream().toList();
        ObservableList<Song> Songs = FXCollections.observableArrayList();
        Songs.addAll(songsList);

        titleColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("title"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("length"));
        table.setItems(Songs);

        table.setOnMouseClicked((mouseEvent -> {

            // check valid song
            Song selectedSong = table.getSelectionModel().getSelectedItem();

            if (selectedSong != null) {
                player.stop();

                String mediaURI = selectedSong.getTitleURI();
                player = new MediaPlayer( new Media(mediaURI));
                player.play();
                logger.info("clicked on: " + selectedSong.getTitle());
                logger.info("playing: " + selectedSong.getTitle());

                songPlayingLabel.setText(selectedSong.getTitle());
            }
        }));

        // Play the initial song
//        String uriString = new File("src/main/resources/Kato-Unravel.mp3").toURI().toString();
//        String uriString = new File("C:\\Users\\stefa\\Programming\\Java\\Projects\\MusicPlayer\\src\\main\\resources\\Kato-Unravel.mp3").toURI().toString();
//        String uriString = Path.of("src/main/resources/Kato-Unravel.mp3").toUri().toString();
//        String uriString = Path.of("Kato-Unravel.mp3").toUri().toString();

        Song firstSong = allSongs.values().stream().findFirst().orElse(null);
        String uriString = Objects.requireNonNull(firstSong).getTitleURI();
        logger.info("Playing 1st song: " + firstSong.getTitle());
        player = new MediaPlayer( new Media(uriString));
        player.play();

        songPlayingLabel.setText(firstSong.getTitle());
        nextSongLabel.setText(songsList.get(songsList.indexOf(firstSong) + 1).getTitle());
        logger.info("first song index: " + songsList.indexOf(firstSong));


        playButton();
        previousButton();
    }

    private void previousButton() {
        previousButton.setOnMouseClicked((mouseEvent -> {
            logger.info("previous button clicked");


            Song selectedSong = table.getSelectionModel().getSelectedItem();

            player.stop();

            String mediaURI = selectedSong.getTitleURI();
            player = new MediaPlayer( new Media(mediaURI));
            player.play();
            logger.info("playing: " + selectedSong.getTitle());


        }));
    }

    private void playButton() {
        playPauseButton.setOnMouseClicked((mouseEvent -> {
            logger.info("play/pause button clicked");

            isPlaying = !isPlaying;

            if (!isPlaying) {
                player.pause();
                logger.info("song is paused");
            } else {
                player.play();
                logger.info("song is resumed");
            }
        }));
    }
}