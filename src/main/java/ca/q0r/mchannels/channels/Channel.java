package ca.q0r.mchannels.channels;

import ca.q0r.mchannels.types.ChannelType;
import ca.q0r.mchannels.yml.locale.LocaleType;
import ca.q0r.mchat.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;

public abstract class Channel {
    protected HashSet<Occupant> occupants;
    protected String name, prefix, suffix;
    protected ChannelType type;
    protected Boolean defaulted;

    public Channel(String name, ChannelType type, String prefix, String suffix) {
        this.occupants = new HashSet<Occupant>();

        this.name = name.toLowerCase();
        this.prefix = prefix;
        this.suffix = suffix;

        this.type = type;

        this.defaulted = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            return;
        }

        this.name = name.toLowerCase();
    }

    public ChannelType getType() {
        return type;
    }

    public HashSet<Occupant> getOccupants() {
        return occupants;
    }

    public HashSet<Occupant> getActiveOccupants() {
        HashSet<Occupant> set = new HashSet<Occupant>();

        for (Occupant occupant : getOccupants()) {
            if (occupant.getState()) {
                set.add(occupant);
            }
        }

        return set;
    }

    public void add(Occupant occupant) {
        occupants.add(occupant);
    }

    public void remove(Occupant occupant) {
        occupants.remove(occupant);
    }

    public Occupant getOccupant(UUID uuid) {
        for (Occupant occupant : occupants) {
            if (occupant.getUuid().toString().equalsIgnoreCase(uuid.toString())) {
                return occupant;
            }
        }

        return null;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        if (prefix == null) {
            return;
        }

        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        if (suffix == null) {
            return;
        }

        this.suffix = suffix;
    }

    public Boolean isDefault() {
        return defaulted;
    }

    public void setDefault(Boolean defaulted) {
        this.defaulted = defaulted != null ? defaulted : false;
    }

    public void sendMessage(Player sender, String format) {
        if (sender == null || format == null) {
            return;
        }

        String msg = format.replace("+channel", MessageUtil.addColour(prefix + name + suffix));

        for (Occupant occupant : getActiveOccupants()) {
            Player recipient = Bukkit.getPlayer(occupant.getUuid());

            if (recipient != null) {
                sendMessage(sender, recipient, msg);
            }
        }

        MessageUtil.log(msg);
    }

    protected abstract void sendMessage(Player sender, Player recipient, String message);

    public void broadcastMessage(String message) {
        if (message == null) {
            return;
        }

        String format = LocaleType.FORMAT_BROADCAST.getRaw();
        String msg = format.replace("+channel", MessageUtil.addColour(prefix + name + suffix)).replace("+msg", message);

        for (Occupant occupant : getActiveOccupants()) {
            Player recipient = Bukkit.getPlayer(occupant.getUuid());

            if (recipient != null) {
                recipient.sendMessage(msg);
            }
        }

        MessageUtil.log(msg);
    }
}