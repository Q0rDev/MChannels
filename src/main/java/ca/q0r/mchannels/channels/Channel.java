package ca.q0r.mchannels.channels;

import ca.q0r.mchannels.types.ChannelType;
import ca.q0r.mchat.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Channel {
    protected HashMap<String, Boolean> occupants;
    protected String name, prefix, suffix;
    protected ChannelType type;
    protected Boolean defaulted;

    public Channel(String name, ChannelType type, String prefix, String suffix) {
        this.occupants = new HashMap<String, Boolean>();

        this.name = name.toLowerCase();
        this.prefix = prefix;
        this.suffix = suffix;

        this.type = type;

        this.defaulted = false;
    }

    public void setName(String name) {
        if (name == null) {
            return;
        }

        this.name = name.toLowerCase();
    }

    public String getName() {
        return name;
    }

    public void setType(ChannelType type) {
        if (type == null) {
            type = ChannelType.GLOBAL;
        }

        this.type = type;
    }

    public ChannelType getType() {
        return type;
    }

    public Set<String> getOccupants() {
        return occupants.keySet();
    }

    public Set<String> getActiveOccupants() {
        Set<String> set = new HashSet<String>();

        for (Map.Entry<String, Boolean> entry : occupants.entrySet()) {
            if (entry.getValue()) {
                set.add(entry.getKey());
            }
        }

        return set;
    }

    public void addOccupant(String occupant) {
        addOccupant(occupant, true);
    }

    public void addOccupant(String occupant, Boolean state) {
        if (occupant == null || state == null) {
            return;
        }

        occupants.put(occupant, state);
    }

    public void removeOccupant(String occupant) {
        if (occupant == null || occupants.get(occupant) == null) {
            return;
        }

        occupants.remove(occupant);
    }

    public Boolean getOccupantAvailability(String occupant) {
        if (occupant == null || occupants.get(occupant) == null) {
            return false;
        }

        return occupants.get(occupant);
    }

    public void setOccupantAvailability(String occupant, Boolean state) {
        if (occupant == null || state == null || occupants.get(occupant) == null) {
            return;
        }

        occupants.put(occupant, state);
    }

    public void setPrefix(String prefix) {
        if (prefix == null) {
            return;
        }

        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setSuffix(String suffix) {
        if (suffix == null) {
            return;
        }

        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }

    public Boolean isDefault() {
        return defaulted;
    }

    public void setDefault(Boolean defaulted) {
        this.defaulted = defaulted != null ? defaulted : false;
    }

    public void sendMessage(Player sender, String message) {
        if (sender == null || message == null) {
            return;
        }

        String msg = MessageUtil.addColour(prefix + name + suffix) + " " + message;

        for (String names : getActiveOccupants()) {
            Player player = Bukkit.getServer().getPlayer(names);

            if (player == null) {
                continue;
            }

            MessageUtil.logFormatted("Name:" + this.getName());

            sendMessage(sender, player, msg);
        }

        MessageUtil.log(msg);
    }

    protected abstract void sendMessage(Player sender, Player player, String message);

    public void broadcastMessage(String message) {
        if (message == null) {
            return;
        }

        String msg = MessageUtil.addColour(prefix + name + suffix + " " + message);

        for (String names : getActiveOccupants()) {
            Player playerz = Bukkit.getServer().getPlayer(names);

            if (playerz == null) {
                continue;
            }

            playerz.sendMessage(msg);
        }

        MessageUtil.log(msg);
    }
}
