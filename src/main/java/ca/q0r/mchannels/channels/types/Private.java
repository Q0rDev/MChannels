package ca.q0r.mchannels.channels.types;

import ca.q0r.mchannels.channels.Channel;
import ca.q0r.mchannels.types.ChannelType;
import org.bukkit.entity.Player;

public class Private extends Channel {
    public Private(String name, String prefix, String suffix) {
        super(name, ChannelType.PRIVATE, prefix, suffix);
    }

    public void sendMessage(Player sender, Player player, String message) {
        player.sendMessage(message);
    }
}
