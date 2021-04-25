package projet.iutlp.projet_objectifssportifs.ModelDB;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;

/**
 * classe Objectif décrivant un objectif a realiser par l'utilisateur
 */
public class Objectif implements Serializable {

    /**
     * l'id de l'objectif
     */
    private int id;

    /**
     * la date du debut de l'objectif
     */
    private LocalDate dDebut;

    /**
     * la date de fin de l'objectif
     */
    private LocalDate dFin;

    /**
     * la distance a faire
     */
    private int distance;

    /**
     * le temps à réaliser
     */
    private Duration temps;

    /**
     * le pourcentage correspondant a la progression de l'accomplissement de l'objectif en distance
     */
    private int pourcentageDistance;

    /**
     * le pourcentage correspondant a la progression de l'accomplissement de l'objectif en temps
     */
    private int pourcentageTemps;

    /**
     * true si l'objectif est atteint, false sinon
     */
    private boolean estRemplis;

    /**
     * le sport associe à l'objectif
     */
    private Sport sport;

    /**
     *constructeur de la classe objectif avec 9 parametres ( utilisé plutot pour extraire les données de la BDD)
     * @param id
     *          l'id de l'objectif dans la base
     * @param dDebut
     *            la date de debut
     * @param dFin
     *              la date de fin
     * @param distance
     *              la distance a parcourir
     * @param temps
     *               le temps a effectuer
     * @param pourcentageDistance
     *              le taux d'accomplissement de l'objectif distance
     * @param pourcentageTemps
     *              le taux d'accomplissement de l'objectif temps
     * @param estRemplis
     *                 si l'objectif est atteint
     * @param sport
     *                  le sport lié à l'objectif
     */
    public Objectif(int id, LocalDate dDebut, LocalDate dFin, int distance,Duration temps, int pourcentageDistance, int pourcentageTemps,
                     boolean estRemplis, Sport sport) {
        this.id = id;
        this.dDebut = dDebut;
        this.dFin = dFin;
        this.distance = distance;
        this.temps = temps;
        this.pourcentageDistance=pourcentageDistance;
        this.pourcentageTemps=pourcentageTemps;
        this.estRemplis = estRemplis;
        this.sport = sport;
    }

    /**
     *constructeur de la classe objectif avec 7 parametres
     * Plutot utilise lors de la creation d'un objectif par l'utilisateur (l'objectif n'etant pas creer dans la BDD,
     * on ne peut pas set son ID)
     * @param id
     *      l'id de l'objectif dans la base
     * @param dDebut
     *            la date de debut
     * @param dFin
     *              la date de fin
     * @param distance
     *              la distance a parcourir
     * @param temps
     *               le temps a effectuer
     * @param estRemplis
     *                 si l'objectif est remplis
     * @param sport
     *                  le sport lié à l'objectif
     */
    public Objectif(int id,LocalDate dDebut, LocalDate dFin, int distance, Duration temps, boolean estRemplis, Sport sport) {
        this.id=id;
        this.dDebut = dDebut;
        this.dFin = dFin;
        this.distance = distance;
        this.temps = temps;
        this.pourcentageDistance=0;
        this.pourcentageTemps=0;
        this.estRemplis = estRemplis;
        this.sport = sport;
    }

    /**
     *constructeur de la classe objectif avec 6 parametres
     * Plutot utilise lors de la creation d'un objectif par l'utilisateur (l'objectif n'etant pas creer dans la BDD,
     * on ne peut pas set son ID)
     * @param dDebut
     *            la date de debut
     * @param dFin
     *              la date de fin
     * @param distance
     *              la distance a parcourir
     * @param temps
     *               le temps a effectuer
     * @param estRemplis
     *                 si l'objectif est remplis
     * @param sport
     *                  le sport lié à l'objectif
     */
    public Objectif(LocalDate dDebut, LocalDate dFin, int distance, Duration temps, boolean estRemplis, Sport sport) {
        this.dDebut = dDebut;
        this.dFin = dFin;
        this.distance = distance;
        this.temps = temps;
        this.pourcentageDistance=0;
        this.pourcentageTemps=0;
        this.estRemplis = estRemplis;
        this.sport = sport;
    }

    /**
     * getteur de l'id
     * @return id
     *          l'id de l'objectif dans la base
     */
    public int getId() {
        return id;
    }

    /**
     * setteur de l'id
     * @param id
     *          le nouvel id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * getteur de la date debut
     * @return dDebut
     *          la date de debut
     */
    public LocalDate getdDebut() {
        return dDebut;
    }

    /**
     * setteur de la date debut
     * @param dDebut
     *          la nouvel date de debut
     */
    public void setdDebut(LocalDate dDebut) {
        this.dDebut = dDebut;
    }

    /**
     * getteur de la date de fin
     * @return dFin
     *          la date de fin de l'objectif
     */
    public LocalDate getdFin() {
        return dFin;
    }

    /**
     * setteur de la date fin
     * @param dFin
     *          la nouvelle date de fin
     */
    public void setdFin(LocalDate dFin) {
        this.dFin = dFin;
    }

    /**
     * getteur de la distance a parcourir
     * @return distance
     *              la distance a parcourir
     */
    public int getDistance() {
        return distance;
    }

    /**
     * setteur de la distance
     * @param distance
     *              la nouvelle distance
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * le getteur du temps a realise
     * @return temps
     *          le temps a realiser
     */
    public Duration getTemps() {
        return temps;
    }

    /**
     * setteur de temps
     * @param temps
     *          le nouveau temps
     */
    public void setTemps(Duration temps) {
        this.temps = temps;
    }

    /**
     * getteur du pourcentage de distance effectuée par rapport à l'objectif
     * @return
     *         le pourcentage realise
     */
    public int getPourcentageDistance() {
        return pourcentageDistance;
    }

    /**
     * setteur du pourcentage de distance
     * @param pourcentageDistance
     *          le nouveau pourcentage de distance
     */
    public void setPourcentageDistance(int pourcentageDistance) {
        this.pourcentageDistance = pourcentageDistance;
    }

    /**
     * getteur du pourcentage temps
     * @return
     *         le pourcentage du temps realise
     */
    public int getPourcentageTemps() {
        return pourcentageTemps;
    }

    /**
     * setteur du pourcentage temps
     * @param pourcentageTemps
     *              le nouveau pourcentage temps
     */
    public void setPourcentageTemps(int pourcentageTemps) {
        this.pourcentageTemps = pourcentageTemps;
    }

    /**
     * getteur de estRemplis
     * @return estRemplis
     *         true si l'objectif atteints
     */
    public boolean estRemplis() {
        return estRemplis;
    }

    /**
     * setteur de estRemplis
     * @param estRemplis
     *           le nouveau statut de l'objectif
     */
    public void setEstRemplis(boolean estRemplis) {
        this.estRemplis = estRemplis;
    }

    /**
     * getteur de sport
     * @return sport
     *          le sport associe a l'objectif
     */
    public Sport getSport() {
        return sport;
    }

    /**
     * setteur de sport
     * @param sport
     *          le nouveau sport associe a l'objectif
     */
    public void setSport(Sport sport) {
        this.sport = sport;
    }
}
