package projet.iutlp.projet_objectifssportifs.BddSqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import projet.iutlp.projet_objectifssportifs.ModelDB.Sport;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe d'acces à table sport de la base de donnees
 * singleton car si on creer 2 objet SportDB l'app crash (1 handler pour 1 table)
 */
public class SportDB extends SQLiteOpenHelper {

    /**
     * Le nom de la base de données
     */
    private static final String DATABASE_NAME = "ObjectifsSportsDB";
    /**
     * version de la base de donnees
     */
    private static final int DATABASE_VERSION = 1;
    /**
     * Nom de la table
     */
    private static final String SPORT_TABLE_NAME = "sports";
    /**
     * Nom des colonnes
     * colonnes ID
     * Identifiant du sport (autoIncrement)
     */
    public static final String COLUMN_ID_SPORT = "id";
    /**
     * Nom du sport
     */
    private static final String COLUMN_NAME_SPORT = "nom";

    /**
     * 1 si le sport est mesurable en metre, 0 sinon
     */
    private static final String COLUMN_IS_DISTANCE = "Is_Distance";

    /**
     * 1 si le sport est mesurable en temps, 0 sinon
     */
    private static final String COLUMN_IS_TEMPS = "Is_Temps";

    /**
     * le context de l'application
     * utile pour appeler ObjectifDB et Entrainement DB
     */
    Context context;

    /**
     * requete sql de creation de la table
     */
    private static final String SPORTS_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + SPORT_TABLE_NAME + " (" +
                    COLUMN_ID_SPORT + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME_SPORT + " TEXT," + COLUMN_IS_DISTANCE + " INTEGER," + COLUMN_IS_TEMPS + " INTEGER );";

    /**
     * L'unique objet sportDB pouvant etre creer
     */
    private static SportDB instance;

    /**
     * la methode getInstance qui assure le singleton
     *
     * @param context le context de l'appication
     * @return instance
     * l'unique instance de l'application
     */
    public static SportDB getInstance(Context context) {
        if (instance == null) {
            instance = new SportDB(context);
            context=context;
        }
        return instance;
    }


    /**
     * Constructeur
     *
     * @param context le context de l'application
     */
    private SportDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        /*
         * on créer et ouvre la db une 1ere fois
         */
        this.onCreate(getReadableDatabase());


    }


    /**
     * Methode onCreate lance a la creation
     *
     * @param db la base de données SQLite
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SPORTS_TABLE_CREATE);

    }

    /**
     * Methode onUpgrade
     *
     * @param db
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + SPORT_TABLE_NAME);
        onCreate(db);
    }


    /**
     * methode getSports retournant tous les sports dans la base
     *
     * @return la liste des sports présent dans la base
     */
    public List<Sport> getSports() {
        List<Sport> result = new ArrayList<Sport>();
        Cursor cursor = getReadableDatabase().query(SPORT_TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            //On recupere les items de la base
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_SPORT));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_SPORT));


            boolean bDistance = false;
            boolean bTemps = false;
            int isDistance = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_DISTANCE));
            int isTemps = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_TEMPS));

            if (isDistance == 1) {
                bDistance = true;
            }
            if (isTemps == 1) {
                bTemps = true;
            }
            Sport sport = new Sport(id, name, bDistance, bTemps);
            result.add(sport);

        }


        return result;
    }

    /**
     * methode addSport pour ajouter un sport à la base
     *
     * @param sport Le sport ajouté
     */
    public void addSport(Sport sport) {


        //Transformation des booleans en int
        int isDistance = 0;
        int isTemps = 0;
        if (sport.isDistance())
            isDistance = 1;
        if (sport.isTemps())
            isTemps = 1;

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_SPORT, sport.getNom());
        values.put(COLUMN_IS_DISTANCE, isDistance);
        values.put(COLUMN_IS_TEMPS, isTemps);

        this.getReadableDatabase().insert(SPORT_TABLE_NAME, null, values);

    }

    /**
     * methode remove
     *
     * @param sport
     */
    public void removeSport(Sport sport) {
        //getWritableDatabase().delete(retoucher ici supp a, COLUMN_ID_SPORT + " = ?", new String[]{ Integer.toString(sport.getId())});
        getWritableDatabase().delete(SPORT_TABLE_NAME, COLUMN_ID_SPORT + " = ?", new String[]{Integer.toString(sport.getId())});
    }

    /**
     * methode supprimant un sport
     * supprimet les objectif et entrainements liés a ce sport également
     * @param id
     *          l'id du sport en question
     */
    public void delete(int id){
        //On supprime tous les objectifs liés au sport
        ObjectifDB.getInstance(context).deleteByIdSport(id);
        //On supprimes tous les entrainements liés au sport
        EntrainementDB.getInstance(context).deleteByIdSport(id);
        //On supprime le sport
        this.getWritableDatabase().delete(SPORT_TABLE_NAME,"id=?",new String[]{""+id});
    }

    /**
     * methode mettant a jour dans la base un sport
     * @param sport
     *              le sport mis a jour
     */
    public void update(Sport sport){
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_NAME_SPORT,sport.getNom());

        if(sport.isDistance())contentValues.put(COLUMN_IS_DISTANCE,1);
        else contentValues.put(COLUMN_IS_DISTANCE,0);

        if(sport.isTemps())contentValues.put(COLUMN_IS_TEMPS,1);
        else contentValues.put(COLUMN_IS_TEMPS,0);

        this.getWritableDatabase().update(SPORT_TABLE_NAME,contentValues,"id=?",new String[]{""+sport.getId()});
    }

    /**
     * methode retournant 1 sport de la base
     *
     * @param index l'index du sport
     * @return sport
     * le sport dans la base a cet index
     */
    public Sport getSport(int index) {

        Cursor cursor = getReadableDatabase().rawQuery("Select * from " + SPORT_TABLE_NAME + " where " + COLUMN_ID_SPORT + " =?", new String[]{String.valueOf(index)});
        if (cursor.moveToFirst()) {

            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_SPORT));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_SPORT));

            boolean bDistance = false;
            boolean bTemps = false;

            int isDistance = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_DISTANCE));
            int isTemps = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_TEMPS));

            if (isDistance == 1) {
                bDistance = true;
            }
            if (isTemps == 1) {
                bTemps = true;
            }

            return new Sport(id, name, bDistance, bTemps);
        } else {
            return null;
        }


    }

    /**
     * Methode verifirant si un sport est deja dans la base
     *
     * @param sport le sport a verifier
     * @return int
     * -1 si le sport n'est pas dans la base , index du sport si le sport est dans la base
     */
    public int EstDansLaBase(Sport sport) {
        //Transformation des booleens en int
        int isDistance = 0;
        int isTemps = 0;
        if (sport.isDistance())
            isDistance = 1;
        if (sport.isTemps())
            isTemps = 1;

        //la requete
        String query = "Select * from " + SPORT_TABLE_NAME + " where " + COLUMN_NAME_SPORT + " = ? and " + COLUMN_IS_DISTANCE + " = ? and " + COLUMN_IS_TEMPS + " = ? ;";
        Cursor c = getReadableDatabase().rawQuery(query, new String[]{sport.getNom(), "" + isDistance, "" + isTemps});

        if (c.moveToFirst()) {
            //Si le sport est dans la base, on retourne son id
            return c.getInt(c.getColumnIndex(COLUMN_ID_SPORT));
        } else {
            //Sinon on retourne -1
            return -1;
        }


    }

    /**
     * Methode vidant la table des sports
     */
    public void clear() {
        this.getWritableDatabase().delete(SPORT_TABLE_NAME, null, null);
    }


}
