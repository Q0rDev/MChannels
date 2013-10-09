package ca.q0r.mchannels.channels.types;

import ca.q0r.mchannels.channels.Channel;
import ca.q0r.mchannels.types.ChannelType;
import org.bukkit.entity.Player;

public class World extends Channel {
    public World(String name, String prefix, String suffix) {
        super(name, ChannelType.WORLD, prefix, suffix);
    }

    public void sendMessage(Player sender, Player player, String message) {
        if (player.getWorld().getName().equals(sender.getWorld().getName())) {
            player.sendMessage(message);
        }
    }
}
