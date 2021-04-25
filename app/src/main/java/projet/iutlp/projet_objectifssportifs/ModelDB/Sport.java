package projet.iutlp.projet_objectifssportifs.ModelDB;

import java.io.Serializable;

/**
 * Classe representant un sport
 */
public class Sport implements Serializable {

    /**
     * id du sport dans la base
     */
    private int id;

    /**
     * le nom du sport
     */
    private String nom;

    /**
     * true si le sport est mesurable en metre
     */
    private boolean isDistance;

    /**
     * true si le sport
     */
    private boolean isTemps;

    /**
     * constructeur de Sport avec 3 paramètres
     * @param nom
     *          le nom du sport
     * @param isDistance
     *              true si le sport est mesurable en metres
     * @param isTemps
     *              true si le sport est mesurable en temps
     */
    public Sport(String nom, boolean isDistance, boolean isTemps) {
        this.nom = nom;
        this.isDistance = isDistance;
        this.isTemps = isTemps;
    }

    /**
     * constructeur de Sport avec 4 paramètres
     * @param id
     *          l'id du sport dans la base
     * @param nom
     *          le nom du sport
     * @param isDistance
     *              true si le sport est mesurable en metres
     * @param isTemps
     *              true si le sport est mesurable en temps
     */
    public Sport(int id, String nom, boolean isDistance, boolean isTemps) {
        this.id = id;
        this.nom = nom;
        this.isDistance = isDistance;
        this.isTemps = isTemps;
    }

    /**
     * getteur de l'id
     * @return l'id du sport
     */
    public int getId() {
        return id;
    }

    /**
     * setteur de l'id
     * @param id
     *          l'id du sport
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * getteur du nom du sport
     * @return le nom du sport
     */
    public String getNom() {
        return nom;
    }

    /**
     * setteur du nom du sport
     * @param nom
     *          le nouveau nom du sport
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * getteur de isDistance
     * @return
     *          isDistance
     */
    public boolean isDistance() {
        return isDistance;
    }

    /**
     * setteur de isDistance
     * @param distance
     *              return isDistance
     */
    public void setDistance(boolean distance) {
        isDistance = distance;
    }

    /**
     * getteur de isTemps
     * @return
     *             return isTemps
     */
    public boolean isTemps() {
        return isTemps;
    }

    /**
     * setteur de temps
     * @param temps
     *          setteur de temps
     */
    public void setTemps(boolean temps) {
        isTemps = temps;
    }
}
