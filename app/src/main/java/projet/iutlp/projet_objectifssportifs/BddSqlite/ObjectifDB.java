package projet.iutlp.projet_objectifssportifs.BddSqlite;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import projet.iutlp.projet_objectifssportifs.MainActivity;
import projet.iutlp.projet_objectifssportifs.ModelDB.Entrainement;
import projet.iutlp.projet_objectifssportifs.ModelDB.Objectif;
import projet.iutlp.projet_objectifssportifs.ModelDB.Sport;
import projet.iutlp.projet_objectifssportifs.R;


import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



/**
 *  Classe d'acces à table Objectifs de la base de donnees (singleton)
 */
public class ObjectifDB extends SQLiteOpenHelper {

    /**
     *Le nom de la base de données
     */
    private static final String DATABASE_NAME = "ObjectifsSportsDB";
    /**
     *version de la base de donnees
     */
    private static final int DATABASE_VERSION = 1;
    /**
     * Nom de la table
     */
    private static final String OBJECTIF_TABLE_NAME = "objectifs";
    /**
     * Nom des colonnes
     * colonne ID
     *          Identifiant de l'objectif (autoIncrement)
     */
    public static final String COLUMN_ID_OBJECTIF = "id";

    /**
     * Colonnes date_debut
     *          la date de debut de l'objectif
     */
    public static final String COLUMN_DATE_DEBUT="date_debut";

    /**
     * Colonnes date_fin
     *          la date de fin de l'objectif
     */
    public static final String COLUMN_DATE_FIN="date_fin";

    /**
     * Colonne distance
     *              la distance de l'objectif
     */
    public static final String COLUMN_DISTANCE = "distance";

    /**
     * Colonne temps
     *              le temps de l'objectif
     */
    public static final String COLUMN_TEMPS = "temps";

    /**
     * Colonne pourcentageDistance
     *             le pourcentage d'accomplissement de la distance de l'objectif
     */
    public static final String COLUMN_POUCENTAGE_DISTANCE="pourcentageDistance";


    /**
     * Colonne pourcentageDistance
     *             le pourcentage d'accomplissement de la distance de l'objectif
     */
    public static final String COLUMN_POUCENTAGE_TEMPS="poucentageTemps";

    /**
     * Colonne statut
     *              le statut de l'objectif, 1 si rempli, 0 sinon
     */
    public static final String COLUMN_STATUT = "statut";

    /**
     * Colonne id_sport FOREIGN KEY
     *              l'id du sport conresspondant à l'objectif
     */
    public static final String COLUMN_ID_SPORT = "id_sport";

    /**
     * On stock le context pour appeler SportDB lorsque lon a besoin
     */
    private Context context;

    /**
     * l'instance unique de la classe
     */
    private static ObjectifDB instance;


    /**
     * Requete SQL de création de la table
     */
    private static final String OBJECTIF_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + OBJECTIF_TABLE_NAME + " (" +
                    COLUMN_ID_OBJECTIF + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DATE_DEBUT + " TEXT,"+ COLUMN_DATE_FIN +" TEXT,"+COLUMN_DISTANCE+" INTEGER,"+COLUMN_TEMPS+" TEXT," +
                    COLUMN_POUCENTAGE_DISTANCE+" INTEGER,"+COLUMN_POUCENTAGE_TEMPS+" INTEGER,"+
            COLUMN_STATUT+" INTEGER,"+ COLUMN_ID_SPORT+" INTEGER, FOREIGN KEY("+COLUMN_ID_SPORT+") REFERENCES sports(id));";

    /**
     * La méthode getInstance assurant de singleton et permettant l'obtenir l'objet
     */
    public static ObjectifDB getInstance(Context context){
        if(instance==null){
            instance=new ObjectifDB(context);
        }
        return instance;
    }


    /**
     * Constructeur privé
     * @param context
     *          le context de l'application
     */
    private ObjectifDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.onCreate(getWritableDatabase());
        this.context=context;
    }

    /**
     * Methode onCreate, s'execute lors du lancement
     * @param db
     *          l'objet base de donnée
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(OBJECTIF_TABLE_CREATE);
    }



    /**
     * Methode onUpgrade
     * @param db
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+OBJECTIF_TABLE_NAME);
        onCreate(db);
    }


    /**
     * methodes getObjectifs
     * @return List<Objectif>
     *      la liste de tous les objectifs
     */
    public List<Objectif> getObjectifs(){
        List<Objectif> result = new ArrayList<Objectif>();
        Cursor cursor = getReadableDatabase().query(OBJECTIF_TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            LocalDate l=LocalDate.parse((cursor.getString(cursor.getColumnIndex(COLUMN_DATE_DEBUT))));


            //On recupere les items de la base
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_OBJECTIF));

            //on recupere les dates et le temps


            LocalDate dDebut = LocalDate.parse(cursor.getString(cursor.getColumnIndex(COLUMN_DATE_DEBUT)));
            LocalDate dFin = LocalDate.parse(cursor.getString(cursor.getColumnIndex(COLUMN_DATE_FIN)));
            Duration temps=Duration.parse(cursor.getString(cursor.getColumnIndex(COLUMN_TEMPS)));

            int distance = cursor.getInt(cursor.getColumnIndex(COLUMN_DISTANCE));


            int pourcentageDistance=cursor.getInt(cursor.getColumnIndex(COLUMN_POUCENTAGE_DISTANCE));

            int pourcentageTemps=cursor.getInt(cursor.getColumnIndex(COLUMN_POUCENTAGE_TEMPS));


            boolean estRemplis = false;
            if(cursor.getInt(cursor.getColumnIndex(COLUMN_STATUT))==1){
                estRemplis=true;
            }

            int sportIndex=cursor.getInt(cursor.getColumnIndex(COLUMN_ID_SPORT));



            SportDB sportDB=SportDB.getInstance(context);
            result.add(new Objectif(id,dDebut, dFin,  distance, temps ,pourcentageDistance,pourcentageTemps,estRemplis, sportDB.getSport(sportIndex)));
        }

        return result;
    }

    /**
     *methode retournant un objectif selon son id
     * @param idObj
     *          l'id de l'objectif recherché
     * @return
     *      l'objectif recherché
     */
    public Objectif getObjectif(int idObj){

        //On recherche l'objectif dans la base
        Cursor cursor = getReadableDatabase().query(OBJECTIF_TABLE_NAME, null, "id=?", new String[]{""+idObj}, null, null, null);
        if(cursor.moveToFirst()){
            LocalDate l=LocalDate.parse((cursor.getString(cursor.getColumnIndex(COLUMN_DATE_DEBUT))));


            //On recupere les items de la base
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_OBJECTIF));

            //on recupere les dates et le temps


            LocalDate dDebut = LocalDate.parse(cursor.getString(cursor.getColumnIndex(COLUMN_DATE_DEBUT)));
            LocalDate dFin = LocalDate.parse(cursor.getString(cursor.getColumnIndex(COLUMN_DATE_FIN)));
            Duration temps=Duration.parse(cursor.getString(cursor.getColumnIndex(COLUMN_TEMPS)));

            int distance = cursor.getInt(cursor.getColumnIndex(COLUMN_DISTANCE));


            int pourcentageDistance=cursor.getInt(cursor.getColumnIndex(COLUMN_POUCENTAGE_DISTANCE));

            int pourcentageTemps=cursor.getInt(cursor.getColumnIndex(COLUMN_POUCENTAGE_TEMPS));


            boolean estRemplis = false;
            if(cursor.getInt(cursor.getColumnIndex(COLUMN_STATUT))==1){
                estRemplis=true;
            }

            int sportIndex=cursor.getInt(cursor.getColumnIndex(COLUMN_ID_SPORT));



            SportDB sportDB=SportDB.getInstance(context);
            return new Objectif(id,dDebut, dFin,  distance, temps ,pourcentageDistance,pourcentageTemps,estRemplis, sportDB.getSport(sportIndex));

        }
        return null;

    }


    /**
     * Méthode pour ajouter un objectif
     * @param objectif
     *              le nouvel objectif
     * @return true
     *          si l'objectif a bien ete ajouté en base
     */
    public boolean addObjectif(Objectif objectif){


        //On verifie que le sport est dans la base avant d'ajoute l'objectif
        SportDB sdb=SportDB.getInstance(context);
        int indexSport=sdb.EstDansLaBase(objectif.getSport());
        if(indexSport!=-1) {
            ///Si oui, alors on peut ajouter l'objectif

           //on passe le statut en int
           int statut=0;
           if(objectif.estRemplis())statut=1;


           /*
           *On calcule le pourcentage d'accomplissement que l'utilisateur a deja pu realiser
           * (cas ou l'utilisateur ajoute un objectif apres avoir ajouté un entrainement)
           * On recupere, dans un premier temps, la distance et le temps cumulé entre les deux dates
            */
            EntrainementDB entrainementDB=EntrainementDB.getInstance(context);
            String[] tabCumul=entrainementDB.getEntrainementCumule(objectif.getSport(),objectif.getdDebut(),
                    objectif.getdFin());

            /*
            *On calcule le poucentage de la distance
             */
            int pourcentageDistance;
            //On evite la division par 0

            if(Integer.parseInt(tabCumul[0])!=0) pourcentageDistance = (int)((Double.parseDouble(tabCumul[0])/objectif.getDistance())*100);
            else pourcentageDistance = 0;

            objectif.setPourcentageDistance(pourcentageDistance);


            /*
            *On calcule le pourcentage du temps
             */
            int pourcentageTemps;

            Duration dureeRealise=Duration.parse(tabCumul[1]);
            if(dureeRealise.getSeconds()!=0) pourcentageTemps = (int)((((double)dureeRealise.getSeconds()/(double)objectif.getTemps().getSeconds()))*100);
            else pourcentageTemps = 0;

            objectif.setPourcentageTemps(pourcentageTemps);

            if(pourcentageDistance>100 && pourcentageTemps>100){
                objectif.setEstRemplis(true);
            }




           ContentValues values = new ContentValues();
           values.put(COLUMN_DATE_DEBUT, objectif.getdDebut().toString());
           values.put(COLUMN_DATE_FIN, objectif.getdFin().toString());
           values.put(COLUMN_DISTANCE, objectif.getDistance());
           values.put(COLUMN_TEMPS, objectif.getTemps().toString());
           values.put(COLUMN_POUCENTAGE_DISTANCE, objectif.getPourcentageDistance());
           values.put(COLUMN_POUCENTAGE_TEMPS, objectif.getPourcentageTemps());
           values.put(COLUMN_STATUT, statut);
           values.put(COLUMN_ID_SPORT, indexSport);
           this.getWritableDatabase().insert(OBJECTIF_TABLE_NAME, null, values);
           this.close();

           return true;
        }
       //sinon on retourne false
        return false;
    }

    /**
     * Methode vidant la table des objectifs
     */
    public void clear(){
        this.getWritableDatabase().delete(OBJECTIF_TABLE_NAME,null,null);
    }

    /**
     * Methode supprimant un objectif de la base
     * @param id
     *          l'id de l'objectif dans la base
     */
    public void delete(int id){
        this.getWritableDatabase().delete(OBJECTIF_TABLE_NAME,COLUMN_ID_OBJECTIF+"=?",new String[]{""+id});
    }


    /**
     * methode mettants a jour les taux d'accomplissement des objecifs après l'ajout d'un entrainement par l'utilisateur
     * @param entrainement
     *              l'entrainements entré par l'utilisateur
     */
    public void updateObjectifsApresAjoutEntrainement(Entrainement entrainement){
        /*
        /On recupere tous les objectifs qui doivent prendre en compte l'entrainement
         */
        String query = "Select * from " + OBJECTIF_TABLE_NAME + " where " + COLUMN_DATE_DEBUT + " <= ?  and "+COLUMN_DATE_FIN+">= ? and " +
                COLUMN_ID_SPORT + " = ? ;";
        Cursor c = getReadableDatabase().rawQuery(query, new String[]{entrainement.getDate().toString(), entrainement.getDate().toString(),
                "" + entrainement.getSport().getId()});

        ArrayList<Objectif> listeObjectifs=new ArrayList<>();

        if(c.moveToFirst()){

            Boolean estRempli=false;
            if(c.getInt(c.getColumnIndex(COLUMN_STATUT))==1)estRempli=true;
            Objectif o=new Objectif(
                    c.getInt(0),
                    LocalDate.parse(c.getString(c.getColumnIndex(COLUMN_DATE_DEBUT))),
                    LocalDate.parse(c.getString(c.getColumnIndex(COLUMN_DATE_FIN))),
                    c.getInt(c.getColumnIndex(COLUMN_DISTANCE)),
                    Duration.parse(c.getString(c.getColumnIndex(COLUMN_TEMPS))),
                    c.getInt(c.getColumnIndex(COLUMN_POUCENTAGE_DISTANCE)),
                    c.getInt(c.getColumnIndex(COLUMN_POUCENTAGE_TEMPS)),
                    estRempli,
                    entrainement.getSport()

            );

           listeObjectifs.add(o);
           while (c.moveToNext()){
               o=new Objectif(
                       c.getInt(0),
                       LocalDate.parse(c.getString(c.getColumnIndex(COLUMN_DATE_DEBUT))),
                       LocalDate.parse(c.getString(c.getColumnIndex(COLUMN_DATE_FIN))),
                       c.getInt(c.getColumnIndex(COLUMN_DISTANCE)),
                       Duration.parse(c.getString(c.getColumnIndex(COLUMN_TEMPS))),
                       c.getInt(c.getColumnIndex(COLUMN_POUCENTAGE_DISTANCE)),
                       c.getInt(c.getColumnIndex(COLUMN_POUCENTAGE_TEMPS)),
                       estRempli,
                       entrainement.getSport());
                       listeObjectifs.add(o);
           }
        }


        /*
        *Maintenant que l'on a recupérer tous les objectifs, on peut calculer leurs nouveau pourcentages
        * Pour eviter de tous recalculer, on calcule juste le pourcentage de l'entrainemt par rapport aux objectifs
        * et on rajoute ce pourcentage au pourcentage des objectif deja existants
        * On fait un produit en croix pour cela (distance ou temps entrainement / distance ou temps objectif )*100
        * puis on fait une maj de la base de données
         */

        String updateObjectifQuery;

        for (Objectif obj: listeObjectifs
             ) {

            //On gere le cas ou la distance ou le temps serait égale à 0
            int nouveauPourcentageDistance;
            if(obj.getDistance()>0) nouveauPourcentageDistance=(int)((((double)entrainement.getDistance()/(double)obj.getDistance())*100)+obj.getPourcentageDistance());
            else nouveauPourcentageDistance=(int)(((double)entrainement.getDistance()/1)*100);

            int nouveauPourcentageTemps;
            if(obj.getTemps().getSeconds()>0) nouveauPourcentageTemps=(int)((((double)entrainement.getTemps().getSeconds()/(double)obj.getTemps().getSeconds())*100)+obj.getPourcentageTemps());
            else nouveauPourcentageTemps=(int)(((double)entrainement.getTemps().getSeconds()/1)*100);

            //Update de l'objectif
            updateObjectifQuery="UPDATE "+OBJECTIF_TABLE_NAME+" set "+COLUMN_POUCENTAGE_DISTANCE+" = "+nouveauPourcentageDistance+ ", "+
                    COLUMN_POUCENTAGE_TEMPS+" = "+nouveauPourcentageTemps+" WHERE "+COLUMN_ID_OBJECTIF +" = "+obj.getId();

            getWritableDatabase().execSQL(updateObjectifQuery);



            /*
             *Notification
             */
            //Si a objectif est remplis, alors on le notifie
            //On verifie que l'utilisateur a remplis son objectif à 100 %
            boolean notif=true;
            if(obj.getSport().isDistance()){
                if(nouveauPourcentageDistance<100){
                    notif =false;
                }
            }

            if(obj.getSport().isTemps()){
                if(nouveauPourcentageTemps<100){
                    notif=false;
                }
            }

            //si l'objectif est bien remplis on peut notifier
            if(notif){

                String CHANNEL_ID="id_channel";

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel=new NotificationChannel(CHANNEL_ID,"Nom du canal",importance);
                    channel.setDescription("Description de mon canal");
                    NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }

                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

                NotificationCompat.Builder builder=new NotificationCompat.Builder(context,CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Objectif atteint !")
                        .setContentText("Bravo soldat")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);
                notificationManagerCompat.notify(0,builder.build());


            }

        }



    }

    /*
     * methode mettants a jour les taux d'accomplissement des objecifs après la suppression d'un entrainement par l'utilisateur
     * @param entrainementSuppprime
     *              l'entrainements supprimé par l'utilisateur
     *
    public void updateObjectifsApresSuppEntrainement(Entrainement entrainementSuppprime){

         /*
        /On recupere tous les objectifs qui doivent prendre en compte la suppression de l'entrainement
         *
        String query = "Select * from " + OBJECTIF_TABLE_NAME + " where " + COLUMN_DATE_DEBUT + " <= ?  and "+COLUMN_DATE_FIN+">= ? and " +
                COLUMN_ID_SPORT + " = ? ;";
        Cursor c = getReadableDatabase().rawQuery(query, new String[]{entrainementSuppprime.getDate().toString(), entrainementSuppprime.getDate().toString(),
                "" + entrainementSuppprime.getSport().getId()});

        ArrayList<Objectif> listeObjectifs=new ArrayList<>();

        Objectif o;
            while (c.moveToNext()){
                Boolean estRempli=false;
                if(c.getInt(c.getColumnIndex(COLUMN_STATUT))==1)estRempli=true;
                o=new Objectif(
                        c.getInt(0),
                        LocalDate.parse(c.getString(c.getColumnIndex(COLUMN_DATE_DEBUT))),
                        LocalDate.parse(c.getString(c.getColumnIndex(COLUMN_DATE_FIN))),
                        c.getInt(c.getColumnIndex(COLUMN_DISTANCE)),
                        Duration.parse(c.getString(c.getColumnIndex(COLUMN_TEMPS))),
                        c.getInt(c.getColumnIndex(COLUMN_POUCENTAGE_DISTANCE)),
                        c.getInt(c.getColumnIndex(COLUMN_POUCENTAGE_TEMPS)),
                        estRempli,
                        entrainementSuppprime.getSport());
                listeObjectifs.add(o);
            }

        /*
         *Maintenant que l'on a recupérer tous les objectifs, on peut calculer leurs nouveau pourcentages
         * Pour eviter de tous recalculer, on calcule juste le pourcentage de l'entrainemt par rapport aux objectifs
         * et on enleve ce pourcentage au pourcentage des objectif deja existants
         * On fait un produit en croix pour cela (distance ou temps entrainement / distance ou temps objectif )*100
         * puis on fait une maj de la base de données


        String updateObjectifQuery;

        for (Objectif obj: listeObjectifs
        ) {

            //On gere le cas ou la distance ou le temps serait égale à 0
            int nouveauPourcentageDistance;
            if (obj.getDistance() > 0)
                nouveauPourcentageDistance = (int) (obj.getPourcentageDistance() - (((double) entrainementSuppprime.getDistance() / (double) obj.getDistance()) * 100));
            else nouveauPourcentageDistance = 0;

            int nouveauPourcentageTemps;
            if (obj.getTemps().getSeconds() > 0)
                nouveauPourcentageTemps = (int) (obj.getPourcentageTemps() - (((double) entrainementSuppprime.getTemps().getSeconds() / (double) obj.getTemps().getSeconds()) * 100));
            else nouveauPourcentageTemps = 0;

            if(nouveauPourcentageDistance<0)nouveauPourcentageDistance=0;
            if(nouveauPourcentageTemps<0)nouveauPourcentageTemps=0;

            //Update de l'objectif
            updateObjectifQuery = "UPDATE " + OBJECTIF_TABLE_NAME + " set " + COLUMN_POUCENTAGE_DISTANCE + " = " + nouveauPourcentageDistance + ", " +
                    COLUMN_POUCENTAGE_TEMPS + " = " + nouveauPourcentageTemps + " WHERE " + COLUMN_ID_OBJECTIF + " = " + obj.getId();

            getWritableDatabase().execSQL(updateObjectifQuery);
        }




    }
*/
    /**
     * methode mettant à jour un objectif
     * on oublie pas de recalculer les pourcentages
     * @param objectif
     *          le nouvel objectif
     */
    public boolean modifObjectif(Objectif objectif){



        //On verifie que le sport est dans la base avant d'ajoute l'objectif
        SportDB sdb=SportDB.getInstance(context);
        int indexSport=sdb.EstDansLaBase(objectif.getSport());
        if(indexSport!=-1) {


            /*
             *On recalcule le pourcentage d'accomplissement que l'utilisateur a deja pu realiser
             * (cas ou l'utilisateur ajoute un objectif apres avoir ajouté un entrainement)
             * On recupere, dans un premier temps, la distance et le temps cumulé entre les deux dates
             */
            EntrainementDB entrainementDB=EntrainementDB.getInstance(context);
            String[] tabCumul=entrainementDB.getEntrainementCumule(objectif.getSport(),objectif.getdDebut(),
                    objectif.getdFin());

            /*
             *On recalcule le poucentage de la distance
             */
            int pourcentageDistance;
            //On evite la division par 0

            if(Integer.parseInt(tabCumul[0])!=0) pourcentageDistance = (int)((Double.parseDouble(tabCumul[0])/objectif.getDistance())*100);
            else pourcentageDistance = 0;

            objectif.setPourcentageDistance(pourcentageDistance);

            /*
             *On calcule le pourcentage du temps
             */
            int pourcentageTemps;

            Duration dureeRealise=Duration.parse(tabCumul[1]);
            if(dureeRealise.getSeconds()!=0) pourcentageTemps = (int)((((double)dureeRealise.getSeconds()/(double)objectif.getTemps().getSeconds()))*100);
            else pourcentageTemps = 0;

            objectif.setPourcentageTemps(pourcentageTemps);

            if(pourcentageDistance>100 && pourcentageTemps>100){
                objectif.setEstRemplis(true);
            }

            int statut=0;
            if(objectif.estRemplis()){
                statut=1;
            }


            ContentValues values = new ContentValues();
            values.put(COLUMN_DATE_DEBUT, objectif.getdDebut().toString());
            values.put(COLUMN_DATE_FIN, objectif.getdFin().toString());
            values.put(COLUMN_DISTANCE, objectif.getDistance());
            values.put(COLUMN_TEMPS, objectif.getTemps().toString());
            values.put(COLUMN_POUCENTAGE_DISTANCE, objectif.getPourcentageDistance());
            values.put(COLUMN_POUCENTAGE_TEMPS, objectif.getPourcentageTemps());
            values.put(COLUMN_STATUT, statut);
            values.put(COLUMN_ID_SPORT, objectif.getSport().getId());

            

            //on fait l'update et on retourne true
            this.getWritableDatabase().update(OBJECTIF_TABLE_NAME, values, "id=?", new String[]{""+objectif.getId()});

            return true;


        }
        return false;

    }

    /**
     * methode qui update tous les objectifs
     */
    public void updateObjectifs() {
        /*
         *Je recupere tous les objectifs
         */
        Cursor c = getReadableDatabase().query(OBJECTIF_TABLE_NAME,null,null,null,null,null,null);

        ArrayList<Objectif> listeObjectifs = new ArrayList<>();

        Objectif o;
        while (c.moveToNext()) {
            Boolean estRempli = false;
            if (c.getInt(c.getColumnIndex(COLUMN_STATUT)) == 1) estRempli = true;

            Sport sport=SportDB.getInstance(this.context).getSport(c.getInt(c.getColumnIndex(COLUMN_ID_SPORT)));
            o = new Objectif(
                    c.getInt(0),
                    LocalDate.parse(c.getString(c.getColumnIndex(COLUMN_DATE_DEBUT))),
                    LocalDate.parse(c.getString(c.getColumnIndex(COLUMN_DATE_FIN))),
                    c.getInt(c.getColumnIndex(COLUMN_DISTANCE)),
                    Duration.parse(c.getString(c.getColumnIndex(COLUMN_TEMPS))),
                    c.getInt(c.getColumnIndex(COLUMN_POUCENTAGE_DISTANCE)),
                    c.getInt(c.getColumnIndex(COLUMN_POUCENTAGE_TEMPS)),
                    estRempli,
                    sport
                    );

            listeObjectifs.add(o);
        }



        /*
         *Pour chaque objectif
         *je recalcule entierement leur pourcentage
         */


        String[] tabCumule;

        for (Objectif objectif : listeObjectifs) {
            //je selectionne la somme des distance et la somme et des temps des entrainements que l'objectif englobe

            tabCumule = EntrainementDB.getInstance(this.context).getEntrainementCumule(objectif.getSport(),
                    objectif.getdDebut(), objectif.getdFin());

            //Je calcule le nouveau poucentage de distance


            int newPourcentageDistance;
            if (objectif.getDistance() > 0){
                newPourcentageDistance = (int) ((Double.parseDouble(tabCumule[0]) / (double)(objectif.getDistance()))*100);
                Log.e("MICHE", "updateObjectifApresModifEntrainement: "+"iciic "+newPourcentageDistance );
            }
            else newPourcentageDistance = 0;

            int newPourcentageTemps;
            if (objectif.getTemps().toMillis() > 0)
                newPourcentageTemps = (int)((Duration.parse(tabCumule[1]).toMillis() /(double)( objectif.getTemps().toMillis()))*100);
            else newPourcentageTemps = 0;

            Log.e("MICHEL", "updateObjectifApresModifEntrainement: " +newPourcentageDistance );
            //Maintenant que j'ai mes 2 pourcentages, je peux updates mon objectifs
            String updateObjectifQuery = "UPDATE " + OBJECTIF_TABLE_NAME + " set " + COLUMN_POUCENTAGE_DISTANCE + " = " + newPourcentageDistance + ", " +
                    COLUMN_POUCENTAGE_TEMPS + " = " + newPourcentageTemps + " WHERE " + COLUMN_ID_OBJECTIF + " = " + objectif.getId();

            getWritableDatabase().execSQL(updateObjectifQuery);

        }

    }

    /**
     * methode qui supprime les objectif selon le sport
     * @param idSport
     *          l'id du sport concerné
     */
    public void deleteByIdSport(int idSport){

        this.getWritableDatabase().delete(OBJECTIF_TABLE_NAME,COLUMN_ID_SPORT+"=?",new String[]{""+idSport});
    }






}
