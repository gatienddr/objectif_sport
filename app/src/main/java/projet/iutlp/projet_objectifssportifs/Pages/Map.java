package projet.iutlp.projet_objectifssportifs.Pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import projet.iutlp.projet_objectifssportifs.BddSqlite.GeoPointDB;
import projet.iutlp.projet_objectifssportifs.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

/**
 * class affichant la map du parcour utilisateur lors d'un entrainement GPS
 */
public class Map extends AppCompatActivity {

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Je recupere l'id de l'entrainement
        Intent intent=getIntent();
        Bundle b = intent.getExtras();
        final int idEntrainement = (int)b.get("idEntrainement");

        //je recupere le parcour de l'utilisateur
        ArrayList<GeoPoint> parcour=GeoPointDB.getInstance(Map.this).getParcour(idEntrainement);



        /*
        *Je mets en place la map comme dans le tuto OSMdroid
        */
        Context ctx = getApplicationContext();
        /*
        *preference Manager est deprécié , j'ai pas trouvé d'alternative j'avoue
         */
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_map);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);


        /*
        *Pour utilser OSMdroid, on doit demander les droit de storage externe
         */
        requestPermissionsIfNecessary(new String[] {
                // WRITE_EXTERNAL_STORAGE is required in order to show the map
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });


        map.setVisibility(View.VISIBLE);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(15.0);

        //j'affiche la map sur le point de départ dur parcour si il existe
        //sinon sur Nancy la plus belle ville au monde ;)
        GeoPoint startPoint;
        if(parcour.size()>0){
            startPoint=parcour.get(0);
            //puis je trace le parcout

            Polyline line = new Polyline();   //see note below!
            line.setPoints(parcour);
            map.getOverlayManager().add(line);
        }
        else{
            startPoint = new GeoPoint(48.6920, 6.1844);
        }

        mapController.setCenter(startPoint);





    }

    /**
     * methode onResume, indispensable pour utiliser la map
     */
    @Override
    public void onResume() {
        super.onResume();
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    /**
     * methode onPause, indispensable pour utiliser la map
     */
    @Override
    public void onPause() {
        super.onPause();
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    /**
     * metode demandant les droits, indispensable pour utiliser la map osmdroid
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * methode verifiant que l'utilisateur a accepté les droits du storage externes
     * @param permissions
     */
    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}