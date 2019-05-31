package ovh.quiquelhappy.mcplugins.purebans.objects;

public class message {
    String player_name;
    String player_uuid;
    Long epoch;
    String msg;

    // constructor has type of data that is required
    public message(String player_name, String player_uuid, Long epoch, String msg)
    {
        // initialize the input variable from main
        // function to the global variable of the class
        this.player_name = player_name;
        this.player_uuid = player_uuid;
        this.epoch = epoch;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
