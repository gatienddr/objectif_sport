package projet.iutlp.projet_objectifssportifs.Pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import projet.iutlp.projet_objectifssportifs.BddSqlite.EntrainementDB;
import projet.iutlp.projet_objectifssportifs.BddSqlite.GeoPointDB;
import projet.iutlp.projet_objectifssportifs.BddSqlite.SportDB;
import projet.iutlp.projet_objectifssportifs.ModelDB.Entrainement;
import projet.iutlp.projet_objectifssportifs.ModelDB.Sport;
import projet.iutlp.projet_objectifssportifs.R;
import projet.iutlp.projet_objectifssportifs.Utilitaire.Uniformisation;

import org.osmdroid.util.GeoPoint;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * activity entrainementGps
 * mesurant en temps réel l'entrainement d'un utilisateur grâce au GPS
 */
public class EntrainementGps extends AppCompatActivity implements LocationListener{

    /**
     * l'id de la permission
     * utilisé pour la localisation GPS
     */
    public static final int PERMISSION_ID=1;

    /**
     * la text view de l'altitude de l'utilisateur
     */
    private TextView textViewAlitude;
    /**
     * la text view de la vitesse de l'utilisateur
     */
    private TextView textViewVitesse;
    /**
     * la text view affichant le chronomètre à l'utilisateur
     */
    private TextView textViewChrono;
    /**
     * le temps effectué par l'utilisateur
     */
    private long temps;

    /**
     * La distance effectué par l'utilisateur
     * mesuré à l'aide du GPS
     */
    public int distance;

    /**
     * le temps en miliseconde au début de l'entrainement
     */
     private long tempsMilliNow;
    /**
     * le temps en milliseconde lorsque l'utilisateur arrete l'entrainement
     */
    private long tempsMiliStop;

    /**
     * le boolean running
     *          true si l'utilisateur a lancé son entrainement avec le switch
     *             false sinon
     *             est utile dans la gestion du thread destiné à affiché le chronomètre à l'utilisateur
     */
    private boolean running;

    /**
     * La derniere localisation de l'utilisateur
     * utile pour calculer la distance
     */
   private Location lastLocation;

    /**
     * La liste des noms des sports pratiqués
     */
    private ArrayList<String> listeSport;


    /**
     * la liste des position de l'utilisateur (pour refaire le tracé du parcour sur la map)
     */
    ArrayList<GeoPoint> listePosition;




    /**
     * metod onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrainement_gps);

        /*
         *On recupere les sports
         */
        final ArrayList<Sport> listeSportBase=(ArrayList<Sport>) SportDB.getInstance(EntrainementGps.this).getSports();

        /*
         * On set le spinner des sports pratiqués par l'utilisateur
         */
        this.listeSport=new ArrayList<>();
        for (Sport s:listeSportBase
        ) {
            this.listeSport.add(s.getNom());

        }
        /*
         *Si l'utilisateur n'a pas encore declarer ses sports, on le redirige directement
         */
        if(listeSportBase.size()==0){
            setResult(-1);
            finish();
        }


        /*
        *Le chrono n'est pas en marche lorsque l'utilisateur arrive sur l'activirty
        */
        this.running=false;
        /*
         *On set la distance à 0
         */
        this.distance=0;

        /*
        *On recupere les textviews
         */
        this.textViewAlitude=findViewById(R.id.txtViewAltitude);
        this.textViewVitesse=findViewById(R.id.textViewVitesse);
        this.textViewChrono=findViewById(R.id.textViewChrono);

        /*
        *On initialise le temps a 0
         */
        this.temps=0;

        /*
         *on initialise la liste des positions
         */
        this.listePosition=new ArrayList<>();





        //On recupere le switch button de lancement d'activité
        SwipeButton swipeButton=(SwipeButton)findViewById(R.id.swipeEntGps);

        //Listener sur le changement d'etat du switch button
        swipeButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                running=active;
                //Si l'entrainement est lancé
                if(active){
                    //on recupere l'heure du lancement en milliseconde
                    tempsMilliNow =System.currentTimeMillis();
                    //On lance le chrono visuel pour l'utilisateur
                    startChrono(true);

                }
                //Si l'utilisateur arrete l'entrainement
                if(!active){
                    //on arrete le chrono visuel
                    startChrono(false);
                    //on recupere le moment ou l'entrainement est arreté
                    tempsMiliStop= System.currentTimeMillis();
                    temps=(tempsMiliStop-tempsMilliNow);

                    finishEntGps();




                }
            }
        });

        //On lance le suivi GPS
        startTracking();
    }


    /**
     * methode onLocationChange, utilisé lorsque l'utilisateur se déplace
     * @param location
     *          la nouvel location (position, altitude, vitesse etc)
     */
    @Override
    public void onLocationChanged(@NonNull Location location) {

        //On vérifie qu'elle est différent de null
        if(location!=null){

            //On set les vue de vitesse et d'altitude
            textViewAlitude.setText("Altitude : "+location.getAltitude());
            textViewVitesse.setText("Vitesse : "+location.getSpeed()*3.6+" Km/h");

            //on calcule la distance parcourru
            calculateDistance(location);

            //On garde la position en mémoire
            this.listePosition.add(new GeoPoint(location.getLongitude(),location.getLatitude()));

        }

        

    }

    /*
    *Methode implémenter de LocationListener pas utile pour notre cas
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }


    /**
     * methode startTracking
     * gérant le déplacement de l'utilisateur
     */
    public void startTracking(){
        //On vérifie d'avoir les permissions avant d'utiliser le gps
        if(ActivityCompat.checkSelfPermission(EntrainementGps.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED &&
                (ActivityCompat.checkSelfPermission(EntrainementGps.this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED) ){
            LocationManager locationManager=(LocationManager)getApplicationContext().getSystemService(EntrainementGps.this.LOCATION_SERVICE);

            //On verifie que l'utilisateur a le gps activé
            boolean gps_enabled = false;
            boolean network_enabled = false;

            try {
                gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch(Exception ex) {}

            try {
                network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch(Exception ex) {}

            //On lui indique sinon
            if(!gps_enabled && !network_enabled) {

                final Dialog dialog = new AlertDialog.Builder(EntrainementGps.this)
                        .setMessage(R.string.alerteGpsPasActif)
                        .setTitle(R.string.titreAlerteGpsPasActif)
                        .setPositiveButton(R.string.ouiChef,null).create();
                dialog.show();

            }

            //on parametre le suivi gps
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,100,1, this);
            //et on le lance
            onLocationChanged(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        }
        //si on a pas les permissions, on les demande
        else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_ID);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_ID);
        }
    }

    /**
     * methode onRequestResult
     * @param requestCode
     *             le request code
     * @param permissions
     *              la permission
     * @param grantResults
     *              le grand result
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //On verifie que les demande de droit ont été accepté par l'utilisateur
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSION_ID){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Si tout est bon, on lance le suivi
                startTracking();
            }
        }
    }


    /**
     *  * methode start chrono
     *      * affiche le chronometre à l'utilisateur
     *      * fonctionne de méthode asynchrome, ne reflete pas le reel chronometre que l'on mettra dans la DB (avec getMilis)
     *      * mais cette imprécisione est trop petite pour être visible par l'utilisateur
     * @param go
     *          si true alors on lance le chrono
     *          sinon on l'arrete
     */
    public void startChrono(boolean go){

        Thread t=new Thread(){
            @Override
            public void run() {
                super.run();

                try {
                    //On itere de 1 toutes les secondes la variables sec
                    int sec=0;
                    while (running){

                        //Obligé de mettre en final donc de les redéclarer à chaque fois
                        final int secFinal=sec;
                        final int minuteFinal=sec/60;
                        final int heureFinal=minuteFinal/60;

                        //on lance une 2eme tache pour pouvoir acceder à l'UI avec la methode runOnUiThread
                        //(on ne peut pas mettre le sleep et la modification de l'ui dans le meme thread en Android)
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //et on l'affiche à l'utilisateur
                                textViewChrono.setText(heureFinal+":"+minuteFinal+":"+secFinal);
                                textViewChrono.setText(Uniformisation.getTimeUniforme(Duration.ofSeconds(secFinal)));
                            }
                        });

                        sec++;


                        sleep(1000);
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        if(go){
            t.start();
        }
        if(!go){
            t.interrupt();
        }

    }

    /**
     * methode calculant la distance entre la nouvelle position et l'ancienne si elle non null
     * @param newLocation
     *              la nouvelle location
     */
    public void calculateDistance(Location newLocation){
        if(this.lastLocation!=null){
            this.distance+=newLocation.distanceTo(lastLocation);
            TextView txtViewDistance=findViewById(R.id.textViewDistance);
            txtViewDistance.setText("Distance parcourue : "+this.distance);
        }
        lastLocation=newLocation;

    }

    public void finishEntGps(){
        /*
         *On initialise le spinner de sport que l'on affiche a la fin de l'ent
         */
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.finish_ent_gps, null, false);


        final Spinner spinner=view.findViewById(R.id.spinner_sportEntGPS);
        final ArrayAdapter<String> adapter =new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, this.listeSport);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);




        //les texts view aussi
        final TextView txtViewDistanceGPS=(TextView)view.findViewById(R.id.txtViewDistanceGPS);
        final TextView txtViewTempsGPS=view.findViewById(R.id.txtViewTempsGPS);

        //txtViewDistanceGPS.setText("Distance totale effectuée : " +this.distance);
        txtViewDistanceGPS.setText("Distance totale effectuée : "+this.distance);
        txtViewTempsGPS.setText("Temps total effectué : "+Uniformisation.getTimeUniforme(Duration.ofMillis(this.temps)));

        //On set si on affiche la distance et/ou le temps
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!SportDB.getInstance(EntrainementGps.this).getSport(i+1).isDistance())txtViewDistanceGPS.setVisibility(View.GONE);
                if(!SportDB.getInstance(EntrainementGps.this).getSport(i+1).isTemps())txtViewTempsGPS.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


       final AlertDialog alertDialog=new AlertDialog.Builder(EntrainementGps.this)
                .setView(view)
                .show();

        //On set le bouton de validaiton
        Button bValideEntGps=view.findViewById(R.id.buttonValideEntGPS);
        bValideEntGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //On recupere le sport selon la valeur du spinner
                Sport sport=SportDB.getInstance(EntrainementGps.this).getSport(spinner.getSelectedItemPosition()+1);
                //On créer et on ajoute l'entrainement à la DB
                Entrainement ent=new Entrainement(LocalDate.now(), distance, Duration.ofMillis(temps), sport);
                long idEntrainement=EntrainementDB.getInstance(EntrainementGps.this).addEntraintement(ent);
                //on ajout le tracé gps
                if(sport.isDistance()){
                    GeoPointDB.getInstance(EntrainementGps.this).add(listePosition,idEntrainement);
                }
                alertDialog.cancel();
                //On set le result adequat pour afficher les entrainements et on finit l'activité
                EntrainementGps.this.setResult(2);
                EntrainementGps.this.finish();
            }
        });

    }



}