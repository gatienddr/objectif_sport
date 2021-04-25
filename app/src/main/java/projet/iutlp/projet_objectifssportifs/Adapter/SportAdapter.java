package projet.iutlp.projet_objectifssportifs.Adapter;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import projet.iutlp.projet_objectifssportifs.BddSqlite.SportDB;
import projet.iutlp.projet_objectifssportifs.MainActivity;
import projet.iutlp.projet_objectifssportifs.ModelDB.Sport;
import projet.iutlp.projet_objectifssportifs.R;

import java.util.ArrayList;

/**
 * classe adapter pour afficher les sports dans une recycler view
 */
public class SportAdapter extends RecyclerView.Adapter<SportAdapter.SportViewHolder> {

    /**
     * L'arrayList les sports pratiqués
     */
    private ArrayList<Sport> listeSport;

    /**
     * le context
     * utile pour les couleurs des images view
     */
    Context context;


    /**
     * Constructeur de l'adapteur
     * @param al
     *          l'arraylist des sports
     * @param context
     *          le contexte de l'app
     */
    public SportAdapter(ArrayList<Sport> al, Context context){
        this.listeSport=al;
        this.context=context;
    }


    /**
     Methode onCreateViewHolder du reyclerAdapter
     * @param parent
     *             View group parent
     * @param viewType
     *              Viex type
     * @return
     *              SportViewHolder, un item de la recyclerView
     */
    @NonNull
    @Override
    public SportAdapter.SportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sport_view, parent, false);
        SportViewHolder vh = new SportViewHolder(v);
        return vh;
    }


    /**
     onBindViewHolder, recyclant les objets SportViewHolder
     * @param holder
     *          le holder deja existant
     * @param position
     *          la position de l'item apparaissant a l'ecran dans la liste des Sports
     */
    @Override
    public void onBindViewHolder(@NonNull final SportAdapter.SportViewHolder holder, final int position) {
        holder.textViewSportNom.setText(this.listeSport.get(position).getNom());
        holder.textViewSportIsDistance.setText("Distance : "+this.listeSport.get(position).isDistance());
        holder.textViewSportIsTemps.setText("Temps : "+this.listeSport.get(position).isTemps());

        /*
        *la couleur des traits
         */
        if(this.listeSport.get(position).isDistance())holder.couleurIsDistance.setBackgroundColor(ContextCompat.getColor(this.context, R.color.colorvert));
        else holder.couleurIsDistance.setBackgroundColor(ContextCompat.getColor(this.context, R.color.colortRed));
        if(this.listeSport.get(position).isTemps()) holder.couleurIsTemps.setBackgroundColor(ContextCompat.getColor(this.context,R.color.colorvert));
        else holder.couleurIsTemps.setBackgroundColor(ContextCompat.getColor(this.context,R.color.colortRed));

        //Je mets un long click listener sur les items pour pouvoir modifier le sport
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                /*
                 * Je gere les bouttons et interaction de la fenetre de modif de sport ici
                 */

                //on inflate la view que l'on veut afficher dans la fenetre de dialog
                final LayoutInflater inflater = (LayoutInflater) ((AppCompatActivity)(holder.itemView.getContext())).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View viewModifSport = inflater.inflate(R.layout.modif_sport, null, false);

                //on set les items que l'on peut modifier
                final AutoCompleteTextView autoCompleteTextView=viewModifSport.findViewById(R.id.sportSearch);
                autoCompleteTextView.setText(listeSport.get(position).getNom());

                final Switch switchDistance=viewModifSport.findViewById(R.id.switchDistance);
                if(listeSport.get(position).isDistance())switchDistance.setChecked(true);
                else switchDistance.setChecked(false);

                final Switch switchTemps=viewModifSport.findViewById(R.id.switchTemps);
                if(listeSport.get(position).isTemps())switchTemps.setChecked(true);
                else switchTemps.setChecked(false);


                final Dialog dialog = new AlertDialog.Builder(holder.itemView.getContext()).setView(viewModifSport).create();
                dialog.show();


                Button bModif=viewModifSport.findViewById(R.id.boutonModifSport);
                bModif.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Sport sport=new Sport(
                           listeSport.get(position).getId(),
                           autoCompleteTextView.getText().toString(),
                           switchDistance.isChecked(),
                           switchTemps.isChecked()
                        );

                        SportDB.getInstance(holder.itemView.getContext()).update(sport);
                        //je refresh les adapters
                        refresh(holder.itemView.getContext());
                        ((MainActivity)(holder.itemView.getContext())).refreshAll();
                        dialog.cancel();
                    }
                });


                //je commence par le button de suppression
                Button bSupp =viewModifSport.findViewById(R.id.buttonSupp);
                bSupp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //je supprime le sport et les entrainements et objectifs liés
                        SportDB.getInstance(view.getContext()).delete(listeSport.get(position).getId());
                        dialog.cancel();
                        //je refresh les adapters
                        refresh(holder.itemView.getContext());
                        ((MainActivity)(holder.itemView.getContext())).refreshAll();


                    }
                });


                return true;
            }
        });

    }

    /**
     * methode getItemCount
     * @return la taille de la listes des sports
     */
    @Override
    public int getItemCount() {
        return listeSport.size();
    }

    /**
     * Methode permettant de refresh le recyclerview (après l'ajout d'un sport notamment)
     * @param context
     *          le contexte de l'application
     */
    public void refresh(Context context){
        this.listeSport=(ArrayList<Sport>)SportDB.getInstance(context).getSports();
        this.notifyDataSetChanged();
    }




    /**
     * Classe SportViewHolder correspondant aux attributs des "tuiles" du recycler view
     */
    public static class SportViewHolder extends RecyclerView.ViewHolder {

        /**
         * le nom du sport
         */
        public TextView textViewSportNom;
        /**
         * Si il est mesurable en distance
         */
        public TextView textViewSportIsDistance;
        /**
         * Si il est mesurable en temps
         */
        public TextView textViewSportIsTemps;

        /**
         * couleur
         * vert si il est mesurable en distance, rouge sinon
         */
        public View couleurIsDistance;
        /**
         * couleur
         * vert si il est mesurable en temps, rouge sinon
         */
        public View couleurIsTemps;



        /**
         * Constructeur
         * @param v
         *      view recyclée
         */
        public SportViewHolder(View v) {
            super(v);
            textViewSportNom =  v.findViewById(R.id.sport);
            textViewSportIsDistance= v.findViewById(R.id.isDistance);
            textViewSportIsTemps=v.findViewById(R.id.isTemps);
            couleurIsDistance=v.findViewById(R.id.couleurIsDistance);
            couleurIsTemps=v.findViewById(R.id.couleurIsTemps);

        }

    }



}
