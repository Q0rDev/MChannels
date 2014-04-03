package ca.q0r.mchannels.events;

import ca.q0r.mchannels.channels.Channel;
import ca.q0r.mchannels.channels.ChannelManager;
import ca.q0r.mchannels.channels.Occupant;
import ca.q0r.mchannels.yml.locale.LocaleType;
import ca.q0r.mchat.api.Parser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashSet;
import java.util.Set;

public class ChatListener implements Listener {
    public ChatListener() {
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled() || !event.getMessage().startsWith("@")) {
            return;
        }

        String[] arr = event.getMessage().split(" ");

        String cName = arr[0].replace("@", "");
        String msg = arr.length > 1 ? arr[1] : "";

        Player player = event.getPlayer();
        HashSet<Channel> channels = new HashSet<>();

        if (cName.equalsIgnoreCase("all")) {
            channels = ChannelManager.getPlayersActiveChannels(player.getName());
        } else {
            Channel channel = ChannelManager.getChannel(cName);

            if (channel != null) {
                Occupant occupant = channel.getOccupant(player.getName());

                if (occupant != null && occupant.getState()) {
                    channels.add(channel);
                }
            }
        }

        if (channels.size() < 1) {
            return;
        }

        for (Channel channel : channels) {
            channel.sendMessage(player, getFormat(player, msg));
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String pName = event.getPlayer().getName();
        String world = event.getPlayer().getWorld().getName();

        Channel dChannel = ChannelManager.getDefaultChannel();
        Set<Channel> cChannel = ChannelManager.getPlayersActiveChannels(pName);

        if (cChannel.size() < 1 && dChannel != null) {
            dChannel.add(new Occupant(pName));
            dChannel.broadcastMessage(Parser.parseMessage(pName, world, "", LocaleType.FORMAT_JOIN.getRaw()));
        }
    }

    private String getFormat(Player player, String message) {
        String format = LocaleType.FORMAT_MESSAGE.getRaw();

        return Parser.parseMessage(player.getName(), player.getWorld().getName(), message, format);
    }
}