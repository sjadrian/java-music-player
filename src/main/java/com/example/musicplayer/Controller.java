package com.example.musicplayer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


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

    String[] food = {"pizza", "sushi", "ramen", "ramen", "ramen", "ramen", "ramen", "ramen", "ramen", "ramen", "ramen", "ramen"};
    String currentFood;

    MediaPlayer player;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Play the initial song
//        String uriString = new File("src/main/resources/Kato-Unravel.mp3").toURI().toString();
//        String uriString = new File("C:\\Users\\stefa\\Programming\\Java\\Projects\\MusicPlayer\\src\\main\\resources\\Kato-Unravel.mp3").toURI().toString();
//        String uriString = Path.of("src/main/resources/Kato-Unravel.mp3").toUri().toString();
//        String uriString = Path.of("Kato-Unravel.mp3").toUri().toString();
        String uriString = Objects.requireNonNull(getClass().getResource("/Kato-Unravel.mp3")).toExternalForm();
        System.out.println(uriString);
        player = new MediaPlayer( new Media(uriString));
        player.play();

        // get all songs
        Map<String, Song> allSongs = new GetAllSong().fetchData();

        for (String title: allSongs.keySet()) {
            songListView.getItems().add(title);
        }

//        songListView.getItems().addAll(food);
        songListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                currentFood = songListView.getSelectionModel().getSelectedItem();
                songPlayingLabel.setText(currentFood);
            }
        });

        songListView.setOnMouseClicked((event)-> {
            player.stop();
            System.out.println("clicked on: " + songListView.getSelectionModel().getSelectedItem());

            Song selectedSong = allSongs.get(songListView.getSelectionModel().getSelectedItem());
            String mediaURI = selectedSong.getTitleURI();
            player = new MediaPlayer( new Media(mediaURI));
            player.play();
        });


    }
}