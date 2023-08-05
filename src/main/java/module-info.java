module com.example.musicplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires org.slf4j;

    opens com.example.musicplayer to javafx.fxml;
    exports com.example.musicplayer;
}