package ca.q0r.mchannels.types;

import java.util.HashMap;

public enum ChannelCommandType {
    RELOAD("reload", "", 0),
    TYPES("types", "", 0),
    EDIT_TYPES("edittypes", "", 0),
    CREATE("create", "create [ChannelName] [ChannelType] <Password/Distance>'.", 3),
    REMOVE("remove", "remove [ChannelName]'.", 2),
    EDIT("edit", "edit [ChannelName] [EditType] [Option]'.", 4),
    JOIN("join", "join [ChannelName]'.", 2),
    LEAVE("leave", "leave [ChannelName]'.", 2),
    AWAY("away", "away [ChannelName]'.", 2),
    BACK("back", "back [ChannelName]'.", 2);

    private final String name, help;
    private final Integer length;

    private static final HashMap<String, ChannelCommandType> nMap = new HashMap<>();

    static {
        for (ChannelCommandType type : values()) {
            nMap.put(type.name.toLowerCase(), type);
        }
    }

    ChannelCommandType(String name, String help, Integer length) {
        this.name = name;
        this.help = help;
        this.length = length;
    }

    public String getPermission() {
        return "mchannel." + name;
    }

    public String getPermission(String name) {
        if (name == null) {
            return null;
        }

        return getPermission() + "." + name;
    }

    public String getHelp(String cmd) {
        if (help.isEmpty()) {
            return null;
        }

        return "Please use'/" + cmd + " " + help;
    }

    public Integer getLength() {
        return length;
    }

    public static ChannelCommandType fromName(String name) {
        if (name == null) {
            return null;
        }

        return nMap.get(name.toLowerCase());
    }
}