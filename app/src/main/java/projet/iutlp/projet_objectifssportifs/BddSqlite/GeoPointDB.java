package projet.iutlp.projet_objectifssportifs.BddSqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

/**
 * class enregistrant les points (longitudes/latitudes) dans la base SQLITE
 */
public class GeoPointDB extends SQLiteOpenHelper {

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
    private static final String GEOPOINTS_TABLE_NAME = "geopoints";

    /**
     * Nom des colonnes
     * colonne ID
     *          Identifiant de l'entrainements (autoIncrement)
     */
    public static final String COLUMN_ID_GEOPOINT = "id";

    /**
     * Colonne longitude
     *          longitude de la position
     */
    public static final String COLUMN_LONGITUDE = "longitude";

    /**
     * Colonne latitude
     *          latitude de la position
     */
    public static final String COLUMN_LATITUDE = "latitude";

    /**
     * Colonne id entrainement FOREIGN KEY
     *          l'id de l'entrainement associé au tracing gps
     */
    public static final String COLUMN_ID_ENTRAINEMENT_ASSOCIE = "id_entrainement";

    /**
     * l'instance unique de la classe
     */
    private static GeoPointDB instance;


    /**
     * Requete SQL de création de la table
     */
    private static final String GEOPOINTS_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + GEOPOINTS_TABLE_NAME + " (" +
                    COLUMN_ID_GEOPOINT + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LONGITUDE+ " REAL, "+ COLUMN_LATITUDE+" , "
                    + COLUMN_ID_ENTRAINEMENT_ASSOCIE+" INTEGER, FOREIGN KEY("+COLUMN_ID_ENTRAINEMENT_ASSOCIE+") REFERENCES entrainements(id));";


    /**
     * La méthode getInstance assurant de singleton et permettant l'obtenir l'objet
     */
    public static GeoPointDB getInstance(Context context){
        if(instance==null){
            instance=new GeoPointDB(context);
        }
        return instance;
    }

    public GeoPointDB(@Nullable Context context) {
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
        db.execSQL(GEOPOINTS_TABLE_CREATE);
    }

    /**
     * Methode onUpgrade
     * @param db
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+GEOPOINTS_TABLE_NAME);
        onCreate(db);
    }

    /**
     * methode retournant tous les geopoint d'un entrainement gps
     * @param idEntrainement
     *              l'entrainement gps en question
     * @return
     *          le tracé sous forme d'une liste de Geopoint
     */
    public ArrayList<GeoPoint> getParcour(int idEntrainement){

        ArrayList<GeoPoint> listGeopoint=new ArrayList<>();

        //je fais une requete et je tries selon l'id pour les avoir dans le bon ordre
        Cursor cursor = getReadableDatabase().query(GEOPOINTS_TABLE_NAME, null, COLUMN_ID_ENTRAINEMENT_ASSOCIE+"=?", new String[]{""+idEntrainement}, null, null, COLUMN_ID_GEOPOINT);
        while(cursor.moveToNext()){
            listGeopoint.add(new GeoPoint(cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE)),
                    cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE))));
        }
        this.close();
        return listGeopoint;
    }

    /**
     * methode ajoutant des point de localisation à un entrainement gps
     * @param listePoints
     *              la liste des geo points
     * @param idEntrainement
     *              l'id de l'entrainement associé
     */
    public void add(ArrayList<GeoPoint> listePoints, long idEntrainement){
        ContentValues contentValues;

        /*
        *on parcour tous les points et on les ajoute
         */
        for (GeoPoint g: listePoints
             ) {
            contentValues=new ContentValues();
            contentValues.put(COLUMN_LONGITUDE,""+g.getLongitude());
            contentValues.put(COLUMN_LATITUDE,""+g.getLatitude());
            contentValues.put(COLUMN_ID_ENTRAINEMENT_ASSOCIE,idEntrainement);
            this.getWritableDatabase().insert(GEOPOINTS_TABLE_NAME,null, contentValues);
        }



    }
}
