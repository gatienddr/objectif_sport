package projet.iutlp.projet_objectifssportifs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;


import projet.iutlp.projet_objectifssportifs.Adapter.EntrainementAdapter;
import projet.iutlp.projet_objectifssportifs.Adapter.ObjectifAdapter;
import projet.iutlp.projet_objectifssportifs.Adapter.SportAdapter;

import projet.iutlp.projet_objectifssportifs.ModelDB.Entrainement;
import projet.iutlp.projet_objectifssportifs.ModelDB.Objectif;
import projet.iutlp.projet_objectifssportifs.ModelDB.Sport;
import projet.iutlp.projet_objectifssportifs.BddSqlite.EntrainementDB;
import projet.iutlp.projet_objectifssportifs.BddSqlite.ObjectifDB;
import projet.iutlp.projet_objectifssportifs.BddSqlite.SportDB;
import projet.iutlp.projet_objectifssportifs.Pages.AjoutEntrainement;
import projet.iutlp.projet_objectifssportifs.Pages.AjoutObjectif;
import projet.iutlp.projet_objectifssportifs.Pages.EntrainementGps;
import projet.iutlp.projet_objectifssportifs.Utilitaire.ListeSports;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;

/**
 * Activity principale de l'application
 */
public class MainActivity extends AppCompatActivity {




    /**
     * l'adapter de la liste des objectif
     * je le mets en attribut pour pouvoir y acceder et le mettre a jour lors de la fin d'une activité (methode  onActivityResult) de la classe
     */
    private ObjectifAdapter objectifAdapter;

    /**
     * l'adapter de la liste des entrainements
     * je le mets en attribut pour pouvoir y acceder et le mettre a jour lors de la fin d'une activité (methode  onActivityResult) de la classe
     */
    private EntrainementAdapter entAdapter;

    /**
     * barre de navigation en bas de l'écran
     *  je le mets en attribut pour pouvoir y acceder et le mettre a jour lors de la fin d'une activité (methode  onActivityResult) de la classe
     */
    private BottomNavigationView bottomNavigationView;


    /**
     * methode onCreate appelé au lancement de l'activité
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        /*
         *Initialisation des composants pour les sports
         */
        //la bdd
        final SportDB sdb = SportDB.getInstance(MainActivity.this);

        //l'adapter du recycler view
        final SportAdapter sa = new SportAdapter((ArrayList<Sport>) sdb.getSports(), getApplicationContext());

        //le recycler view
        final RecyclerView recyclerViewSport = findViewById(R.id.listViewSport);
        recyclerViewSport.setHasFixedSize(true);
        LinearLayoutManager layoutManagerSport = new LinearLayoutManager(this);
        recyclerViewSport.setLayoutManager(layoutManagerSport);
        recyclerViewSport.setAdapter(sa);



        /*
         *Initialisation des composants objectifs
         */
        //Pour la bdd
        ObjectifDB odb = ObjectifDB.getInstance(this);
        //l'adapter
        this.objectifAdapter = new ObjectifAdapter((ArrayList<Objectif>) odb.getObjectifs());
        //Le recycler view
        final RecyclerView recyclerViewObjectif= findViewById(R.id.viewObjectifs);
        recyclerViewObjectif.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewObjectif.setLayoutManager(layoutManager);
        recyclerViewObjectif.setAdapter(this.objectifAdapter);


        /*
         *Initialisation des composants pour les entrainements
         */
        //Pour la bdd
        EntrainementDB edb = EntrainementDB.getInstance(getApplicationContext());
        //l'adapter
        entAdapter = new EntrainementAdapter((ArrayList<Entrainement>) edb.getEntrainements());
        //Le recyclerview
        final RecyclerView recyclerViewEntrainement = (RecyclerView) findViewById(R.id.viewEntrainements);
        recyclerViewEntrainement.setHasFixedSize(true);
        LinearLayoutManager layoutManagerEntrainement = new LinearLayoutManager(this);
        recyclerViewEntrainement.setLayoutManager(layoutManagerEntrainement);
        recyclerViewEntrainement.setAdapter(entAdapter);


        //On affiche les objectis au lancement de l'app, on mets les autres recyclers invisible
        recyclerViewEntrainement.setVisibility(View.INVISIBLE);
        recyclerViewSport.setVisibility(View.INVISIBLE);


        //Initialisation du listener de la bar de navigation, elle se charge d'afficher les recycler view ou non
        bottomNavigationView= findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.page_objectifs) {
                    recyclerViewSport.setVisibility(View.INVISIBLE);
                    recyclerViewObjectif.setVisibility(View.VISIBLE);
                    recyclerViewEntrainement.setVisibility(View.INVISIBLE);
                }
                if (item.getItemId() == R.id.page_sports) {
                    recyclerViewSport.setVisibility(View.VISIBLE);
                    recyclerViewObjectif.setVisibility(View.INVISIBLE);
                    recyclerViewEntrainement.setVisibility(View.INVISIBLE);
                }
                if (item.getItemId() == R.id.page_entrainements) {
                    recyclerViewSport.setVisibility(View.INVISIBLE);
                    recyclerViewObjectif.setVisibility(View.INVISIBLE);
                    recyclerViewEntrainement.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });




        /*
         *Initialisation de la vue qui va nous servir de fenetre de choix (alerte )
         * On la mets en dehors du listener floating action button pour ne pas le trop le charger d'autre listener
         */
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View viewMenu = inflater.inflate(R.layout.ajout_view, null, false);
        final Dialog dialogMenu = new AlertDialog.Builder(MainActivity.this).setView(viewMenu).create();
        /*
         *Initialisation des listeners sur la fenetre
         * Pour ajouter un sport
         */
        ImageView logoAjoutSport = (ImageView) viewMenu.findViewById(R.id.ajoutSport);
        logoAjoutSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //On ferme la fenetre de choix
                dialogMenu.cancel();
                //On set la vue pour l'alertDialog d'ajout de sport
                 View viewAjoutSport= inflater.inflate(R.layout.ajout_sport, null, false);

                //On set la autocomplete bar avec le tableau des sports de la classe ListeSport
                ArrayAdapter<String> adapterSearch = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.select_dialog_multichoice, ListeSports.LISTE);
                final AutoCompleteTextView searchBar = (AutoCompleteTextView)
                        viewAjoutSport.findViewById(R.id.sportSearch);
                searchBar.setAdapter(adapterSearch);


                /*
                 *Listener sur le bouton d'ajout de sport
                 */
                Button b = viewAjoutSport.findViewById(R.id.boutonAjoutSport);

                final Switch switchDistance = viewAjoutSport.findViewById(R.id.switchDistance);
                final Switch switchTemps = viewAjoutSport.findViewById(R.id.switchTemps);

                //On creer et on affiche la fenetre de dialog
                final Dialog dialog = new AlertDialog.Builder(MainActivity.this).setView(viewAjoutSport).create();
                dialog.show();

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Sport sportAjoute = new Sport("" + searchBar.getText(), switchDistance.isChecked(), switchTemps.isChecked());

                        sdb.addSport(sportAjoute);
                        sa.refresh(getApplicationContext());
                        dialog.cancel();
                        /*
                         *On affiche la liste des sports
                         */
                        bottomNavigationView.setSelectedItemId(R.id.page_sports);


                    }
                });



            }
        });

        /*
         *Listener sur le bouton de lancement d'entrainement
         */
        ImageView logoDemarrerEnt = (ImageView) viewMenu.findViewById(R.id.demarrerEntrainement);
        logoDemarrerEnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, EntrainementGps.class);
                startActivityForResult(intent,0);
                dialogMenu.cancel();
            }
        });


        /*
         *Pour ajouter un objectif
         */
        ImageView logoAjoutObjectif = viewMenu.findViewById(R.id.ajoutObjectif);


        logoAjoutObjectif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //On ferme la fenetre de choix
                dialogMenu.cancel();

                Intent intent=new Intent(MainActivity.this, AjoutObjectif.class);
                startActivityForResult(intent,0);

                //On set la vue pour l'alertDialog d'ajout d'objectif
               /* final View viewAjoutObjectif = inflater.inflate(R.layout.ajout_objectif, null, false);
                final Dialog dialog = new AlertDialog.Builder(MainActivity.this).setView(viewAjoutObjectif).create();
                dialog.show();

                final DatePicker dp=viewAjoutObjectif.findViewById(R.id.dpDateDeb);
                //dp.setVisibility(View.INVISIBLE);

                final EditText et=viewAjoutObjectif.findViewById(R.id.editText);
                et.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        et.setVisibility(View.INVISIBLE);
                        dp.setVisibility(View.VISIBLE);
                    }
                });*/

            }
        });

        /*
        *Pour ajouter une activité
         */

        ImageView logoAjoutEntrainement = viewMenu.findViewById(R.id.ajoutEntrainemente);

        logoAjoutEntrainement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMenu.cancel();
                //redirection vers l'activite d'ajout d'entrainements
                Intent intent=new Intent(MainActivity.this, AjoutEntrainement.class);
                startActivityForResult(intent,0);
            }
        });

        //Initialisation du listener sur le floacting action button
        FloatingActionButton fab = findViewById(R.id.boutonAjout);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dialogMenu.show();

            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*
         *Si une activite vient de se finir avec le code -1
         * C'est que l'utilisateur n'a pas ajouter de sport avant d'ajouter un objectif ou un entrainement
         * On va donc le rediriger vers l'ajout de sport
         */
        if(resultCode==-1) {

            new AlertDialog.Builder(MainActivity.this)
                    .setMessage(R.string.alertePasDeSport)
                    .setPositiveButton(R.string.ouiChef,null)
                    .show();
        }
        /*
        *Si l'activité qui vient de se finir est AjoutObjectif (request code = 1 )
        * On refresh la recycler view
         */
        if(resultCode==1) {
            bottomNavigationView.setSelectedItemId(R.id.page_objectifs);
            objectifAdapter.refresh(MainActivity.this);
        }
        /*
         *Si l'activité qui vient de se finir est AjoutEntrainement (request code = 2 )
         * On refresh les recycler views d'entrainement et d'objectifs
         */
        if(resultCode==2) {
            bottomNavigationView.setSelectedItemId(R.id.page_entrainements);
            entAdapter.refresh(MainActivity.this);
            objectifAdapter.refresh(MainActivity.this);
        }


    }

    /**
     * methode rechargeant les recyclers views entrainement et sport
     */
    public void refreshAll(){
        entAdapter.refresh(MainActivity.this);
        objectifAdapter.refresh(MainActivity.this);
    }




}

