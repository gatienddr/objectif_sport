package projet.iutlp.projet_objectifssportifs.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import projet.iutlp.projet_objectifssportifs.BddSqlite.EntrainementDB;
import projet.iutlp.projet_objectifssportifs.ModelDB.Sport;
import projet.iutlp.projet_objectifssportifs.Pages.Map;
import projet.iutlp.projet_objectifssportifs.Pages.ModifEntrainement;
import projet.iutlp.projet_objectifssportifs.Utilitaire.Uniformisation;
import projet.iutlp.projet_objectifssportifs.ModelDB.Entrainement;
import projet.iutlp.projet_objectifssportifs.R;

import java.util.ArrayList;



/**
 * classe adapter pour afficher les entrainements dans une recycler view
 */
public class EntrainementAdapter extends RecyclerView.Adapter<EntrainementAdapter.EntrainementViewHolder> {

    /**
     * La liste des entrainements
     */
    private ArrayList<Entrainement> listeEntrainements;

    /**
     * Le consctructeur de l'adapter
     * @param al
     *          l'array list des entrainements
     */
    public EntrainementAdapter(ArrayList<Entrainement> al) {
        listeEntrainements = al;
    }


    /**
     * Methode onCreateViewHolder du reyclerAdapter
     * @param parent
     *             View group parent
     * @param viewType
     *              Viex type
     * @return
     *              EntrainementViewHolder, un item de la recyclerView
     */
    @NonNull
    @Override
    public EntrainementAdapter.EntrainementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entrainement_view, parent, false);
        EntrainementViewHolder vh = new EntrainementViewHolder(v);
        return vh;
    }

    /**
     *  onBindViewHolder, recyclant les objets EntrainementsViewHolder
     * @param holder
     *          le holder deja existant
     * @param position
     *          la position de l'item apparaissant a l'ecran dans la liste des entrainements
     */
    @Override
    public void onBindViewHolder(@NonNull EntrainementViewHolder holder, int position) {
        /*
        *On recupere le sport
        */

         Sport sport=listeEntrainements.get(position).getSport();

        /*
         *On set les items du viewHolder en mettant en forme temps et date avec les methodes de la classe uniformisation
         */

        holder.textViewSportEnt.setText(""+sport.getNom());
        holder.textViewDateEnt.setText(Uniformisation.getDateUniforme(listeEntrainements.get(position).getDate()));
        holder.textViewTempsEnt.setText(Uniformisation.getTimeUniforme(listeEntrainements.get(position).getTemps()));
        holder.textViewDistanceEnt.setText(""+listeEntrainements.get(position).getDistance()+" metres");

        /*
         * On n'affiche pas la durée/le temps si le sport n'est pas mesurable en durée/temps
         */
        if(!sport.isDistance())holder.textViewDistanceEnt.setVisibility(View.GONE);
        else holder.textViewDistanceEnt.setVisibility(View.VISIBLE);
        if(!sport.isTemps())holder.textViewTempsEnt.setVisibility(View.GONE);
        else holder.textViewTempsEnt.setVisibility(View.VISIBLE);

        //On met la position en final pour pouvoir l'utiliser par la suite
        final int pos=position;
        //On met un long click listener sur les items pour pouvoir les modifier/supprimer
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent=new Intent(view.getContext(),ModifEntrainement.class);
                //Je mets dans le Intent l'id de l'entrainement
                intent.putExtra("idEntrainement",listeEntrainements.get(pos).getId());

                //je lance l'activité de modification d'objectif
                ((AppCompatActivity)view.getContext()).startActivityForResult(intent,0);
                return true;
            }
        });

        /*
        *Si l'utilisateur fais un simple clique, et que le sport est mesurable en distance
        * alors on le redirige vers la map
         */
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), Map.class);
                intent.putExtra("idEntrainement",listeEntrainements.get(pos).getId());
                if(listeEntrainements.get(pos).getSport().isDistance()){
                    ((AppCompatActivity)view.getContext()).startActivityForResult(intent,0);
                }

            }
        });

    }


    /**
     * Methode permettant de refresh le recyclerview (après l'ajout d'un entrainement notamment)
     * @param context
     *          le contexte de l'application
     */
    public void refresh(Context context){
        this.listeEntrainements=(ArrayList<Entrainement>) EntrainementDB.getInstance(context).getEntrainements();
        this.notifyDataSetChanged();
    }

    /**
     * methode getItemCount
     * @return la taille de la listes d'entrainements
     */
    @Override
    public int getItemCount() {
        return listeEntrainements.size();
    }


    /**
     * Classe EntrainementViewHolder correspondant aux attributs des "tuiles" du recycler view
     */
    public static class EntrainementViewHolder extends RecyclerView.ViewHolder {

        /**
         * Le sport
         */
        public TextView textViewSportEnt;
        /**
         * la distance parcouru
         */
        public TextView textViewDistanceEnt;
        /**
         * Le temps effectué
         */
        public TextView textViewTempsEnt;
        /**
         * La date de l'entrainement
         */
        public TextView textViewDateEnt;


        /**
         * Constructeur
         * @param v
         *          La vue deja existante a recycler
         */
        public EntrainementViewHolder(View v) {
            super(v);
            textViewSportEnt=v.findViewById(R.id.sportEnt);
            textViewDistanceEnt =  v.findViewById(R.id.distanceEnt);
            textViewDateEnt= v.findViewById(R.id.dateEnt);
            textViewTempsEnt=v.findViewById(R.id.tempsEnt);

        }

    }




}

