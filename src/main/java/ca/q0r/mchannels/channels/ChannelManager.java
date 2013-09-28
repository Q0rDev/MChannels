package ca.q0r.mchannels.channels;

import ca.q0r.mchannels.types.ChannelEditType;
import ca.q0r.mchannels.types.ChannelType;

import java.util.HashSet;
import java.util.Set;

public class ChannelManager {
    private static Set<Channel> channels;
    private static ChannelYml yml;

    public static void initialize() {
        channels = new HashSet<Channel>();
        yml = new ChannelYml();

        loadChannels();
    }

    public static Set<Channel> getChannels() {
        loadChannels();

        return channels;
    }

    /**
     * Loads Channels from yml to Memory.
     */
    public static void loadChannels() {
        for (String key : yml.getConfig().getKeys(false)) {
            ChannelType type = ChannelType.fromName(yml.getConfig().getString(key + ".type"));
            String prefix = yml.getConfig().getString(key + ".prefix", "[");
            String suffix = yml.getConfig().getString(key + ".suffix", "]");
            Boolean passworded = yml.getConfig().getBoolean(key + ".passworded", false);
            String password = yml.getConfig().getString(key + ".password", "");
            Integer distance = yml.getConfig().getInt(key + ".distance", -1);
            Boolean defaulted = yml.getConfig().getBoolean(key + ".default", false);

            if (type == null) {
                type = ChannelType.GLOBAL;
            }

            channels.add(new Channel(key.toLowerCase(), type, prefix, suffix, passworded, password, distance, defaulted));
        }
    }

    /**
     * Loads a Channel from the config.
     */
    public static void loadChannel(String name) {
        ChannelType type = ChannelType.fromName(yml.getConfig().getString(name + ".type"));
        String prefix = yml.getConfig().getString(name + ".prefix", "[");
        String suffix = yml.getConfig().getString(name + ".suffix", "]");
        Boolean passworded = yml.getConfig().getBoolean(name + ".passworded", false);
        String password = yml.getConfig().getString(name + ".password", "");
        Integer distance = yml.getConfig().getInt(name + ".distance", -1);
        Boolean defaulted = yml.getConfig().getBoolean(name + ".default", false);

        channels.add(new Channel(name.toLowerCase(), type, prefix, suffix, passworded, password, distance, defaulted));
    }


    /**
     * Reloads Channels from yml to Memory.
     */
    public static void reloadChannels() {
        yml = new ChannelYml();

        for (String key : yml.getConfig().getKeys(false)) {
            if (getChannel(key) != null) {
                Channel channel = getChannel(key);

                channel.setType(ChannelType.fromName(yml.getConfig().getString(key + ".type")));
                channel.setPrefix(yml.getConfig().getString(key + ".prefix", "["));
                channel.setSuffix(yml.getConfig().getString(key + ".suffix", "]"));
                channel.setPassword(yml.getConfig().getString(key + ".password", ""));
                channel.setPassworded(yml.getConfig().getBoolean(key + ".passworded", false), channel.getPassword());
                channel.setDistance(yml.getConfig().getInt(key + ".distance", -1));
                channel.setDefault(yml.getConfig().getBoolean(key + ".default", false));
            } else {
                loadChannel(key);
            }
        }
    }

    /**
     * Loads Channels from yml to Memory.
     * @param name Name of Channel being created.
     * @param type Type of Channel being created.
     * @param prefix Prefix of Channel being created.
     * @param suffix Suffix of Channel being created.
     * @param passworded Is Channel passworded?
     * @param password Channel's Password.
     * @param distance Distance used if Type is local.
     * @param defaulted Is Channel the Default channel.
     */
    public static void createChannel(String name, ChannelType type, String prefix, String suffix, Boolean passworded, String password, Integer distance, Boolean defaulted) {
        channels.add(new Channel(name.toLowerCase(), type, prefix, suffix, passworded, password, distance, defaulted));

        yml.getConfig().set(name.toLowerCase() + ".type", type.getName());
        yml.getConfig().set(name.toLowerCase() + ".prefix", prefix);
        yml.getConfig().set(name.toLowerCase() + ".suffix", suffix);
        yml.getConfig().set(name.toLowerCase() + ".passworded", passworded);
        yml.getConfig().set(name.toLowerCase() + ".password", password);
        yml.getConfig().set(name.toLowerCase() + ".distance", distance);
        yml.getConfig().set(name.toLowerCase() + ".default", defaulted);

        if (defaulted) {
            setDefaultChannel(name);
        }

        yml.save();
    }

    /**
     * Removes a Channel from yml/Memory.
     * @param name Name of Channel being removed.
     */
    public static void removeChannel(String name) {
        Boolean isReal = false;

        for (Channel channel : channels) {
            if (channel.getName().equalsIgnoreCase(name)) {
                isReal = true;
                break;
            }
        }

        if (isReal) {
            channels.remove(getChannel(name));

            yml.getConfig().set(name, null);

            yml.save();
        }
    }

    /**
     * Looks for a Channel in Memory.
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

    /**
     * Saves all Channels in Memory to yml.
     */
    public static void saveChannels() {
        for (Channel channel : channels) {
            saveChannel(channel);
        }
    }

    /**
     * Saves a Channel from Memory to config.
     * @param channel Channel being saved.
     */
    public static void saveChannel(Channel channel) {
        yml.getConfig().set(channel.getName().toLowerCase() + ".type", channel.getType().getName().toLowerCase());
        yml.getConfig().set(channel.getName().toLowerCase() + ".prefix", channel.getPrefix());
        yml.getConfig().set(channel.getName().toLowerCase() + ".suffix", channel.getSuffix());
        yml.getConfig().set(channel.getName().toLowerCase() + ".passworded", channel.isPassworded());
        yml.getConfig().set(channel.getName().toLowerCase() + ".password", channel.getPassword());
        yml.getConfig().set(channel.getName().toLowerCase() + ".distance", channel.getDistance());
        yml.getConfig().set(channel.getName().toLowerCase() + ".default", channel.isDefault());

        yml.save();
    }

    /**
     * Makes a Channel the Default Channel.
     * @param name Name of Channel being defaulted.
     */
    public static void setDefaultChannel(String name) {
        Boolean hasDefaulted = false;

        for (Channel channel : channels) {
            if (channel.getName().equalsIgnoreCase(name)) {
                channel.setDefault(true);
                saveChannel(channel);
                hasDefaulted = true;
            } else if (channel.isDefault() && hasDefaulted) {
                channel.setDefault(false);
                saveChannel(channel);
            }
        }
    }

    /**
     * Reads Default Channel from Memory.
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
     * Reads Player's Active Channels
     * @param player Player's name being sought.
     * @return Set containing all Channels the Player is Active in.
     */
    public static Set<Channel> getPlayersActiveChannels(String player) {
        Set<Channel> channels = new HashSet<Channel>();

        for (Channel channelz : getChannels()) {
            if (channelz.getActiveOccupants().contains(player)) {
                channels.add(channelz);
            }
        }

        return channels;
    }

    /**
     * Reads Player's Channels
     * @param player Player's name being sought.
     * @return Set containing all Channels the Player is in.
     */
    public static Set<Channel> getPlayersChannels(String player) {
        Set<Channel> channels = new HashSet<Channel>();

        for (Channel channelz : getChannels()) {
            if (channelz.getOccupants().contains(player)) {
                channels.add(channelz);
            }
        }

        return channels;
    }

    /**
     * Loads Channels from yml to Memory.
     * @param channel Name of Channel being edited.
     * @param type EditType being used.
     * @param option Option being used.
     */
    public static void editChannel(Channel channel, ChannelEditType type, Object option) {
        if (option.getClass() == type.getOptionClass()) {
            if (type.getName().equalsIgnoreCase("name")) {
                yml.getConfig().set(channel.getName(), null);

                channel.setName((String) option);
            } else if (type.getName().equalsIgnoreCase("default")) {
                setDefaultChannel(channel.getName());
            } else if (type.getName().equalsIgnoreCase("distance")) {
                channel.setDistance((Integer) option);
            } else if (type.getName().equalsIgnoreCase("password")) {
                channel.setPassword((String) option);
            } else if (type.getName().equalsIgnoreCase("passworded")) {
                channel.setPassworded((Boolean) option, channel.getPassword());
            } else if (type.getName().equalsIgnoreCase("prefix")) {
                channel.setPrefix((String) option);
            } else if (type.getName().equalsIgnoreCase("suffix")) {
                channel.setSuffix((String) option);
            } else if (type.getName().equalsIgnoreCase("type")) {
                channel.setType((ChannelType) option);
            }

            saveChannel(channel);
        }
    }
}
