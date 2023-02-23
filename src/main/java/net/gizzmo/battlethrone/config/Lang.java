package net.gizzmo.battlethrone.config;

import net.gizzmo.battlethrone.api.tools.StringTools;
import net.gizzmo.battlethrone.core.MConfig;

public enum Lang {
    Prefix("&6&lTHRONE > &f"),
    TIME_NOW("maintenant"),
    TIME_YEARS_SINGULAR("année"),
    TIME_YEARS_PLURAL("années"),
    TIME_MONTHS_SINGULAR("mois"),
    TIME_MONTHS_PLURAL("mois"),
    TIME_WEEKS_SINGULAR("semaine"),
    TIME_WEEKS_PLURAL("semaines"),
    TIME_DAYS_SINGULAR("jour"),
    TIME_DAYS_PLURAL("jours"),
    TIME_HOURS_SINGULAR("heure"),
    TIME_HOURS_PLURAL("heures"),
    TIME_MINUTES_SINGULAR("minute"),
    TIME_MINUTES_PLURAL("minutes"),
    TIME_SECONDS_SINGULAR("seconde"),
    TIME_SECONDS_PLURAL("secondes"),
    MSG_NoAccess("Vous n'avez pas accès à cette commande."),
    MSG_USAGE_ThroneAdmin("Commande non valide. \\n&6&lTHRONE > &fUtilisation correcte: /tadmin <axe|create|delete|set|list|reload>."),
    MSG_USAGE_ThroneAdminSet("Commande non valide. \\n&6&lTHRONE > &fUtilisation correcte: /tadmin set <name|money_earning|cooldown_period|command|location|hologram_location>."),
    MSG_USAGE_SubCommand("Commande non valide. \\n&6&lTHRONE > &fUtilisation correcte: %usage%"),
    MSG_PLAYER_Only("Cette commande ne peut être utilisée que par un joueur."),
    MSG_THRONE_Reward("Vous avez gagné %money_earning%$"),
    MSG_EVENT_INTERACT_AXE_FirstPosition("Première position sélectionnée (%pos%)."),
    MSG_EVENT_INTERACT_AXE_SecondPosition("Deuxième position sélectionnée (%pos%)."),
    MSG_EVENT_THRONE_ToPlayerCaptured("Vous venez de capturer le throne."),
    MSG_EVENT_THRONE_ToAllPlayerCaptured("%owner% vient de capturer un throne !"),
    MSG_EVENT_THRONE_ToPlayerEnter("Vous êtes entré dans la région d'un throne."),
    MSG_EVENT_THRONE_ToPlayerLeave("Vous avez quitté le throne."),
    MSG_THRONEADMIN_NumberFormat("Veuillez entrer un nombre valide."),
    MSG_THRONEADMIN_RELOAD_Reloaded("Les configurations ont été rechargées."),
    MSG_THRONEADMIN_AXE_GiveItem("Vous avez reçu l'outil de création."),
    MSG_THRONEADMIN_AXE_InventoryFull("Votre inventaire est plein. Impossible de recevoir l'outil de création."),
    MSG_THRONEADMIN_CREATE_AlreadyExists("Le throne [%name%] existe déjà."),
    MSG_THRONEADMIN_CREATE_AREA_NoPosition("Veuillez choisir une première position pour commencer la création !"),
    MSG_THRONEADMIN_CREATE_AREA_NoFirstPosition("Veuillez sélectionner la première position."),
    MSG_THRONEADMIN_CREATE_AREA_NoSecondPosition("Veuillez sélectionner la deuxième position."),
    MSG_THRONEADMIN_CREATE_Created("Le throne [id: %id%, name: %name%] vient d'être créé."),
    MSG_THRONEADMIN_DELETE_NotExists("Le throne %name% n'existe pas."),
    MSG_THRONEADMIN_DELETE_Deleted("Le throne vient d'être supprimé."),
    MSG_THRONEADMIN_LIST_Header("Liste des thrones (%page%/%totalpages%):"),
    MSG_THRONEADMIN_LIST_Throne(" * %name%, location: %location%"),
    MSG_THRONEADMIN_LIST_Empty("Aucun throne n'existe."),
    MSG_THRONEADMIN_SET_ThroneNotExists("Le throne %name% n'existe pas."),
    MSG_THRONEADMIN_SET_Name("Le nom du throne a été modifié. Nom actuel: %name%."),
    MSG_THRONEADMIN_SET_MoneyEarning("La quantité d'argent gagnée a été modifiée. Gain actuel: %money_earning%."),
    MSG_THRONEADMIN_SET_CooldownPeriod("Le temps de recharge entre chaque gain a été modifié. Temps actuel: %cooldown_period%."),
    MSG_THRONEADMIN_SET_POSITION_NoPosition("Veuillez choisir une première position pour pouvoir modifier la position du throne !"),
    MSG_THRONEADMIN_SET_POSITION_NoFirstPosition("Veuillez sélectionner la première position pour pouvoir modifier la position du throne."),
    MSG_THRONEADMIN_SET_POSITION_NoSecondPosition("Veuillez sélectionner la deuxième position pour pouvoir modifier la position du throne."),
    MSG_THRONEADMIN_SET_POSITION_Modified("La position du throne a été modifiée. Position actuelle : %pos%."),
    MSG_THRONEADMIN_SET_HOLOGRAM_NotEnable("Impossible d'exécuter la commande car les hologrammes sont désactivés dans le fichier de configuration."),
    MSG_THRONEADMIN_SET_HOLOGRAM_HolographicDisplayNotEnable("Impossible d'exécuter la commande car le plugin HolographicDisplays n'a pas été trouvé."),
    MSG_THRONEADMIN_SET_HOLOGRAM_NotFound("Aucun hologramme trouvé pour le throne %name%."),
    MSG_THRONEADMIN_SET_HOLOGRAM_Moved("L'hologramme a bien été déplacé. Position actuelle : %pos%."),
    MSG_THRONEADMIN_SET_COMMAND_LIST_Header("Liste des commandes pour le throne [%throne_name%] :"),
    MSG_THRONEADMIN_SET_COMMAND_LIST_Empty("Aucune commande."),
    MSG_THRONEADMIN_SET_COMMAND_LIST_Info(" - ID : %commande_id%, Commande : %commande%"),
    MSG_THRONEADMIN_SET_COMMAND_ADD_Usage("Utilisation non valide. \\n&6&lTHRONE > &fUtilisation correcte : /tadmin set command <nom_du_throne> add /commande,/commande,/commande..."),
    MSG_THRONEADMIN_SET_COMMAND_ADD_CommandInvalid("Commande invalide : %commande%"),
    MSG_THRONEADMIN_SET_COMMAND_ADD_Added("Commande [%commande%] ajoutée."),
    MSG_THRONEADMIN_SET_COMMAND_REMOVE_Usage("Utilisation non valide. \\n&6&lTHRONE > &fUtilisation correcte : /tadmin set command <nom_du_throne> remove id,id,id..."),
    MSG_THRONEADMIN_SET_COMMAND_REMOVE_IdNotFound("L'ID %commande_id% n'existe pas. Utilisez /tadmin set command <nom_du_throne> list."),
    MSG_THRONEADMIN_SET_COMMAND_REMOVE_IdInvalid("L'ID %commande_id% doit être un nombre valide."),
    MSG_THRONEADMIN_SET_COMMAND_REMOVE_Removed("Commande [%commande%] supprimée.");


    private static MConfig config;
    private final String defaultValue;

    Lang(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public static void setConfig(MConfig mconfig) {
        config = mconfig;
        load();
    }

    private static void load() {
        Lang[] langs = values();

        for (Lang lang : langs) {
            if (config.getConfig().getString(lang.getPath()) == null) {
                config.getConfig().set(lang.getPath(), lang.getDefaultValue());
            }
        }

        config.save();
    }

    public String getPath() {
        return this.name().replaceAll("_", ".");
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public String toString() {
        return this.fixColors(config.getConfig().getString(this.getPath()));
    }

    public String toMsg() {
        return this.fixColors(config.getConfig().getString(Prefix.getPath()) + config.getConfig().getString(this.getPath()));
    }

    private String fixColors(final String var1) {
        return StringTools.fixColors(var1);
    }
}
