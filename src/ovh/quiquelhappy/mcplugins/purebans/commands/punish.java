package ovh.quiquelhappy.mcplugins.purebans.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ovh.quiquelhappy.mcplugins.purebans.main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static org.bukkit.Bukkit.getOfflinePlayer;

public class punish implements CommandExecutor {

    static Connection conn = main.conn;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(sender instanceof Player){

            Player player = null;
            OfflinePlayer offlinePlayer = null;
            Player moderator = ((Player) sender).getPlayer();

            if(Bukkit.getPlayer(args[0])!=null){
                player = Bukkit.getPlayer(args[0]);
                System.out.println("[PureBans] selected player from online status");
            } else {
                if(args.length>0){
                    try {
                        Statement stmt = null;
                        stmt = conn.createStatement();
                        ResultSet rs = null;
                        rs = stmt.executeQuery("SELECT uuid FROM pure_stats WHERE name = '"+args[0]+"'");

                        if(rs.next()) {
                            offlinePlayer = getOfflinePlayer(UUID.fromString(rs.getString("uuid")));
                            System.out.println("[PureBans] player "+args[0]+" selected from the database");

                        } else {
                            System.out.println("[PureBans] selection tried to select a player that has never joined");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            }

            if(player!=null){
                ovh.quiquelhappy.mcplugins.purebans.util.saveContext.saveCurrentContext(player,moderator);
            } else {
                if(offlinePlayer != null){
                    ovh.quiquelhappy.mcplugins.purebans.util.saveOfflineContext.saveCurrentOfflineContext(offlinePlayer,moderator);
                } else {
                    System.out.println("[PureBans] couldn't find that player online nor in the database");
                }
            }
        }
        return true;
    }
}
