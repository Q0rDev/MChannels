package ca.q0r.mchannels.channels.types;

import ca.q0r.mchannels.channels.Channel;
import ca.q0r.mchannels.types.ChannelType;
import org.bukkit.entity.Player;

public class Password extends Channel {
    private String password;

    public Password(String name, String prefix, String suffix, String password) {
        super(name, ChannelType.PASSWORD, prefix, suffix);

        this.password = password;
    }

    public void sendMessage(Player sender, Player player, String message) {
        player.sendMessage(message);
    }

    public void setPassword(String password) {
        if (password == null) {
            return;
        }

        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
