package projet.iutlp.projet_objectifssportifs.Pages;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import projet.iutlp.projet_objectifssportifs.BddSqlite.ObjectifDB;
import projet.iutlp.projet_objectifssportifs.BddSqlite.SportDB;
import projet.iutlp.projet_objectifssportifs.ModelDB.Objectif;
import projet.iutlp.projet_objectifssportifs.ModelDB.Sport;
import projet.iutlp.projet_objectifssportifs.R;
import projet.iutlp.projet_objectifssportifs.Utilitaire.Uniformisation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;

/**
 * activity permettant de modifier/supprimer un objectif
 * on y accede en restant appuyé sur l'objectif sur l'ecran d'accueil
 */
public class ModifObjectif   extends AppCompatActivity {

    /**
     * methode onCreate appelé au lancement de l'activité
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modif_objectif);

        //Je recupere l'id de l'objectif a modifier/supprimer
        Intent intent=getIntent();
        Bundle b = intent.getExtras();
        final int idObjectif = (int)b.get("idObjectif");

        //Je recupere l'objectif de la BDD

        final Objectif objectif=ObjectifDB.getInstance(ModifObjectif.this).getObjectif(idObjectif);


        /*
         *Je gere le bouton de suppression d'objectif
         */
        Button buttonSupp=findViewById(R.id.buttonSupp);

        buttonSupp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new AlertDialog.Builder(ModifObjectif.this)
                        .setMessage(R.string.alerteSuppressionObjectif)
                        .setTitle(R.string.attention)
                        .setPositiveButton(R.string.ouiChef, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ObjectifDB.getInstance(view.getContext()).delete(idObjectif);
                                //On finit l'activité et on refresh la liste des objectifs
                                setResult(1);
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.non,null).show();
            }
        });


        /*
         *On recupere tous les sports
         */
        final ArrayList<Sport> listeSport=(ArrayList<Sport>) SportDB.getInstance(ModifObjectif.this).getSports();

        /*
         * On set le spinner des sports pratiqués par l'utilisateur et on recupere sa position dans la liste
         * car id de sport != position dans la liste (on peut supprimer des sports)
         */
        int position=0;
        final ArrayList<String> al=new ArrayList<>();
        for (Sport s:listeSport
        ) {
            if(s.getId()==objectif.getSport().getId()){//on cherche la place du sport dans la liste
                position=al.size();//On recupere la position
            }
            al.add(s.getNom());


        }
        final Spinner spinner=findViewById(R.id.spinner_sport);
        final ArrayAdapter<String> adapter =new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, al);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //on set le sport actuel pour l'objectif
        spinner.setSelection(position);

        //On set les boutons des dates
        //et les dates de l'objectifs avant modifications

        final FloatingActionButton bModifDateDeb=findViewById(R.id.modifierDateDeb);
        final DatePicker dpDebut=findViewById(R.id.dpDateDeb);
        dpDebut.init(objectif.getdDebut().getYear(),objectif.getdDebut().getMonthValue()-1,objectif.getdDebut().getDayOfMonth(),null);
        final Button bValideDateDeb=findViewById(R.id.bValideDateDeb);

        bModifDateDeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpDebut.setVisibility(View.VISIBLE);
                bValideDateDeb.setVisibility(View.VISIBLE);
                bModifDateDeb.setVisibility(View.GONE);


            }
        });

        final TextView txDateDeb=findViewById(R.id.viewDateDeb);
        txDateDeb.setText(Uniformisation.getDateUniforme(objectif.getdDebut()));
        bValideDateDeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dpDebut.setVisibility(View.GONE);
                bValideDateDeb.setVisibility(View.GONE);
                bModifDateDeb.setVisibility(View.VISIBLE);
                txDateDeb.setText(Uniformisation.getDateUniforme(LocalDate.of(dpDebut.getYear(),dpDebut.getMonth()+1,dpDebut.getDayOfMonth())));
            }
        });

        final TextView txDateFin=findViewById(R.id.viewDateFin);
        LocalDate dfinInit=objectif.getdFin();
        txDateFin.setText(Uniformisation.getDateUniforme(objectif.getdFin()));
        final FloatingActionButton bModifDateFin=findViewById(R.id.modifierDateFin);
        final DatePicker dpFin=findViewById(R.id.dpDateFin);
        dpFin.init(dfinInit.getYear(),dfinInit.getMonthValue()-1,dfinInit.getDayOfMonth(),null);

        dpFin.setMinDate(LocalDate.of(dpDebut.getYear(),dpDebut.getMonth()+1,dpDebut.getDayOfMonth()).getLong(ChronoField.EPOCH_DAY));
        final Button bValideDateFin=findViewById(R.id.bValideDateFin);
        bModifDateFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpFin.setVisibility(View.VISIBLE);
                bValideDateFin.setVisibility(View.VISIBLE);
                bModifDateFin.setVisibility(View.GONE);
            }
        });

        bValideDateFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpFin.setVisibility(View.GONE);
                bValideDateFin.setVisibility(View.GONE);
                bModifDateFin.setVisibility(View.VISIBLE);
                txDateFin.setText(Uniformisation.getDateUniforme(LocalDate.of(dpFin.getYear(),dpFin.getMonth()+1,dpFin.getDayOfMonth())));
            }
        });


        /*
         *On set les numberPicker pour le temps
         */
        final TextView titreTemps=findViewById(R.id.titreTemps);
        final NumberPicker npHours=findViewById(R.id.npTempsHours);
        npHours.setMinValue(0);
        npHours.setMaxValue(Integer.MAX_VALUE);
        npHours.setValue((int)objectif.getTemps().toHours());

        final NumberPicker npMinutes=findViewById(R.id.npTempsMinutes);
        npMinutes.setMinValue(0);
        npMinutes.setMaxValue(59);
        npMinutes.setValue((int)objectif.getTemps().toMinutes()%60);

        final TextView tvTemps=findViewById(R.id.txtHeures);
        final TextView tvMinutes=findViewById(R.id.txtMinutes);


        /*
         *On s'occupe des boutons pour set les distances
         * si l'utilisateur entre un caractere indérisable, on reset la distance à 50000 metres
         */
        final TextView titreDistance=findViewById(R.id.titreDistance);
        final Button bLessDistance=findViewById(R.id.bLessDistance);
        final Button bAddDistance=findViewById(R.id.bAddDistance);
        final EditText editTextDistance=findViewById(R.id.editDistance);

        editTextDistance.setText(""+objectif.getDistance());

        bLessDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    int distance= Integer.parseInt(editTextDistance.getText().toString());
                    distance=distance-1000;
                    editTextDistance.setText(""+distance);
                }
                catch (Exception e){
                    editTextDistance.setText("50000");
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
                    editTextDistance.setText("50000");
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
                    editTextDistance.setText("50000");
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
                    editTextDistance.setText("50000");
                }
                return true;
            }
        });

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
         *Bouton de modification de l'objectif
         */
        final Button bModifObjectif = findViewById(R.id.bModifObjectif);
        bModifObjectif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //On obtient les dates de l'objectif
                LocalDate dateDebut = LocalDate.of(dpDebut.getYear(),dpDebut.getMonth()+1,dpDebut.getDayOfMonth());
                LocalDate dateFin = LocalDate.of(dpFin.getYear(),dpFin.getMonth()+1,dpFin.getDayOfMonth());

                //On vérifie que les dates sont dans le bon ordre
                if(dateDebut.isAfter(dateFin)){
                    Snackbar.make(view,R.string.snackBarAlerteDate, BaseTransientBottomBar.LENGTH_SHORT).show();
                }
                else{
                    //On recupere le sport et on regarde s'il se mesure en distance
                    Sport sport=listeSport.get(spinner.getSelectedItemPosition());
                    //On initialise la distance a 0, c'est le mieux a faire pour la DB
                    //On verifira a la sortie si le sport est mesurable en distance a la sortie de la DB
                    int distance=0;
                    if(sport.isDistance()){
                        try{
                            distance= Integer.parseInt(editTextDistance.getText().toString());
                        }
                        catch (Exception e){
                            //Sinon on remet la distance à 50000 metres
                            Snackbar.make(view,R.string.snackBarAlerteDistance,BaseTransientBottomBar.LENGTH_SHORT).show();
                            editTextDistance.setText(R.string.distance_objectif);
                            distance=50000;
                        }

                    }
                    //On initialise le temps a 0 pour la DB
                    //On verifira a la sortie si le sport est mesurable en temps a la sortie de la DB
                    Duration temps=Duration.ofHours(0);
                    if(sport.isTemps()){
                        //Si le sport est mesurable en temps, on recupere le temps
                        temps= Duration.ofHours((long)npHours.getValue()).plusMinutes(npMinutes.getValue());
                    }

                    Log.e("TAG", "onClick: "+sport.getId() );
                    //On creer l'objectif
                    Objectif objectif=new Objectif(idObjectif,dateDebut,dateFin,distance,temps,false,sport);
                    if(ObjectifDB.getInstance(getApplicationContext()).modifObjectif(objectif)){
                        //Si c'est ok, on finit l'activité avec le result code == 1
                        //ne marche pas avec finishActivity(1)
                        setResult(1);
                        finish();
                    }
                }


            }
        });




    }




}
