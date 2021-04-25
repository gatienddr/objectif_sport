package projet.iutlp.projet_objectifssportifs.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import projet.iutlp.projet_objectifssportifs.BddSqlite.ObjectifDB;
import projet.iutlp.projet_objectifssportifs.ModelDB.Objectif;
import projet.iutlp.projet_objectifssportifs.ModelDB.Sport;
import projet.iutlp.projet_objectifssportifs.Pages.ModifObjectif;
import projet.iutlp.projet_objectifssportifs.R;
import projet.iutlp.projet_objectifssportifs.Utilitaire.Uniformisation;

import java.util.ArrayList;

/**
 * classe adapter pour afficher les objectifs dans une recycler view
 */
public class ObjectifAdapter extends RecyclerView.Adapter<ObjectifAdapter.ObjectifViewHolder> {


    /**
     * La liste des objectifs a afficher
     */
    private ArrayList<Objectif> listeObjectifs;


    /**
     * Le constructeur de l'adapter
     * @param al
     *          arraylist des objectifs
     */
    public ObjectifAdapter(ArrayList<Objectif> al) {
        listeObjectifs = al;
    }


    /**
     Methode onCreateViewHolder du reyclerAdapter
     * @param parent
     *             View group parent
     * @param viewType
     *              Viex type
     * @return
     *              ObjectifViewHolder, un item de la recyclerView
     */
    @NonNull
    @Override
    public ObjectifAdapter.ObjectifViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.objectif_view, parent, false);
        ObjectifViewHolder vh = new ObjectifViewHolder(v);

        return vh;


    }

    /**
     onBindViewHolder, recyclant les objets ObjectifViewHolder
     * @param holder
     *          le holder deja existant
     * @param position
     *          la position de l'item apparaissant a l'ecran dans la liste des objectifs
     */
    @Override
    public void onBindViewHolder(@NonNull final ObjectifViewHolder holder, final int position) {
        /*
        *On recupere et set les dates de debut et de fin
         */
        String dateDeb=""+listeObjectifs.get(position).getdDebut().getDayOfMonth()+" "+listeObjectifs.get(position).getdDebut().getMonth()+" "+""+listeObjectifs.get(position).getdDebut().getYear();
        String dateFin=""+listeObjectifs.get(position).getdFin().getDayOfMonth()+" "+listeObjectifs.get(position).getdFin().getMonth()+" "+""+listeObjectifs.get(position).getdFin().getYear();

        /*
        *On recupere le sport
         */
        Sport sport=listeObjectifs.get(position).getSport();

        /*
        *On set les champs
         */
        holder.textViewDistance.setText(""+listeObjectifs.get(position).getDistance());
        holder.textViewDistancePourcentage.setText(""+listeObjectifs.get(position).getPourcentageDistance()+" % réalisé");
        holder.pbPourcentageDistance.setMax(100);
        holder.pbPourcentageDistance.setMin(0);
        holder.pbPourcentageDistance.setProgress(listeObjectifs.get(position).getPourcentageDistance());
        holder.textViewTemps.setText(""+ Uniformisation.getTimeUniforme(listeObjectifs.get(position).getTemps()));
        holder.textViewTempsPourcentage.setText(""+listeObjectifs.get(position).getPourcentageTemps() +" % réalisé ");
        holder.pbPourcentageTemps.setMax(100);
        holder.pbPourcentageTemps.setMin(0);
        holder.pbPourcentageTemps.setProgress(listeObjectifs.get(position).getPourcentageTemps());
        holder.textViewDateDebut.setText(dateDeb);
        holder.textViewDateFin.setText(dateFin);
        holder.textViewSportObj.setText(sport.getNom());

        /*
         * On n'affiche pas la durée/le temps si le sport n'est pas mesurable en durée/temps
         */
        if(!sport.isDistance()){
            holder.textViewDistance.setVisibility(View.GONE);
            holder.textViewDistancePourcentage.setVisibility(View.GONE);
            holder.pbPourcentageDistance.setVisibility(View.GONE);
        }
        else{
            holder.textViewDistance.setVisibility(View.VISIBLE);
            holder.textViewDistancePourcentage.setVisibility(View.VISIBLE);
            holder.pbPourcentageDistance.setVisibility(View.VISIBLE);
        }
        if(!sport.isTemps()){
            holder.textViewTemps.setVisibility(View.GONE);
            holder.textViewTempsPourcentage.setVisibility(View.GONE);
            holder.pbPourcentageTemps.setVisibility(View.GONE);
        }
        else{
            holder.textViewTemps.setVisibility(View.VISIBLE);
            holder.textViewTempsPourcentage.setVisibility(View.VISIBLE);
            holder.pbPourcentageTemps.setVisibility(View.VISIBLE);
        }

       /*
       *je set un long click listener sur la material card pour pouvoir modifier / supprimer l'objectif
        */

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent=new Intent(view.getContext(), ModifObjectif.class);
                //Je mets dans le Intent l'id de l'objectif
                intent.putExtra("idObjectif",listeObjectifs.get(position).getId());

                //je lance l'activité de modification d'objectif
                ((AppCompatActivity)view.getContext()).startActivityForResult(intent,0);
                return true;
            }
        });


    }

    /**
     * methode getItemCount
     * @return la taille de la listes des objectifs
     */
    @Override
    public int getItemCount() {
        return listeObjectifs.size();
    }


    /**
     * Methode permettant de refresh le recyclerview (après l'ajout d'un objectif notamment)
     * @param context
     *          le contexte de l'application
     */
    public void refresh(Context context){
        this.listeObjectifs=(ArrayList<Objectif>) ObjectifDB.getInstance(context).getObjectifs();
        this.notifyDataSetChanged();
    }


    /**
     * Classe ObjectifViewHolder correspondant aux attributs des "tuiles" du recycler view
     */
    public static class ObjectifViewHolder extends RecyclerView.ViewHolder  {

        /**
         * La distance
         */
        public TextView textViewDistance;
        /**
         * le temps
         */
        public TextView textViewTemps;
        /**
         * le pourcentage de distance réalisée
         */
        public TextView textViewDistancePourcentage;
        /**
         * La progress bar correpondant au poucentage distance réalisé
         */
        public ProgressBar pbPourcentageDistance;
        /**
         * le pourcentage de temps réalisée
         */
        public TextView textViewTempsPourcentage;
        /**
         * La progress bar correpondant au poucentage temps réalisé
         */
        public ProgressBar pbPourcentageTemps;
        /**
         * Date de debut de periode pour realiser l'objectif
         */
        public TextView textViewDateDebut;
        /**
         * date fin de periode pour realiser l'objectif
         */
        public TextView textViewDateFin;
        /**
         * le sport correspondant
         */
        public TextView textViewSportObj;

        /**
         * Constructeur
         * @param v
         *          La vue deja existante a recycler
         */
        public ObjectifViewHolder(View v) {
            super(v);
            textViewDistance =  v.findViewById(R.id.distanceObjectif);
            textViewDistancePourcentage= v.findViewById(R.id.distancePourcentage);
            pbPourcentageDistance=v.findViewById(R.id.progressPourcentageDistance);
            textViewTempsPourcentage= v.findViewById(R.id.tempsPourcentage);
            pbPourcentageTemps= v.findViewById(R.id.progressPourcentageTemps);
            textViewTemps= v.findViewById(R.id.tempsObjectif);
            textViewDateDebut=v.findViewById(R.id.dateDebut);
            textViewDateFin=v.findViewById(R.id.dateFin);
            textViewSportObj=v.findViewById(R.id.sportObjectif);
        }


    }




}



