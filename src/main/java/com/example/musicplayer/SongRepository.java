package com.example.musicplayer;

import java.util.*;

public class SongRepository {

//    private final LinkedHashMap<String, Song> songResources = new LinkedHashMap<>();
    private final LinkedHashMap<String, Song> songResources = new LinkedHashMap<String, Song>() {{
        put("Kato - Unravel", new Song("Kato - Unravel", Objects.requireNonNull(getClass().getResource("/Kato-Unravel.mp3")).toExternalForm(), "01:56" ));
        put("Kato - Gurenge", new Song("Kato - Gurenge", Objects.requireNonNull(getClass().getResource("/Kato-Gurenge.mp3")).toExternalForm(), "01:38" ));
        put("Kato - Departure", new Song("Kato - Departure", Objects.requireNonNull(getClass().getResource("/Kato-Departure.mp3")).toExternalForm(), "01:46" ));
        put("Kato - A Cruel Angel-'s Thesis (Evangelion)", new Song("Kato - A Cruel Angel-'s Thesis (Evangelion)", Objects.requireNonNull(getClass().getResource("/Kato - A Cruel Angel-'s Thesis (Evangelion).mp3")).toExternalForm(), "01:50" ));
        put("Kato - Blue Bird", new Song("Kato - Blue Bird", Objects.requireNonNull(getClass().getResource("/Kato - Blue Bird.mp3")).toExternalForm(), "01:51" ));
        put("Kato - Leb Deinen Traum (Digimon)", new Song("Kato - Leb Deinen Traum (Digimon)", Objects.requireNonNull(getClass().getResource("/Kato - Leb Deinen Traum (Digimon).mp3")).toExternalForm(), "02:12" ));
        put("Kato - The Hero!! (One Punch Man)", new Song("Kato - The Hero!! (One Punch Man)", Objects.requireNonNull(getClass().getResource("/Kato - The Hero!! (One Punch Man).mp3")).toExternalForm(), "01:57" ));
        put("Kato - Pokémon Theme Lofi", new Song("Kato - Pokémon Theme Lofi", Objects.requireNonNull(getClass().getResource("/Kato - Pokémon Theme Lofi.mp3")).toExternalForm(), "01:20" ));
        put("Kato - Sparkle Lofi (Your Name)", new Song("Kato - Sparkle Lofi (Your Name)", Objects.requireNonNull(getClass().getResource("/Kato - Sparkle Lofi (Your Name).mp3")).toExternalForm(), "01:08" ));
        put("Kato - The Day Lofi (My Hero Academia)", new Song("Kato - The Day Lofi (My Hero Academia)", Objects.requireNonNull(getClass().getResource("/Kato - The Day Lofi (My Hero Academia).mp3")).toExternalForm(), "01:25" ));
        put("Kato - Inferno Lofi (Fire Force)", new Song("Kato - Inferno Lofi (Fire Force)", Objects.requireNonNull(getClass().getResource("/Kato - Inferno Lofi (Fire Force).mp3")).toExternalForm(), "01:17" ));
        put("Kato - We Are!", new Song("Kato - We Are!", Objects.requireNonNull(getClass().getResource("/Kato - We Are!.mp3")).toExternalForm(), "01:49" ));
    }};

    public LinkedHashMap<String, Song> fetchData() {
        return songResources;
    }
}
