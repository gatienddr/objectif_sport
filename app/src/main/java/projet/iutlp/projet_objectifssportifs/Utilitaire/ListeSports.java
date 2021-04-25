package projet.iutlp.projet_objectifssportifs.Utilitaire;

/**
 * classe ayant la liste de sport, utilisé pour l'autosuggest lors de l'ajout d'un sport
 * On aurait pu utiliser une APi mais je n'en ai pas trouvé une contenant tous les sports (elles sont tourné vers les resultats des sport médiatiques)
 */
public class ListeSports {

    /**
     * la liste des sports existant, en français
     */
    public static final String[] LISTE={
            "Accrobranche",
            "AcrosportAcrosport",



             "Aéromodélisme",
             "Aérostation",
             "Agility",
             "Aïkido",
             "Alpinisme",
             "Apnée",
             "Aquabike",
             "Aquagym",
             "Arts du cirque",
             "Athlétisme",
             "Aviron",



            "Baby gym",
            "Babyfoot",
             "Badminton",
             "Ball trap",
             "Bandy",
             "Base jump",
             "Baseball",
             "Basketball",
             "Beach soccer",
             "Beach tennis",
             "Beach volley",
             "Bébé nageur",
             "Behourd",
             "Biathlon",
             "Billard",
             "BMX",
             "Bobsleigh",
             "Boccia",
             "Bodyboard",
            "Boomerang",
             "Bouzkachi",
             "Bowling",
             "Boxe anglaise",
             "Boxe chinoise",
             "Boxe française",
             "Boxe thaïlandaise",
             "Bras de fer",



             "Caber",
             "Calcio florentin",
             "Canicross",
             "Canne de combat",
             "Canoë kayak",
             "Canyoning",
             "Capoeira",
             "Catch",
             "Cerf-volant",
             "Chanbara",
             "Char à voile",
             "Chase Tag",
             "Cheerleading",
             "Claquettes",
             "Combiné nordique",
             "Corde à sauter",
             "Course à pied",
             "Course aérienne",
             "Course camarguaise",
             "Course d'obstacles",
             "Course d'orientation",
             "Course de cote",
             "Course de drones",
             "Course landaise",
             "Cricket",
             "Croquet",
             "Cross training",
             "Crossfit",
             "Curling",
             "Cyclisme artistique",
             "Cyclisme sur piste",
             "Cyclisme sur route",
             "Cyclo cross",
             "Cyclotourisme",



             "Danse africaine",
             "Danse classique",
             "Danse contemporaine",
             "Danse country",
            "Danse indienne",
             "Danse orientale",
             "Danse rock",
             "Danse sportive",
             "Danse sur glace",
            "Deltaplane",
             "Disc golf",
             "Dragon boat",
             "Dragster",



            "E-sport",
             "EchecsEchecs",
             "Enduro",
             "Equitation",
             "Escalade",
             "Escrime",



             "Fierljeppen",
             "Fitness",
             "Flag",
             "Flamenco",
             "Fléchettes",
             "Floorball",
             "Football",
             "Football américain",
             "Football australien",
             "Footgolf",
             "Footpool",
             "Force athlétique",
             "Formule GP",
             "Full contact",
             "Futsal",



             "Giraviation",
             "Goalball",
             "Golf",
             "Grappling",
             "Gymnastique",



             "Haltérophilie",
             "Handball",
             "Hapkido",
             "Hip hop",
             "Hockey subaquatique",
             "Hockey sur gazon",
             "Hockey sur glace",
             "Horse ball",
             "Hurling",



             "Iaido",



             "Jeet kune do",
     "Jet ski",
     "Jianzi",
     "Jiu-jitsu brésilien",
     "Joutes nautiques",
     "Ju Jitsu traditionnel",
     "Judo",



     "Kabaddi",
     "Kalarippayat",
     "Kali Arnis Eskrima",
     "Karaté",
     "Karting",
     "Kempo",
     "Kendo",
     "Kenjutsu",
     "Kick boxing",
     "Kin ball",
     "Kitesurf",
     "Kizomba",
     "Kobudo",
     "Korfball",
     "Krav maga",
     "Kung fu",
     "Kyokushinkai",
     "Kyudo",



     "Lacrosse",
     "Legends football league",
     "Luge",
     "Lutte",
     "Lutte sénégalaise",



     "Marche aquatique",
     "Marche athlétique",
     "Marche nordique",
     "Marche sportive",
     "Mixed Martial Arts",
     "Modélisme",
     "Modern' jazz",
     "Moto ball",
     "Moto cross",
     "Moto GP",
     "Motoneige",
     "Mountainboard",
     "Musculation",



     "Nage en eau libre",
     "Nage en eau vive",
     "Natation",
     "Natation artistique",
     "Netball",
     "NinjutsuN",



     "Padel",
     "Paintball",
     "Pancrace",
     "Parachutisme",
     "Paramoteur",
     "Parapente",
     "Parkour",
     "Patinage artistique",
     "Patinage de vitesse",
     "Pelote basque",
     "Penchak Silat",
     "Pentathlon moderne",
     "Pesapallo",
     "Pétanque",
     "Peteca",
     "Pilates",
     "Planche à voile",
     "Plongée sous marine",
     "Plongeon",
     "Poker",
     "Pole dance",
     "PoloPolo",
     "Powerkite",


     "Qi gong",
     "Quad",
     "Qwan ki do",

            "Rafting",
            "Ragga",
            "Raid",
            "Rallycross",
            "Rallye",
            "Randonnée","Rink hockey","Rock","Rodéo","Roller","Rugby","Rugby subaquatique",
            "Salsa","Samba","Sambo","Sarbacane","Saut à la perche","Saut en longueur","Sauvetage","Self défense",
            "Sepak takraw","Skateboard","Skeleton","Ski","Ski acrobatique","Ski alpin","Ski de fond",
            "Ski nautique","Skicross","Slackline","Slamball","Snorkeling","Snowboard","Snowkite","Softball","Speed riding",
            "Spéléologie","Spinning","Squash","Sumo","Surf","Taekwondo","Taï chi",
            "Tambourin","Tango","Tchoukball","Tennis","Tennis de table","Teqball","Tir",
            "Tir à l'arc","Tractor pulling","Trail","Traîneaux","Trampoline",
            "Triathlon","Tricking","Trottinette","Tumbling","Twirling baton",
            "ULM","Ultimate","Ultimate fresbee","Varappe","Vélo","Voile","Volleyball",
            "Voltige","Vovinam Viet Vo Dao","VTT","Wakeboard","Waterpolo",
            "Watfit","Wing chun","Wingsuit","Yoga","Yoseikan budo","Yukigassen","Zumba",
    };
}
