package com.example.musicplayer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import javafx.util.Duration;
import java.util.*;
import java.util.stream.Collectors;

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

    MediaPlayer player;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private boolean isPlaying = true;
    private Song currentlyPlaying;
    private boolean isLooping = false;

    // get all songs
    LinkedHashMap<String, Song> allSongs = new SongRepository().fetchData();
//    List<Song> songsList = allSongs.values().stream().toList();
//    ArrayList<Song> songsList = allSongs.values().stream().collect(Collectors.toCollection(ArrayList::new));
    ArrayList<Song> songsList = new ArrayList<>(allSongs.values());

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        logger.info("Starting the program...");

        // initialize table view
        ObservableList<Song> Songs = FXCollections.observableArrayList();
        Songs.addAll(songsList);

        titleColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("title"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("length"));
        table.setItems(Songs);
        table.getStylesheets().add(getClass().getResource("/modena.css").toExternalForm());

        table.setOnMouseClicked((mouseEvent -> {

            // check valid song
            Song selectedSong = table.getSelectionModel().getSelectedItem();

            if (selectedSong != null) {
                logger.info("clicked on: " + selectedSong.getTitle());

                player.stop();
                playMedia(selectedSong);
            }
        }));

        // Play the initial song
//        String uriString = new File("src/main/resources/Kato-Unravel.mp3").toURI().toString();
//        String uriString = new File("C:\\Users\\stefa\\Programming\\Java\\Projects\\MusicPlayer\\src\\main\\resources\\Kato-Unravel.mp3").toURI().toString();
//        String uriString = Path.of("src/main/resources/Kato-Unravel.mp3").toUri().toString();
//        String uriString = Path.of("Kato-Unravel.mp3").toUri().toString();

        currentlyPlaying  = allSongs.values().stream().findFirst().orElse(null);
        playMedia(currentlyPlaying);

        playButton();
        previousButton();
        nextButton();
        loopButton();
        shuffleButton();
    }

    private void playMedia(Song currentSong) {
        currentlyPlaying  = currentSong;
        String uriString = Objects.requireNonNull(currentlyPlaying).getTitleURI();

        player = new MediaPlayer(new Media(uriString));
        player.setAutoPlay(true);
        player.setOnReady(() -> {
            initialSlider();
        });
        player.currentTimeProperty().addListener((observableValue, duration, t1) -> {
            if (!slider.isValueChanging()) {
                double current = player.getCurrentTime().toSeconds();
                slider.setValue(current);
                timeElapsedLabel.setText(convertTime(current));
                timeRemainingLabel.setText(convertTime(player.getMedia().getDuration().toSeconds() - current));
            }
        });
        player.setOnEndOfMedia(()->{
            if (!isLooping) {
                player.dispose();

                currentlyPlaying = (songsList.indexOf(currentlyPlaying) + 1 == songsList.size()) ? songsList.get(0): songsList.get(songsList.indexOf(currentlyPlaying) + 1);
                updatePlayingLabel();

                playMedia(currentlyPlaying);
            }
        });
        player.play();
        logger.info("Playing: " + currentlyPlaying.getTitle());
        updatePlayingLabel();
    }

    private String convertTime(double second) {
        int minutes = (int) (second/60);
        int seconds = (int) (second%60);

        return String.format("%02d:%02d",minutes,seconds);
    }

    private void initialSlider() {
        logger.info("Initializing slider");
        slider.setMin(0);
        slider.setMax(player.getTotalDuration().toSeconds());

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if (slider.isValueChanging()) {
                    logger.info("slider changed");
                    int current = (int) slider.getValue();
                    logger.info("value: " + current);
                    player.seek(Duration.seconds(current));
                    timeElapsedLabel.setText(convertTime(current));
                    timeRemainingLabel.setText(convertTime(player.getMedia().getDuration().toSeconds() - current));
                }
            }
        });
    }

    private void shuffleButton() {
        shuffleButton.setOnMouseClicked((mouseEvent -> {
            Collections.shuffle(songsList);

            ObservableList<Song> Songs = FXCollections.observableArrayList();
            Songs.addAll(songsList);

            table.setItems(Songs);
            updatePlayingLabel();
        }));
    }

    private void loopButton() {
        loopButton.setOnMouseClicked((mouseEvent -> {
            logger.info("loop button clicked");

            isLooping = !isLooping;
            logger.info("isLooping: " + isLooping);

            if (isLooping) {
                player.setCycleCount(MediaPlayer.INDEFINITE);
                loopButton.setText("Loop: ON");
            } else {
                loopButton.setText("Loop: OFF");
            }
        }));
    }

    private void nextButton() {
        nextButton.setOnMouseClicked((mouseEvent -> {
            logger.info("next button clicked");

            currentlyPlaying = (songsList.indexOf(currentlyPlaying) + 1 == songsList.size()) ? songsList.get(0): songsList.get(songsList.indexOf(currentlyPlaying) + 1);
            player.stop();
            player.dispose();

            playMedia(currentlyPlaying);
        }));
    }

    private void previousButton() {
        previousButton.setOnMouseClicked((mouseEvent -> {
            logger.info("previous button clicked");

            currentlyPlaying = (songsList.indexOf(currentlyPlaying) - 1 < 0) ? songsList.get(songsList.size() - 1) : songsList.get(songsList.indexOf(currentlyPlaying) - 1);
            player.stop();
            player.dispose();

            playMedia(currentlyPlaying);
        }));
    }

    private void updatePlayingLabel() {
        songPlayingLabel.setText(currentlyPlaying.getTitle());
        int nextSongIndex = (songsList.indexOf(currentlyPlaying) + 1 == songsList.size()) ? 0 : songsList.indexOf(currentlyPlaying) + 1;
        nextSongLabel.setText(songsList.get(nextSongIndex).getTitle());

        logger.info("currently playing: " + currentlyPlaying.getTitle());
        logger.info("next up: " + songsList.get(nextSongIndex).getTitle());
    }

    private void playButton() {
        playPauseButton.setOnMouseClicked((mouseEvent -> {
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
        }));
    }
}