package projet.iutlp.projet_objectifssportifs.ModelDB;

import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;

/**
 * classe entrainement mdelisant un entrainement realisé par l'utilisateur
 */
public class Entrainement implements Serializable {

    /**
     * l'id de l'entrainement
     */
    private int id;

    /**
     * la date de l'entrainement
     */
    private LocalDate date;

    /**
     * la distance effectuée
     */
    private int distance;

    /**
     * le temps réalisé
     */
    private Duration temps;

    /**
     * le sport associe à l'entrainement
     */
    private Sport sport;

    /**
     * la liste des points localisation GPS
     */
    private GeoPoint[] listPosition;

    /**
     * constructeur d'entrainement avec 5 parametres
     * @param id
     *          l'id dans la base de l'entrainement
     * @param date
     *          la date de l'entrainement
     * @param distance
     *              la distance parcouru
     * @param temps
     *              le temps realise
     * @param sport
     *              le sport associe
     */
    public Entrainement(int id, LocalDate date, int distance, Duration temps, Sport sport) {
        this.id = id;
        this.date = date;
        this.distance = distance;
        this.temps = temps;
        this.sport = sport;
    }

    /**
     *constructeur d'entrainement avec 4 parametres
     * @param date
     *          la date de l'entrainement
     * @param distance
     *              la distance parcouru
     * @param temps
     *              le temps realise
     * @param sport
     *              le sport associe
     */
    public Entrainement(LocalDate date, int distance, Duration temps, Sport sport) {
        this.date = date;
        this.distance = distance;
        this.temps = temps;
        this.sport = sport;
    }

    /**
     * getteur d'if
     * @return id
     *          l'id de l'entrainement dans la base
     */
    public int getId() {
        return id;
    }

    /**
     * setteur d'id
     * @param id
     *          le nouvel id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * getteur de
     * @return date
     *          la date de l'entrainement
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * setteur de date
     * @param date
     *          la nouvelle date d'entrainement
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * getteur de distance
     * @return distance
     *              la nouvelle distance
     */
    public int getDistance() {
        return distance;
    }

    /**
     * setteur de distance
     * @param distance
     *          la nouvelle distance
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * getteur de temps
     * @return temps
     * le temps de l'entrainement
     */
    public Duration getTemps() {
        return temps;
    }

    /**
     * setteur de temps
     * @param temps
     *          le temps a realise
     */
    public void setTemps(Duration temps) {
        this.temps = temps;
    }

    /**
     * getteur de sport
     * @return sport
     *          le sport associe a l'entrainement
     */
    public Sport getSport() {
        return this.sport;
    }

    /**
     * setteur de sport
     * @param sport
     *          le nouveau sport associe a l'entraienement
     */
    public void setSport(Sport sport) {
        this.sport = sport;
    }
}
