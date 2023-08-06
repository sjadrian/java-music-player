package com.example.musicplayer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import javafx.util.Duration;
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
    private TableView<Song> table;
    @FXML
    private TableColumn<Song, String> titleColumn;
    @FXML
    private TableColumn<Song, String> lengthColumn;
    @FXML
    private Button playPauseButton;
    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button loopButton;
    @FXML
    private Button shuffleButton;
    @FXML
    private Label timeElapsedLabel;
    @FXML
    private Label timeRemainingLabel;
    @FXML
    private Slider slider;

    private MediaPlayer player;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private boolean isPlaying = true;
    private Song currentlyPlayingSong;
    private boolean isLooping = false;

    // get all songs
    LinkedHashMap<String, Song> allSongs = new SongRepository().fetchData();
//    List<Song> songsList = allSongs.values().stream().toList();
//    ArrayList<Song> songsList = allSongs.values().stream().collect(Collectors.toCollection(ArrayList::new));
    ArrayList<Song> songsList = new ArrayList<>(allSongs.values());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        logger.info("Starting the program...");

        // initialize table view
        ObservableList<Song> Songs = FXCollections.observableArrayList();
        Songs.addAll(songsList);

        titleColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("title"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("length"));
        table.setItems(Songs);
        table.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        // Play the first song
        currentlyPlayingSong = Songs.iterator().next();
        playMedia(currentlyPlayingSong);

        // Initialize controls
        playPauseButton.setOnMouseClicked(mouseEvent -> playPauseFunction());
        previousButton.setOnMouseClicked(mouseEvent -> previousFunction());
        nextButton.setOnMouseClicked(mouseEvent -> nextFunction());
        loopButton.setOnMouseClicked(mouseEvent -> loopFunction());
        shuffleButton.setOnMouseClicked(mouseEvent -> shuffleFunction());
        table.setOnMouseClicked((mouseEvent -> tableFunction()));
    }

    private void playMedia(Song currentSong) {
        currentlyPlayingSong = currentSong;

        // setup media player
        player = new MediaPlayer(new Media(currentlyPlayingSong.getTitleURI()));
        player.setAutoPlay(true);
        player.setOnReady(this::initializeSlider);
        player.currentTimeProperty().addListener((observableValue, duration, t1) -> {
            if (!slider.isValueChanging()) {
                double currentMediaTimeInSeconds = player.getCurrentTime().toSeconds();
                slider.setValue(currentMediaTimeInSeconds);
                timeElapsedLabel.setText(convertAndFormatTime(currentMediaTimeInSeconds));
                timeRemainingLabel.setText(convertAndFormatTime(player.getMedia().getDuration().toSeconds() - currentMediaTimeInSeconds));
            }
        });
        player.setOnEndOfMedia(()->{
            if (!isLooping) {
                player.dispose();

                currentlyPlayingSong = (songsList.indexOf(currentlyPlayingSong) + 1 == songsList.size()) ? songsList.get(0): songsList.get(songsList.indexOf(currentlyPlayingSong) + 1);
                updateCurrentlyAndNextPlayingLabels();

                playMedia(currentlyPlayingSong);
            }
        });

        // play the media
        player.play();
        logger.info("Playing: " + currentlyPlayingSong.getTitle());
        updateCurrentlyAndNextPlayingLabels();
    }

    private String convertAndFormatTime(double second) {
        int minutes = (int) (second/60);
        int seconds = (int) (second%60);

        return String.format("%02d:%02d",minutes,seconds);
    }

    private void initializeSlider() {
        logger.info("Initializing slider for: " + currentlyPlayingSong.getTitle());
        slider.setMin(0);
        slider.setMax(player.getTotalDuration().toSeconds());

        slider.valueProperty().addListener((observableValue, number, t1) -> {
            if (slider.isValueChanging()) {
                logger.info("slider changed");
                int currentMediaTimeInSeconds = (int) slider.getValue();
                logger.info("value: " + currentMediaTimeInSeconds);
                player.seek(Duration.seconds(currentMediaTimeInSeconds));
                timeElapsedLabel.setText(convertAndFormatTime(currentMediaTimeInSeconds));
                timeRemainingLabel.setText(convertAndFormatTime(player.getMedia().getDuration().toSeconds() - currentMediaTimeInSeconds));
            }
        });
    }

    private void tableFunction() {
        Song selectedSong = table.getSelectionModel().getSelectedItem();

        // check the clicked song is not null
        if (selectedSong != null) {
            logger.info("clicked on: " + selectedSong.getTitle());

            player.stop();
            playMedia(selectedSong);
        }
    }

    private void shuffleFunction() {
        Collections.shuffle(songsList);

        ObservableList<Song> Songs = FXCollections.observableArrayList();
        Songs.addAll(songsList);

        table.setItems(Songs);
        updateCurrentlyAndNextPlayingLabels();
    }

    private void loopFunction() {
        logger.info("loop button clicked");

        isLooping = !isLooping;
        logger.info("isLooping: " + isLooping);

        if (isLooping) {
            player.setCycleCount(MediaPlayer.INDEFINITE);
            loopButton.setText("Loop: ON");
        } else {
            loopButton.setText("Loop: OFF");
        }
    }

    private void nextFunction() {
        logger.info("next button clicked");

        currentlyPlayingSong = (songsList.indexOf(currentlyPlayingSong) + 1 == songsList.size()) ? songsList.get(0): songsList.get(songsList.indexOf(currentlyPlayingSong) + 1);
        player.stop();
        player.dispose();

        playMedia(currentlyPlayingSong);
    }

    private void previousFunction() {
        logger.info("previous button clicked");

        currentlyPlayingSong = (songsList.indexOf(currentlyPlayingSong) - 1 < 0) ? songsList.get(songsList.size() - 1) : songsList.get(songsList.indexOf(currentlyPlayingSong) - 1);
        player.stop();
        player.dispose();

        playMedia(currentlyPlayingSong);
    }

    private void updateCurrentlyAndNextPlayingLabels() {
        songPlayingLabel.setText(currentlyPlayingSong.getTitle());
        int nextSongIndex = (songsList.indexOf(currentlyPlayingSong) + 1 == songsList.size()) ? 0 : songsList.indexOf(currentlyPlayingSong) + 1;
        nextSongLabel.setText(songsList.get(nextSongIndex).getTitle());

        logger.info("currently playing: " + currentlyPlayingSong.getTitle());
        logger.info("next up: " + songsList.get(nextSongIndex).getTitle());
    }

    private void playPauseFunction() {
        logger.info("play/pause button clicked");

        isPlaying = !isPlaying;

        if (!isPlaying) {
            player.pause();
            playPauseButton.setText("Play");

            logger.info("song is paused");
        } else {
            player.play();
            playPauseButton.setText("Pause");
            logger.info("song is resumed");
        }
    }
}