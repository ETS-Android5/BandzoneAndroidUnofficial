package com.kovospace.bandzoneplayerunofficial.databases;

import android.app.Activity;
import android.content.Context;
import com.kovospace.bandzoneplayerunofficial.objects.Band;
import com.kovospace.bandzoneplayerunofficial.objects.Track;

import java.util.List;

public class TracksDbHelper {
    private static Context context;
    private static TrackEntity trackEntity;
    private static OfflineTracksRoomDatabase offlineTracksRoomDatabase;

    public static void init(Context c) {
        context = c;
        offlineTracksRoomDatabase = OfflineTracksRoomDatabase.getInstance((Activity) context);
    }

    public static void insertBandTrackIfNotExist(Track track) {
        if (track != null && track.getBandSlug() != null && track.getTitle() != null) {
            trackEntity = new TrackEntity(track);
            List<TrackEntity> found = offlineTracksRoomDatabase.trackEntityDao().findByBand(trackEntity.getBandSlug());
            if (found.size() == 0) {
                offlineTracksRoomDatabase.trackEntityDao().insert(trackEntity);
            }
        }
    }

    public static void removeBandTracks(Band band) {
        offlineTracksRoomDatabase.trackEntityDao().deleteByBand(band.getSlug());
    }

    public static List<TrackEntity> getAll() {
        return  offlineTracksRoomDatabase.trackEntityDao().getAll();
    }

    public static List<TrackEntity> getBandTracks(Band band) {
        return offlineTracksRoomDatabase.trackEntityDao().findByBand(band.getSlug());
    }

}