package ca.q0r.mchannels.channels.types;

import ca.q0r.mchannels.channels.Channel;
import ca.q0r.mchannels.types.ChannelType;
import ca.q0r.mchat.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Global extends Channel {
    public Global(String name, String prefix, String suffix) {
        super(name, ChannelType.GLOBAL, prefix, suffix);
    }

    @Override
    public void sendMessage(Player sender, String format) {
        if (sender == null || format == null) {
            return;
        }

        String msg = format.replace("+channel", MessageUtil.addColour(prefix + name + suffix));

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            sendMessage(sender, player, msg);
        }

        MessageUtil.log(msg);
    }

    public void sendMessage(Player sender, Player player, String message) {
        player.sendMessage(message);
    }
}
