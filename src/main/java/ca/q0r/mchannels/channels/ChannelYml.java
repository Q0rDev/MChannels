package ca.q0r.mchannels.channels;

import ca.q0r.mchat.configs.Yml;

import java.io.File;

public class ChannelYml extends Yml {
    public ChannelYml() {
        super(new File("plugins/MChannels/channels.yml"), "MChat Channels");

        if (!file.exists()) {
            loadDefaults();
        }
    }

    public void loadDefaults() {
        set("global.prefix", "[");
        set("global.suffix", "]");
        set("global.type", "global");
        set("global.distance", 0);
        set("global.default", true);
        set("global.prefix", "[");
        set("global.suffix", "]");
        set("global.type", "global");
        set("global.distance", 60);
        set("global.default", false);
        set("private.prefix", "[");
        set("private.suffix", "]");
        set("private.type", "private");
        set("private.distance", 0);
        set("private.default", false);
        set("world.prefix", "[");
        set("world.suffix", "]");
        set("world.type", "world");
        set("world.distance", 0);
        set("world.default", false);
        set("chunk.prefix", "[");
        set("chunk.suffix", "]");
        set("chunk.type", "chunk");
        set("chunk.distance", 5);
        set("chunk.default", false);
        set("password.prefix", "[");
        set("password.suffix", "]");
        set("password.type", "password");
        set("password.distance", 0);
        set("password.password", "hello");
        set("password.passworded", true);
        set("password.default", false);

        save();
    }
}
