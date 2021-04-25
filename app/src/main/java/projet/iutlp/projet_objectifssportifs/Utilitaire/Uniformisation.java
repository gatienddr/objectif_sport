package projet.iutlp.projet_objectifssportifs.Utilitaire;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Classe regroupant/centralisant toute les methode utile et recurrente pour l'application
 */
public class Uniformisation {

    /**
     * Methode mettant en forme une LocalDate
     * @param localDate
     *            la date a mettre en forme
     * @return  String
     *          la date mise en forme
     */
    public static String getDateUniforme(LocalDate localDate){
        return ""+localDate.getDayOfMonth()+"-"+localDate.getMonth()+"-"+localDate.getYear();
    }


    /**
     * Methode mettant en forme un temps avec les heures et les minutes en
     * @param hours
     *          les heures en int
     * @param minutes
     *             les minutes en int
     * @return
     *              le temps en string
     */
    public static String getTimeUniforme(int hours, int minutes){
        String h, m;
        if(hours<10){
            h="0"+hours;
        }
        else{
            h=""+hours;
        }

        if(minutes<10){
            m="0"+minutes;
        }
        else{
            m=""+minutes;
        }
        return h+":"+m;
    }


    /**
     * Methode getTime uniforme
     * affiche la durée d'un entrainement en heures et minutes
     * @param duration
     *             la durée de l'entrainements
     * @return
     *              un String affichant
     */
    public static String getTimeUniforme(Duration duration){

        //Je recupere les minutes
        long minutes=duration.toMinutes()%60;

        //Je recupere les secondes
        long secondes=duration.getSeconds()%60;

        /*
         *Je mets au format que je souhaite
         */
        String hoursUniforme=""+duration.toHours();
        if(duration.toHours()<10){
            hoursUniforme="0"+duration.toHours();
        }

        String minutesUniformes=""+minutes;
        if(minutes<10){
            minutesUniformes="0"+minutes;
        }
        String secondesUniformes=""+secondes;
        if(secondes<10){
            secondesUniformes="0"+secondesUniformes;
        }
        return ""+hoursUniforme+":"+minutesUniformes+":"+secondesUniformes;
    }


}
