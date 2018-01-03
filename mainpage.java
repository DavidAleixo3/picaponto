package com.example.dgale.picaponto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.R.id.list;

public class mainpage extends AppCompatActivity {

    private BeaconManager beaconManager;
    private BeaconRegion region;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {
            @Override
            public void onBeaconsDiscovered(BeaconRegion region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);
                    List<String> places = placesNearBeacon(nearestBeacon);
                    // TODO: update the UI here
                    Toast.makeText(mainpage.this, "Beacon found" + places, Toast.LENGTH_SHORT).show();
                    Log.d("Airport", "Nearest places: " + places);
                }
            }
        });
        /*region = new BeaconRegion("ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 24123, 41345);*/
        region = new BeaconRegion("ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 24123, 41345);


    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }
    @Override
    protected void onPause() {
        beaconManager.stopRanging(region);

        super.onPause();
    }

    private static final Map<String, List<String>> PLACES_BY_BEACONS;

    // TODO: replace "<major>:<minor>" strings to match your own beacons.
    static {
        Map<String, List<String>> placesByBeacons = new HashMap<>();
        placesByBeacons.put("11096:21492", new ArrayList<String>() {{ // Aqui tinha MINOR - MAJOR = 22504:48827
            add("Heavenly Sandwiches");
            // read as: "Heavenly Sandwiches" is closest
            // to the beacon with major 22504 and minor 48827
            add("Green & Green Salads");
            // "Green & Green Salads" is the next closest
            add("Mini Panini");
            // "Mini Panini" is the furthest away
        }});
        placesByBeacons.put("648:12", new ArrayList<String>() {{
            add("Mini Panini");
            add("Green & Green Salads");
            add("Heavenly Sandwiches");
        }});
        PLACES_BY_BEACONS = Collections.unmodifiableMap(placesByBeacons);
    }
    private List<String> placesNearBeacon(Beacon beacon) {
        String beaconKey = String.format("%d:%d", beacon.getMajor(), beacon.getMinor());
        if (PLACES_BY_BEACONS.containsKey(beaconKey)) {
            return PLACES_BY_BEACONS.get(beaconKey);
        }
        return Collections.emptyList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mainpage, menu);
        return true;
    }
}
