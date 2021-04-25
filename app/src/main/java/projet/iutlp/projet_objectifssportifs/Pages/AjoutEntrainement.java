package projet.iutlp.projet_objectifssportifs.Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import projet.iutlp.projet_objectifssportifs.BddSqlite.EntrainementDB;
import projet.iutlp.projet_objectifssportifs.BddSqlite.SportDB;
import projet.iutlp.projet_objectifssportifs.ModelDB.Entrainement;
import projet.iutlp.projet_objectifssportifs.ModelDB.Sport;
import projet.iutlp.projet_objectifssportifs.R;
import projet.iutlp.projet_objectifssportifs.Utilitaire.Uniformisation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Activité d'ajout d'entrainement, très proche de l'ajout d'objectif
 */
public class AjoutEntrainement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_entrainement);



        /*
         *On recupere les sports
         */
        final ArrayList<Sport> listeSport=(ArrayList<Sport>) SportDB.getInstance(AjoutEntrainement.this).getSports();

        /*
         * On set le spinner des sports pratiqués par l'utilisateur
         */
        final ArrayList<String> al=new ArrayList<>();
        for (Sport s:listeSport
        ) {
            al.add(s.getNom());

        }
        /*
        *Si l'utilisateur n'a pas encore declarer ses sports, on le redirige directement
         */
        if(al.size()==0){
            setResult(-1);
            finish();
        }
        final Spinner spinner=findViewById(R.id.spinner_sportEnt);
        final ArrayAdapter<String> adapter =new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, al);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        /*
        *On set les listener sur la date d'entrainement
         *On set la date de l'entrainement à aujourd'hui
         */
        final FloatingActionButton bModifDateEnt=findViewById(R.id.modifierDateEnt);
        final DatePicker dpEnt=findViewById(R.id.dpDateEnt);
        final Button bValideDateEnt=findViewById(R.id.bValideDateEnt);

        bModifDateEnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpEnt.setVisibility(View.VISIBLE);
                bValideDateEnt.setVisibility(View.VISIBLE);
                bModifDateEnt.setVisibility(View.GONE);


            }
        });


        final TextView txDateDeb=findViewById(R.id.viewDateEnt);
        txDateDeb.setText(Uniformisation.getDateUniforme(LocalDate.now()));
        bValideDateEnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dpEnt.setVisibility(View.GONE);
                bValideDateEnt.setVisibility(View.GONE);
                bModifDateEnt.setVisibility(View.VISIBLE);
                txDateDeb.setText(Uniformisation.getDateUniforme(LocalDate.of(dpEnt.getYear(),dpEnt.getMonth()+1,dpEnt.getDayOfMonth())));
            }
        });



        /*
         *On s'occupe des boutons pour set les distances
         * si l'utilisateur entre un caractere indérisable, on reset la distance à 10000 metres
         */
        final TextView titreDistance=findViewById(R.id.titreDistanceEnt);
        final Button bLessDistance=findViewById(R.id.bLessDistanceEnt);
        final Button bAddDistance=findViewById(R.id.bAddDistanceEnt);
        final EditText editTextDistance=findViewById(R.id.editDistanceEnt);

        bLessDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    int distance= Integer.parseInt(editTextDistance.getText().toString());
                    distance=distance-1000;
                    editTextDistance.setText(""+distance);
                }
                catch (Exception e){
                    editTextDistance.setText("10000");
                }
            }
        });


        bAddDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    int distance= Integer.parseInt(editTextDistance.getText().toString());
                    distance=distance+1000;
                    editTextDistance.setText(""+distance);
                }
                catch (Exception e){
                    editTextDistance.setText("10000");
                }
            }
        });

        bLessDistance.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                try{
                    int distance= Integer.parseInt(editTextDistance.getText().toString());
                    distance=distance-10000;
                    editTextDistance.setText(""+distance);
                }
                catch (Exception e){
                    editTextDistance.setText("10000");
                }
                return true;
            }
        });

        bAddDistance.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                try{
                    int distance= Integer.parseInt(editTextDistance.getText().toString());
                    distance=distance+10000;
                    editTextDistance.setText(""+distance);
                }
                catch (Exception e){
                    editTextDistance.setText("10000");
                }
                return true;
            }
        });




        /*
         *On set les numberPicker pour le temps
         */
        final TextView titreTemps=findViewById(R.id.titreTempsEnt);
        final NumberPicker npHours=findViewById(R.id.npTempsHoursEnt);
        npHours.setMinValue(0);
        npHours.setMaxValue(Integer.MAX_VALUE);
        npHours.setValue(1);

        final NumberPicker npMinutes=findViewById(R.id.npTempsMinutesEnt);
        npMinutes.setMinValue(0);
        npMinutes.setMaxValue(59);

        final TextView tvTemps=findViewById(R.id.txtHeuresEnt);
        final TextView tvMinutes=findViewById(R.id.txtMinutesEnt);





        /*
         *Listener sur le menu déroulant des sport
         * adapte l'affichage si un sport est mesurable en distance / temps
         */

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //On recherche le sport dans notre liste
                Sport sport=listeSport.get(i);
                if(!sport.isDistance()){
                    bLessDistance.setVisibility(View.GONE);
                    bAddDistance.setVisibility(View.GONE);
                    editTextDistance.setVisibility(View.GONE);
                    titreDistance.setVisibility(View.GONE);

                }
                else{
                    bLessDistance.setVisibility(View.VISIBLE);
                    bAddDistance.setVisibility(View.VISIBLE);
                    editTextDistance.setVisibility(View.VISIBLE);
                    titreDistance.setVisibility(View.VISIBLE);
                }

                if(!sport.isTemps()){
                    npHours.setVisibility(View.GONE);
                    npMinutes.setVisibility(View.GONE);
                    tvTemps.setVisibility(View.GONE);
                    tvMinutes.setVisibility(View.GONE);
                    titreTemps.setVisibility(View.GONE);
                }
                else{
                    npHours.setVisibility(View.VISIBLE);
                    npMinutes.setVisibility(View.VISIBLE);
                    tvTemps.setVisibility(View.VISIBLE);
                    tvMinutes.setVisibility(View.VISIBLE);
                    titreTemps.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*
         *Bouton d'ajout de l'entrainement
         */
        final Button bAjoutEnt = findViewById(R.id.bAjoutEnt);

        bAjoutEnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //On recupere le sport
                Sport sport=listeSport.get(spinner.getSelectedItemPosition());

                //On obtient la date de l'entrainement
                LocalDate dateEnt = LocalDate.of(dpEnt.getYear(),dpEnt.getMonth()+1,dpEnt.getDayOfMonth());
                //On initialise la distance a 0, c'est le mieux a faire pour la DB
                //On verifira a la sortie si le sport est mesurable en distance a la sortie de la DB
                int distance=0;
                if(sport.isDistance()){
                    try{
                        distance= Integer.parseInt(editTextDistance.getText().toString());
                    }
                    catch (Exception e){
                        //Sinon on remet la distance à 50000 metres
                        Snackbar.make(view,R.string.snackBarAlerteDistance, BaseTransientBottomBar.LENGTH_SHORT).show();
                        editTextDistance.setText("10000");
                        distance=10000;
                    }

                }

                //On initialise le temps a 0 pour la DB
                //On verifira a la sortie si le sport est mesurable en temps a la sortie de la DB
                Duration temps=Duration.ofHours(0);
                if(sport.isTemps()){
                    //Si le sport est mesurable en temps, on recupere le temps
                    temps=Duration.ofHours(npHours.getValue()).plusMinutes( npMinutes.getValue());
                }

                Entrainement entrainement= new Entrainement(dateEnt,distance,temps,sport);
                //On ajoute l'entrainement a la DB
                EntrainementDB.getInstance(getApplicationContext()).addEntraintement(entrainement);
                setResult(2);
                finish();
            }
        });







    }
}