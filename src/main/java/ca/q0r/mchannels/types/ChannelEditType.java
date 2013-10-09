package ca.q0r.mchannels.types;

import java.util.HashMap;

public enum ChannelEditType {
    DEFAULT("default", Boolean.class),
    PASSWORD("password", String.class),
    NAME("name", String.class),
    PREFIX("prefix", String.class),
    SUFFIX("suffix", String.class),
    DISTANCE("distance", Integer.class);

    private final String name;
    private final Class<?> clazz;

    private static final HashMap<String, ChannelEditType> nMap = new HashMap<String, ChannelEditType>();

    static {
        for (ChannelEditType type : values()) {
            nMap.put(type.name.toLowerCase(), type);
        }
    }

    ChannelEditType(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public Class<?> getOptionClass() {
        return clazz;
    }

    public static ChannelEditType fromName(String name) {
        if (name == null) {
            return null;
        }

        return nMap.get(name.toLowerCase());
    }
}