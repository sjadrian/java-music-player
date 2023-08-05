package com.example.musicplayer;

import java.util.*;

public class GetAllSong {

    private final Map<String, Song> songResources = Map.of(
            "Kato - Unravel", new Song("Kato - Unravel", Objects.requireNonNull(getClass().getResource("/Kato-Unravel.mp3")).toExternalForm(), "01:56" ),
            "Kato - Gurenge", new Song("Kato - Gurenge", Objects.requireNonNull(getClass().getResource("/Kato-Gurenge.mp3")).toExternalForm(), "01:38" )
    );

    public Map<String, Song> fetchData() {
        return songResources;
    }
}
