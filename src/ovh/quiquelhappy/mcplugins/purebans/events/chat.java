package ovh.quiquelhappy.mcplugins.purebans.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ovh.quiquelhappy.mcplugins.purebans.main;
import ovh.quiquelhappy.mcplugins.purebans.objects.message;

import java.time.Instant;
import java.util.List;

public class chat implements Listener {
    @EventHandler
    public void AsyncPlayerChatEvent(AsyncPlayerChatEvent e){
        String player_name=e.getPlayer().getName();
        String player_uuid=e.getPlayer().getUniqueId().toString();
        Long timestamp=Instant.now().toEpochMilli();
        String msg=e.getMessage();
        msg = msg.replace("\"", "");
        msg = msg.replace("'", "");
        msg = msg.replace("[", "");
        msg = msg.replace("]", "");

        message currentmsg = new message(player_name,player_uuid,timestamp,msg);

        if(main.msglist.size()>=1){
            message lastmsg = main.msglist.get(main.msglist.size()-1);
            if(!lastmsg.getMsg().equals(currentmsg.getMsg())){
                main.msglist.add(currentmsg);
            }
        } else {
            main.msglist.add(currentmsg);
        }

        if(main.msglist.size() > 100){
            main.msglist.remove(0);
        }
    }
}
