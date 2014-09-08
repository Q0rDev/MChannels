package ca.q0r.mchannels.types;

import java.util.HashMap;

public enum ChannelType {
    GLOBAL("global"),
    LOCAL("local"),
    PRIVATE("private"),
    PASSWORD("password"),
    WORLD("world"),
    CHUNK("chunk");
    private static final HashMap<String, ChannelType> nMap = new HashMap<String, ChannelType>();
    static {
        for (ChannelType type : values()) {
            nMap.put(type.name.toLowerCase(), type);
        }
    }
    private final String name;

    ChannelType(String name) {
        this.name = name;
    }

    public static ChannelType fromName(String name) {
        if (name == null) {
            return null;
        }

        return nMap.get(name.toLowerCase());
    }

    public String getName() {
        return name;
    }
}