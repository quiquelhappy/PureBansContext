package ovh.quiquelhappy.mcplugins.purebans;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import ovh.quiquelhappy.mcplugins.purebans.commands.punish;
import ovh.quiquelhappy.mcplugins.purebans.events.chat;
import ovh.quiquelhappy.mcplugins.purebans.objects.message;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class main extends JavaPlugin {

    Plugin plugin = null;
    public static Connection conn = null;
    public static List<message> msglist =  new ArrayList<message>();
    public static String ServerName = "general";
    Configuration config = null;

    public void onEnable(){
        plugin=this;

        createHeader("");

        System.out.println("  _____                ____                  ");
        System.out.println(" |  __ \\              |  _ \\                 ");
        System.out.println(" | |__) |   _ _ __ ___| |_) | __ _ _ __  ___ ");
        System.out.println(" |  ___/ | | | '__/ _ \\  _ < / _` | '_ \\/ __|");
        System.out.println(" | |   | |_| | | |  __/ |_) | (_| | | | \\__ \\");
        System.out.println(" |_|    \\__,_|_|  \\___|____/ \\__,_|_| |_|___/");

        createHeader("CONFIG");

        if ((new File("plugins" + File.separator + "PureBans" + File.separator + "config.yml")).isFile()) {
            System.out.println("[PureBans] Loading config");
        } else {
            System.out.println("[PureBans] Creating config");
            this.saveDefaultConfig();
            this.getConfig().options().copyDefaults(true);
        }

        config = this.getConfig();

        ServerName=config.getString("server.name");

        String direction = config.getString("mysql.direction");
        String port = config.getString("mysql.port");
        String database = config.getString("mysql.database");
        String username = config.getString("mysql.username");
        String password = config.getString("mysql.password");
        Boolean ssl = config.getBoolean("mysql.SSL");

        try {
            createHeader("SQL DATABASE");
            conn = DriverManager.getConnection("jdbc:mysql://"+direction+":"+port+"/"+database+"?user="+username+"&password="+password+"&useSSL="+ssl);
            System.out.println("[PureBans] Connected to the database");
        } catch (SQLException e) {
            System.out.println("[PureBans] Couldn't connect to the database");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }

        if(conn==null){
            getServer().getPluginManager().disablePlugin(this);
        } else {

            Boolean general = helloGeneral();
            Boolean context = helloContext();

            if(general&&context){

                System.out.println("[PureBans] Both tables exist");
                setupListeners();

            } else {
                if(!general){
                    System.out.println("[PureBans] General table doesn't exists");
                    if(setupDatabase(database, true, false)){
                        System.out.println("[PureBans] Created general table");
                        setupListeners();
                    } else {
                        getServer().getPluginManager().disablePlugin(this);
                    }
                }


                if(!context){
                    System.out.println("[PureBans] Context table doesn't exists");
                    if(setupDatabase(database, false, true)){
                        System.out.println("[PureBans] Created context table");
                        setupListeners();
                    } else {
                        getServer().getPluginManager().disablePlugin(this);
                    }
                }
            }


            // MOVED TO BUNGEE Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::checkUnchecked, 1000, 30000);
        }
    }

    /*

    public void checkUnchecked(){

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT chat_points,gameplay_points,player,operator,contextid FROM pure_bans WHERE checked = 0");
            while (rs.next()) {


                Statement stmt2 = conn.createStatement();
                ResultSet rs2 = stmt2.executeQuery("SELECT username FROM luckperms_players WHERE uuid = '"+rs.getString("player")+"'");

                if(rs2.next()){
                    System.out.println("[PureBans] Executing punishment for "+rs2.getString("username")+", #"+rs.getString("contextid")+". Chat points: "+rs.getInt("chat_points")+", Gameplay points: "+rs.getInt("gameplay_points")+".");

                    String player = rs2.getString("username");
                    String id = rs.getString("contextid");
                    Integer chat = rs.getInt("chat_points");
                    Integer gameplay = rs.getInt("gameplay_points");

                    if(chat>0&&chat<=50){
                        // warning
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "warn "+player+" Please, improve your behavior. Appeal here: https://www.purevanilla.es/punishments/appeal?id="+id);
                    } else if(chat>50&&chat<=150){
                        // temp mute 24h
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tempmute "+player+" 24h Please, improve your behavior. Appeal here: https://www.purevanilla.es/punishments/appeal?id="+id);
                    } else if(chat>150&&chat<=250){
                        // temp mute 1mo
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tempmute "+player+" 30d Please, improve your behavior. Appeal here: https://www.purevanilla.es/punishments/appeal?id="+id);
                    } else if(chat>250){
                        // perma mute
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mute "+player+" Please, improve your behavior. Appeal here: https://www.purevanilla.es/punishments/appeal?id="+id);
                    }

                    if(gameplay>0&&gameplay<100) {
                        // warning
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "warn "+player+" Please, improve your behavior. Appeal here: https://www.purevanilla.es/punishments/appeal?id="+id);
                    } else if(gameplay>=100&&gameplay<150){
                        // temp ban (8h)
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tempban "+player+" 8h Please, improve your behavior. Appeal here: https://www.purevanilla.es/punishments/appeal?id="+id);
                    } else if(gameplay>=150&&gameplay<300){
                        // temp ban (48h)
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tempban "+player+" 48h Please, improve your behavior. Appeal here: https://www.purevanilla.es/punishments/appeal?id="+id);
                    } else if(gameplay>=300){
                        // perma ban
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban "+player+" Please, improve your behavior. Appeal here: https://www.purevanilla.es/punishments/appeal?id="+id);
                    }

                    Statement stmt3 = conn.createStatement();
                    stmt3.executeUpdate("UPDATE `pure_bans` SET `checked`=true WHERE contextid='"+id+"'");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kick "+player+" Please, improve your behavior. Appeal here: https://www.purevanilla.es/punishments/appeal?id="+id);

                } else {
                    System.out.println("[PureBans] Player for "+rs.getString("contextid")+" was not found");
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } MOVED TO BUNGEE */

    /*
          _____        _        _                       _____             __ _
         |  __ \      | |      | |                     / ____|           / _(_)
         | |  | | __ _| |_ __ _| |__   __ _ ___  ___  | |     ___  _ __ | |_ _  __ _
         | |  | |/ _` | __/ _` | '_ \ / _` / __|/ _ \ | |    / _ \| '_ \|  _| |/ _` |
         | |__| | (_| | || (_| | |_) | (_| \__ \  __/ | |___| (_) | | | | | | | (_| |
         |_____/ \__,_|\__\__,_|_.__/ \__,_|___/\___|  \_____\___/|_| |_|_| |_|\__, |
                                                                                __/ |
                                                                               |___/
     */

    /* CHECKS IF THE TABLE EXISTS */

    public boolean helloGeneral(){
        DatabaseMetaData dbm;
        {
            try {
                dbm = main.conn.getMetaData();
                ResultSet tables;
                tables = dbm.getTables(null, null, "pure_bans", null);
                if (tables.next()) {
                    return true;
                }
                else {
                    return false;
                }
            } catch (SQLException e) {
                System.out.println("[PureBans] Couldn't check the stats table");
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
                return false;
            }
        }
    }

    public boolean helloContext(){
        DatabaseMetaData dbm;
        {
            try {
                dbm = main.conn.getMetaData();
                ResultSet tables2;
                tables2 = dbm.getTables(null, null, "pure_bans_context", null);
                if (tables2.next()) {
                    return true;
                }
                else {
                    return false;
                }
            } catch (SQLException e) {
                System.out.println("[PureBans] Couldn't check the stats table");
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
                return false;
            }
        }
    }

    /* CREATES THE TABLE */

    public boolean setupDatabase(String dbname, Boolean general, Boolean context){
        Statement stmt = null;
        try {
            if(general){
                stmt = conn.createStatement();
                stmt.executeUpdate("CREATE TABLE `"+dbname+"`.`pure_bans` ( `player` VARCHAR(36) NOT NULL , `operator` VARCHAR(36) NOT NULL , `chat_points` INT NOT NULL , `gameplay_points` INT NOT NULL , `adversited_other_servers` BOOLEAN NOT NULL DEFAULT FALSE , `threatened_other_users` BOOLEAN NOT NULL DEFAULT FALSE , `racist_behavior` BOOLEAN NOT NULL DEFAULT FALSE , `annoying` BOOLEAN NOT NULL DEFAULT FALSE , `harassment` BOOLEAN NOT NULL DEFAULT FALSE , `base_kill` BOOLEAN NOT NULL DEFAULT FALSE , `griefing` BOOLEAN NOT NULL DEFAULT FALSE , `major_griefing` BOOLEAN NOT NULL DEFAULT FALSE , `offensive_structures` BOOLEAN NOT NULL DEFAULT FALSE , `hacking` BOOLEAN NOT NULL DEFAULT FALSE , `server_raid` BOOLEAN NOT NULL DEFAULT FALSE , `epoch` BIGINT NOT NULL , `epoch_finish` BIGINT NULL DEFAULT NULL , `active` BOOLEAN NOT NULL DEFAULT TRUE , `contextid` VARCHAR(36) NOT NULL , `checked` BOOLEAN NOT NULL DEFAULT FALSE ) ENGINE = InnoDB;");
            }

            if(context){
                stmt = conn.createStatement();
                stmt.executeUpdate("CREATE TABLE `"+dbname+"`.`pure_bans_context` ( `server` VARCHAR(36) NOT NULL , `uuid` VARCHAR(36) NOT NULL , `chat` TEXT NULL DEFAULT NULL , `operator_world` TINYTEXT NULL DEFAULT NULL , `player_world` TINYTEXT NULL DEFAULT NULL , `player_x` INT NULL DEFAULT NULL , `player_y` INT NULL DEFAULT NULL , `player_z` INT NULL DEFAULT NULL , `operator_x` INT NULL DEFAULT NULL , `operator_y` INT NULL DEFAULT NULL , `operator_z` INT NULL DEFAULT NULL , `timestamp` BIGINT NOT NULL , `player_uuid` VARCHAR(36) NOT NULL , `operator_uuid` VARCHAR(36) NULL DEFAULT NULL ) ENGINE = InnoDB;");
            }
            return true;
        } catch (SQLException e) {
            System.out.println("[PureBans] Couldn't create the table");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            return false;
        }

    }

    /*
          _____  _             _         _    _ _   _ _
         |  __ \| |           (_)       | |  | | | (_) |
         | |__) | |_   _  __ _ _ _ __   | |  | | |_ _| |___
         |  ___/| | | | |/ _` | | '_ \  | |  | | __| | / __|
         | |    | | |_| | (_| | | | | | | |__| | |_| | \__ \
         |_|    |_|\__,_|\__, |_|_| |_|  \____/ \__|_|_|___/
                          __/ |
                         |___/
     */

    private void setupListeners(){
        this.getCommand("punish").setExecutor(new punish());
        Bukkit.getServer().getPluginManager().registerEvents(new chat(), this);
    }

    public void createHeader(String text){
        System.out.println(" ");
        System.out.println(" ");
        System.out.println(text);
        System.out.println(" ");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        createHeader("DISABLING PLUGIN");
    }

}
