package ca.q0r.mchannels.channels;

import ca.q0r.mchannels.channels.types.*;
import ca.q0r.mchannels.types.ChannelEditType;
import ca.q0r.mchannels.types.ChannelType;
import ca.q0r.mchannels.yml.channel.ChannelYml;

import java.util.HashSet;
import java.util.Set;

public class ChannelManager {
    private static Set<Channel> channels;
    private static ChannelYml yml;

    public static void initialize() {
        channels = new HashSet<>();
        yml = new ChannelYml();

        loadChannels();
    }

    /**
     * Loads all Channels from yml to Memory.
     */
    public static void loadChannels() {
        for (String name : yml.getConfig().getKeys(false)) {
            loadChannel(name);
        }
    }

    /**
     * Loads Channel from yml to Memory.
     */
    public static void loadChannel(String name) {
        ChannelType type = ChannelType.fromName(yml.getConfig().getString(name + ".type"));
        Channel channel = null;

        String prefix = yml.getConfig().getString(name + ".prefix", "[");
        String suffix = yml.getConfig().getString(name + ".suffix", "]");
        String password = yml.getConfig().getString(name + ".password", "");
        Integer distance = yml.getConfig().getInt(name + ".distance", -1);
        Boolean defaulted = yml.getConfig().getBoolean(name + ".default", false);

        if (type == null) {
            type = ChannelType.GLOBAL;
        }

        switch (type) {
            case GLOBAL:
                channel = new Global(name, prefix, suffix);
                break;
            case CHUNK:
                channel = new Chunk(name, prefix, suffix);
                break;
            case PRIVATE:
                channel = new Chunk(name, prefix, suffix);
                break;
            case PASSWORD:
                channel = new Password(name, prefix, suffix, password);
                break;
            case LOCAL:
                channel = new Local(name, prefix, suffix, distance);
                break;
            case WORLD:
                channel = new World(name, prefix, suffix);
        }

        channels.add(channel);

        if (defaulted) {
            setDefaultChannel(channel);
        }
    }

    /**
     * Reloads all Channels from yml to Memory.
     */
    public static void reloadChannels() {
        yml = new ChannelYml();

        loadChannels();
    }

    /**
     * Looks for a Channel in Memory.
     *
     * @param name Name of Channel being sought after.
     * @return Channel being sought after or null.
     */
    public static Channel getChannel(String name) {
        for (Channel channel : channels) {
            if (channel.getName().equalsIgnoreCase(name)) {
                return channel;
            }
        }

        return null;
    }

    public static Set<Channel> getChannels() {
        loadChannels();

        return channels;
    }

    /**
     * Saves a Channel to yml.
     *
     * @param channel Channel being saved.
     */
    public static void saveChannel(Channel channel) {
        String name = channel.getName();

        yml.set(name.toLowerCase() + ".type", channel.getType().getName());
        yml.set(name.toLowerCase() + ".prefix", channel.getPrefix());
        yml.set(name.toLowerCase() + ".suffix", channel.getSuffix());

        if (channel instanceof Password) {
            yml.set(name.toLowerCase() + ".password", ((Password) channel).getPassword());
        }

        if (channel instanceof Local) {
            yml.set(name.toLowerCase() + ".distance", ((Local) channel).getDistance());
        }

        yml.set(name.toLowerCase() + ".default", channel.isDefault());

        yml.save();
    }

    /**
     * Saves all Channels in Memory to yml.
     */
    public static void saveChannels() {
        for (Channel channel : channels) {
            saveChannel(channel);
        }
    }

    /**
     * Reads Default Channel from Memory.
     *
     * @return Default Channel or null.
     */
    public static Channel getDefaultChannel() {
        for (Channel channel : channels) {
            if (channel.isDefault()) {
                return channel;
            }
        }

        return null;
    }

    /**
     * Makes a Channel the Default Channel.
     *
     * @param channel Channel being defaulted.
     */
    public static void setDefaultChannel(Channel channel) {
        String name = channel.getName();
        for (Channel chann : channels) {
            if (chann.getName().equalsIgnoreCase(name)) {
                chann.setDefault(true);
                saveChannel(chann);

                break;
            }
        }

        for (Channel chan : channels) {
            if (!chan.getName().equalsIgnoreCase(name) && chan.isDefault()) {
                chan.setDefault(false);
                saveChannel(chan);
            }
        }

    }

    /**
     * Reads Player's Active Channels
     *
     * @param player Player's name being sought.
     * @return Set containing all Channels the Player is Active in.
     */
    public static HashSet<Channel> getPlayersActiveChannels(String player) {
        HashSet<Channel> channels = new HashSet<>();

        for (Channel channel : getChannels()) {
            Occupant occupant = channel.getOccupant(player);

            if (occupant != null && occupant.getState()) {
                channels.add(channel);
            }
        }

        return channels;
    }

    /**
     * Reads Player's Channels
     *
     * @param player Player's name being sought.
     * @return Set containing all Channels the Player is in.
     */
    public static Set<Channel> getPlayersChannels(String player) {
        Set<Channel> channels = new HashSet<>();

        for (Channel channel : getChannels()) {
            if (channel.getOccupant(player) != null) {
                channels.add(channel);
            }
        }

        return channels;
    }


    /**
     * Adds a Channel to yml/Memory.
     *
     * @param channel Channel being added.
     */
    public static void addChannel(Channel channel) {
        channels.add(channel);

        if (channel.isDefault()) {
            setDefaultChannel(channel);
        } else {
            saveChannel(channel);
        }
    }

    /**
     * Edits a channel.
     *
     * @param channel Channel being edited.
     * @param type    EditType being used.
     * @param option  Option being used.
     */
    public static void editChannel(Channel channel, ChannelEditType type, Object option) {
        String name = channel.getName();
        String prefix = channel.getPrefix();
        String suffix = channel.getSuffix();

        if (option.getClass() == type.getOptionClass()) {
            switch (type) {
                case NAME:
                    yml.set(channel.getName(), null);
                    channel.setName((String) option);

                    break;
                case DEFAULT:
                    setDefaultChannel(channel);

                    break;
                case DISTANCE:
                    removeChannel(channel);
                    channel = new Local(name, prefix, suffix, (Integer) option);

                    break;
                case PASSWORD:
                    removeChannel(channel);
                    channel = new Password(name, prefix, suffix, (String) option);
                    break;
                case PREFIX:
                    channel.setPrefix((String) option);

                    break;
                case SUFFIX:
                    channel.setSuffix((String) option);

                    break;
            }

            saveChannel(channel);
        }
    }

    /**
     * Removes a Channel from yml/Memory.
     *
     * @param channel Channel being removed.
     */
    public static void removeChannel(Channel channel) {
        for (Channel chan : channels) {
            if (chan.getName().equalsIgnoreCase(channel.getName())) {
                channels.remove(chan);

                break;
            }
        }

        yml.set(channel.getName(), null);
        yml.save();
    }
}