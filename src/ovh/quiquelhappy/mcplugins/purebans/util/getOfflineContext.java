package ovh.quiquelhappy.mcplugins.purebans.util;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import ovh.quiquelhappy.mcplugins.purebans.main;
import ovh.quiquelhappy.mcplugins.purebans.objects.context;
import ovh.quiquelhappy.mcplugins.purebans.objects.message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class getOfflineContext {
    public static context getCurrentOfflineContext(OfflinePlayer player, Player operator){
        String uuid;
        List<message> message_list;
        String operator_uuid;
        World operator_world;
        Integer operator_x;
        Integer operator_y;
        Integer operator_z;
        String player_uuid;
        Long epoch;

        uuid= UUID.randomUUID().toString();
        message_list= main.msglist;

        operator_uuid=operator.getUniqueId().toString();
        operator_world=operator.getWorld();
        operator_x=operator.getLocation().getBlockX();
        operator_y=operator.getLocation().getBlockY();
        operator_z=operator.getLocation().getBlockZ();

        player_uuid=player.getUniqueId().toString();

        epoch= Instant.now().toEpochMilli();
        return new context(uuid,message_list,operator_uuid,operator_world,operator_x,operator_y,operator_z,player_uuid, null,null,null,null,epoch);
    }
}
