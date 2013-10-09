package ca.q0r.mchannels.channels.types;

import ca.q0r.mchannels.channels.Channel;
import ca.q0r.mchannels.types.ChannelType;
import org.bukkit.entity.Player;

public class Local extends Channel {
    Integer distance;

    public Local(String name, String prefix, String suffix, Integer distance) {
        super(name, ChannelType.LOCAL, prefix, suffix);

        this.distance = distance;
    }

    public void sendMessage(Player sender, Player player, String message) {
        if (player.getWorld().getName().equals(sender.getWorld().getName())
                && player.getLocation().distance(sender.getLocation()) < distance) {
            player.sendMessage(message);
        }
    }

    public void setDistance(Integer distance) {
        if (distance == null) {
            return;
        }

        this.distance = distance;
    }

    public Integer getDistance() {
        return distance;
    }
}
