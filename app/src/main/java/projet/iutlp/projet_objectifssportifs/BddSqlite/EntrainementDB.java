package projet.iutlp.projet_objectifssportifs.BddSqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import projet.iutlp.projet_objectifssportifs.ModelDB.Entrainement;
import projet.iutlp.projet_objectifssportifs.ModelDB.Sport;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe d'accès à la table entrainements de la base de données SQLite (singleton)
 */
public class EntrainementDB extends SQLiteOpenHelper {

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
    private static final String ENTRAINEMENT_TABLE_NAME = "entrainements";

    /**
     * Nom des colonnes
     * colonne ID
     *          Identifiant de l'entrainements (autoIncrement)
     */
    public static final String COLUMN_ID_ENTRAINEMENT = "id";

    /**
     * Colonne date
     *          date de l'entrainement
     */
    public static final String COLUMN_DATE = "date";

    /**
     * Colonne distance
     *          distance de l'entrainement en metres
     */
    public static final String COLUMN_DISTANCE = "distance";

    /**
     * Colonne temps
     *          temps de l'entrainement
     */
    public static final String COLUMN_TEMPS = "temps";


    /**
     * Colonne id_sport FOREIGN KEY
     *              l'id du sport conresspondant à l'entrainement
     */
    public static final String COLUMN_ID_SPORT = "id_sport";

    /**
     * On stock le context pour appeler SportDB lorsque lon a besoin
     */
    private Context context;

    /**
     * l'instance unique de la classe
     */
    private static EntrainementDB instance;

    /**
     * Requete SQL de création de la table
     */
    private static final String ENTRAINEMENT_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + ENTRAINEMENT_TABLE_NAME + " (" +
                    COLUMN_ID_ENTRAINEMENT + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DATE+ " TEXT,"+ COLUMN_DISTANCE +" INTEGER,"+COLUMN_TEMPS+" TEXT,"
                    + COLUMN_ID_SPORT+" INTEGER, FOREIGN KEY("+COLUMN_ID_SPORT+") REFERENCES sports(id));";


    /**
     * La méthode getInstance assurant de singleton et permettant l'obtenir l'objet
     */
    public static EntrainementDB getInstance(Context context){
        if(instance==null){
            instance=new EntrainementDB(context);
        }
        return instance;
    }


    /**
     * Constructeur privé
     * @param context
     *          le context de l'application
     */
    private EntrainementDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.onCreate(getWritableDatabase());
    }

    /**
     * Methode onCreate, s'execute lors du lancement
     * @param db
     *          l'objet base de donnée
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ENTRAINEMENT_TABLE_CREATE);
    }

    /**
     * Methode onUpgrade
     * @param db
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ENTRAINEMENT_TABLE_NAME);
        onCreate(db);
    }


    /**
     * methodes getEntrainements retournant tous les entrainements
     * @return
     *         arrayList de tous les entrainements de l'utilisateur
     */
    public List<Entrainement> getEntrainements(){
        List<Entrainement> entrainements=new ArrayList<Entrainement>();
        Cursor cursor = getReadableDatabase().query(ENTRAINEMENT_TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {

            //On recuperre les items de la base
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_ENTRAINEMENT));
            LocalDate date = LocalDate.parse(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
            int distance= cursor.getInt(cursor.getColumnIndex(COLUMN_DISTANCE));
            //LocalTime temps=LocalTime.parse(cursor.getString(cursor.getColumnIndex(COLUMN_TEMPS)));
            Duration temps=Duration.parse(cursor.getString(cursor.getColumnIndex(COLUMN_TEMPS)));

            int idSport = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_SPORT));

            //On va rechercher le sport associe
            SportDB sdb=SportDB.getInstance(context);
            Sport sAssocie=sdb.getSport(idSport);
            Log.d("Gtt", "getEntrainements: "+idSport);
            entrainements.add(new Entrainement(id,date,distance,temps,sAssocie));
        }
        this.close();
        return entrainements;
        }

    /**
     * Methode d'aoujout entrainement
      * @param entrainement
     *          l'entrainement ajoute dans la base
     * @return booleen
     *          >0 si l"entrainement a ete ajoute, false sinon
     */
    public long addEntraintement(Entrainement entrainement){

        //On verifie que le sport est dans la base avant d'ajoute l'objectif
         SportDB sdb=SportDB.getInstance(context);
         int indexSport=sdb.EstDansLaBase(entrainement.getSport());
         //Si le sport est bien dans la base
         if(indexSport!=-1) {
            /*
            *On ajoute l'entrainement dans la base
             */
             ContentValues values = new ContentValues();
             values.put(COLUMN_DATE, entrainement.getDate().toString());
             values.put(COLUMN_DISTANCE, ""+entrainement.getDistance());
             values.put(COLUMN_TEMPS,entrainement.getTemps().toString());
             values.put(COLUMN_ID_SPORT, indexSport);
             long idEntrainement=this.getWritableDatabase().insert(ENTRAINEMENT_TABLE_NAME, null, values);

             /*
              *On met a jour les objectifs de ce sport qui contienne cette date
              */
             ObjectifDB.getInstance(context).updateObjectifsApresAjoutEntrainement(entrainement);


             this.close();
             return idEntrainement;
         }
         //sinon on retourne false
         return -1;
    }

    /**
     * Methode vidant la table des entrainements
     */
    public void clear(){
        this.getWritableDatabase().delete(ENTRAINEMENT_TABLE_NAME,null,null);
    }

    /**
     * methode deletant les entrainements selon l'index
     * @param index
     *          l'index de l'entrainement
     */
    public void delete(int index){
        //Je recherche l'entrainement


        this.getWritableDatabase().delete(ENTRAINEMENT_TABLE_NAME,"id=?",new String[]{""+index});
        //Je reset les pourcentages des objectif
        ObjectifDB.getInstance(context).updateObjectifs();
    }

    /**
     * methode retournant un entrainement selon l'index
     * @param index
     *          l'index de l'entrainement dans la base
     * @return
     *          l'entrainement recherché
     */
    public Entrainement getEntrainement(int index){
        Cursor c=this.getWritableDatabase().query(ENTRAINEMENT_TABLE_NAME,null,"id=?",new String[]{""+index},null,null,null,null);
        if(c.moveToFirst()){
            return new Entrainement(c.getInt(c.getColumnIndex(COLUMN_ID_ENTRAINEMENT)),
                    LocalDate.parse(c.getString(c.getColumnIndex(COLUMN_DATE))),
                    Integer.parseInt(c.getString(c.getColumnIndex(COLUMN_DISTANCE))),
                    Duration.parse(c.getString(c.getColumnIndex(COLUMN_TEMPS))),
                    SportDB.getInstance(context).getSport(c.getInt(c.getColumnIndex(COLUMN_ID_SPORT)))
                    );
        }
        else{
            return null;
        }
    }


    /**
     * Methode retournant la distance et le temps cumulés des entrainements effectué entre 2 dates pour un sport donné
     * @param sport
     *          le sport
     * @param dateDeb
     *           la date de debut des entrainements
     * @param dateFin
     *             la date de fin des entrainements
     * @return  tabCumul
     *          le tableau des cumule de temps et de distance
     *          tabCumul[0]
     *                      la valeur de la distance cumulé
     *          tabCumul[1]
     *                      la valeur du temps cumulé s
     *          On mets le tableau en String pour utiliser les 2 types (int et Duration)
     */
    public String[] getEntrainementCumule(Sport sport, LocalDate dateDeb, LocalDate dateFin){
        //J'initialise mes 2 cases à 0
        String[] tabCumul=new String[2];
        tabCumul[0]="0";
        tabCumul[1]=""+Duration.ZERO;



        /*
        *Si le sport est mesurable en distance
         */
        if(sport.isDistance()) {
            // requete pour recuperer toutes les distances des entrainements entre les 2 dates
            String query = "Select sum("+COLUMN_DISTANCE+") from " + ENTRAINEMENT_TABLE_NAME + " where " + COLUMN_DATE + " BETWEEN ?  and ? and " +
                    COLUMN_ID_SPORT + " = ? ;";
            Cursor c = getReadableDatabase().rawQuery(query, new String[]{dateDeb.toString(), dateFin.toString(), "" + sport.getId()});
            if(c.moveToFirst()){
                tabCumul[0]=""+c.getInt(0);
            }



        }

        /*
         *Si le sport est mesurable en temps
         */
        if(sport.isTemps()){

            String query = "Select "+COLUMN_TEMPS+" from " + ENTRAINEMENT_TABLE_NAME + " where " + COLUMN_DATE + " BETWEEN ?  and ? and " +
                    COLUMN_ID_SPORT + " = ? ;";
            Cursor c = getReadableDatabase().rawQuery(query, new String[]{dateDeb.toString(), dateFin.toString(), "" + sport.getId()});


                Duration durationCumul=Duration.ofMillis(0);



                while(c.moveToNext()) {
                    durationCumul=durationCumul.plus(Duration.parse(c.getString(0)));

                }
                tabCumul[1]=durationCumul.toString();
            }


      return tabCumul;

    }


    /**
     * methode de mise a jour d'entrainement
     * @param newEntrainement
     *          l'entrainement mis a jour
     */
    public void updateEntrainement(Entrainement newEntrainement){

        //je mets a jour l'entrainement


        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_DATE,newEntrainement.getDate().toString());
        contentValues.put(COLUMN_DISTANCE,""+newEntrainement.getDistance());
        contentValues.put(COLUMN_TEMPS,""+newEntrainement.getTemps().toString());

        contentValues.put(COLUMN_ID_SPORT,""+newEntrainement.getSport().getId());
        this.getWritableDatabase().update(ENTRAINEMENT_TABLE_NAME, contentValues, "id=?", new String[]{""+newEntrainement.getId()});

        //je recalcule les pourcentages des objectifs concerné
        ObjectifDB.getInstance(this.context).updateObjectifs();

    }

    /**
     * methode de permettant de supprimer tous les entrainements selon l'id du sport
     * @param id
     *          l'id du sport
     */
    public void deleteByIdSport(int id){

        this.getWritableDatabase().delete(ENTRAINEMENT_TABLE_NAME,COLUMN_ID_SPORT+"=?",new String[]{""+id});
    }





}
