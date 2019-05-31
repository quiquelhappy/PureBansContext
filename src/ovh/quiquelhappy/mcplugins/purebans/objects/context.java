package ovh.quiquelhappy.mcplugins.purebans.objects;

import org.bukkit.World;

import java.util.List;

public class context {
    String uuid;
    List<message> message_list;
    String operator_uuid;
    World operator_world;
    Integer operator_x;
    Integer operator_y;
    Integer operator_z;
    String player_uuid;
    World player_world;
    Integer player_x;
    Integer player_y;
    Integer player_z;
    Long epoch;


    public context(String uuid, List<message> message_list, String operator_uuid, World operator_world, Integer operator_x, Integer operator_y, Integer operator_z, String player_uuid, World player_world, Integer player_x, Integer player_y, Integer player_z, Long epoch)
    {
        this.uuid=uuid;
        this.message_list=message_list;
        this.operator_uuid=operator_uuid;
        this.operator_world=operator_world;
        this.operator_x=operator_x;
        this.operator_y=operator_y;
        this.operator_z=operator_z;
        this.player_uuid=player_uuid;
        this.player_world=player_world;
        this.player_x=player_x;
        this.player_y=player_y;
        this.player_z=player_z;
        this.epoch=epoch;
    }

    public String getUuid() {
        return uuid;
    }

    public List<message> getMessage_list() {
        return message_list;
    }

    public String getOperator_uuid() {
        return operator_uuid;
    }

    public World getOperator_world() {
        return operator_world;
    }

    public Integer getOperator_x() {
        return operator_x;
    }

    public Integer getOperator_y() {
        return operator_y;
    }

    public Integer getOperator_z() {
        return operator_z;
    }

    public String getPlayer_uuid() {
        return player_uuid;
    }

    public World getPlayer_world() {
        return player_world;
    }

    public Integer getPlayer_x() {
        return player_x;
    }

    public Integer getPlayer_y() {
        return player_y;
    }

    public Integer getPlayer_z() {
        return player_z;
    }

    public Long getEpoch() {
        return epoch;
    }
}
