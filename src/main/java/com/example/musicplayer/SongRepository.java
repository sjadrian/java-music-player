package com.example.musicplayer;

import java.util.*;

public class SongRepository {

//    private final LinkedHashMap<String, Song> songResources = new LinkedHashMap<>();
    private final LinkedHashMap<String, Song> songResources = new LinkedHashMap<String, Song>() {{
        put("Kato - Unravel", new Song("Kato - Unravel", Objects.requireNonNull(getClass().getResource("/Kato-Unravel.mp3")).toExternalForm(), "01:56" ));
        put("Kato - Gurenge", new Song("Kato - Gurenge", Objects.requireNonNull(getClass().getResource("/Kato-Gurenge.mp3")).toExternalForm(), "01:38" ));
        put("Kato - Departure", new Song("Kato - Departure", Objects.requireNonNull(getClass().getResource("/Kato-Departure.mp3")).toExternalForm(), "01:46" ));
        put("Kato - A Cruel Angel-'s Thesis (Evangelion)", new Song("Kato - A Cruel Angel-'s Thesis (Evangelion)", Objects.requireNonNull(getClass().getResource("/Kato - A Cruel Angel-'s Thesis (Evangelion).mp3")).toExternalForm(), "01:50" ));
    }};

    public LinkedHashMap<String, Song> fetchData() {
        return songResources;
    }
}
