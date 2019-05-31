package ovh.quiquelhappy.mcplugins.purebans.util;

import com.google.gson.Gson;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import ovh.quiquelhappy.mcplugins.purebans.main;
import ovh.quiquelhappy.mcplugins.purebans.objects.context;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class saveContext {
    public static context saveCurrentContext(Player player, Player moderator){
        context context = ovh.quiquelhappy.mcplugins.purebans.util.getContext.getCurrentContext(player, moderator);

        String uuid = context.getUuid();
        String chat = new Gson().toJson(context.getMessage_list());

        String operator_uuid = context.getOperator_uuid();
        String operator_world = context.getOperator_world().getName();
        Integer operator_x = context.getOperator_x();
        Integer operator_y = context.getOperator_y();
        Integer operator_z = context.getOperator_z();

        String player_uuid = context.getPlayer_uuid();
        String player_world = context.getPlayer_world().getName();
        Integer player_x = context.getPlayer_x();
        Integer player_y = context.getPlayer_y();
        Integer player_z = context.getPlayer_z();

        Long timestamp = context.getEpoch();


        Connection conn = main.conn;
        Statement stmt;

        TextComponent msg = new TextComponent("§e§lPUREBANS §7Creating a context log");
        moderator.spigot().sendMessage(msg);

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO pure_bans_context (server, uuid, chat, operator_world, player_world, player_x, player_y, player_z, operator_x, operator_y, operator_z, timestamp, player_uuid, operator_uuid) VALUES ('"+main.ServerName+"', '"+uuid+"', '"+chat+"', '"+operator_world+"', '"+player_world+"', "+player_x+", "+player_y+", "+player_z+", "+operator_x+", "+operator_y+", "+operator_z+", "+timestamp+", '"+player_uuid+"', '"+operator_uuid+"');");

            TextComponent msg2 = new TextComponent("§e§lPUREBANS §7Complete the punishment by §lclicking here");
            msg2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eContext UUID: "+context.getUuid()).create()));
            msg2.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.purevanilla.es/punishments/new?context="+context.getUuid()));
            moderator.spigot().sendMessage(msg2);

        } catch (SQLException e) {
            TextComponent msg3 = new TextComponent("§e§lPUREBANS §7Error while creating a context log");

            moderator.spigot().sendMessage(msg3);
            e.printStackTrace();
        }

        return context;
    }
}
