package com.kovospace.bandzoneplayerunofficial.databases;

import android.content.Context;
import com.kovospace.bandzoneplayerunofficial.eventBus.ReloadBandsList;
import com.kovospace.bandzoneplayerunofficial.interfaces.BandProfileItem;
import com.kovospace.bandzoneplayerunofficial.objects.Band;
import com.kovospace.bandzoneplayerunofficial.objects.Track;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

public abstract class DbHelper {
    protected static Context context;
    protected static OfflineBandsRoomDatabase offlineBandsRoomDatabase;
    protected static OfflineTracksRoomDatabase offlineTracksRoomDatabase;

    public static void init(Context c) {
        context = c;
        offlineBandsRoomDatabase = OfflineBandsRoomDatabase.getInstance(context);
        offlineTracksRoomDatabase = OfflineTracksRoomDatabase.getInstance(context);
    }

    public static void rememberBandAndTracksForOffline(List<BandProfileItem> bandProfileItem) {
        boolean downloadedAtLeastOne = false;
        Band band = (Band) bandProfileItem.get(0);

        for (int i = 1; i < bandProfileItem.size(); i++) {
            if (((Track) bandProfileItem.get(i)).hasOfflineCopy()) {
                downloadedAtLeastOne = true;
                break;
            }
        }
        if (downloadedAtLeastOne) {
            BandsDbHelper.insertIfNotExist(band);
            for (int i = 1; i < bandProfileItem.size(); i++) {
                TracksDbHelper.insertBandTrackIfNotExist((Track) bandProfileItem.get(i));
            }
        } else {
            BandsDbHelper.delete(band.getSlug());
            TracksDbHelper.removeBandTracks(band);
            EventBus.getDefault().postSticky(new ReloadBandsList(true));
            System.out.println("------------ db remove all query");
        }
        // ^ do buducnosti rerobit, v niektorom vyuziti
        // tohoto adaptera bude moct byt viac kapiel mozno / nemusi byt pozicia 0
    }

    public static void deleteAllDatabases() {
        offlineBandsRoomDatabase.bandEntityDao().deleteAll();
        offlineTracksRoomDatabase.trackEntityDao().deleteAll();
    }
}
