package ca.q0r.mchannels.channels.types;

import ca.q0r.mchannels.channels.Channel;
import ca.q0r.mchannels.types.ChannelType;
import org.bukkit.entity.Player;

public class Chunk extends Channel {
    public Chunk(String name, String prefix, String suffix) {
        super(name, ChannelType.CHUNK, prefix, suffix);
    }

    public void sendMessage(Player sender, Player player, String message) {
        if (player.getWorld().getName().equals(sender.getWorld().getName())
                && player.getLocation().getChunk() == sender.getLocation().getChunk()) {
            player.sendMessage(message);
        }
    }
}
