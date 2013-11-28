package ca.q0r.mchannels.yml.locale;

import ca.q0r.mchat.yml.Yml;

import java.io.File;

public class LocaleYml extends Yml {
    public LocaleYml() {
        super(new File("plugins/MChannels/locale.yml"), "MChannels Locale");
    }

    public void loadDefaults() {
        checkOption("format.broadcast", "@+channel => +msg");
        checkOption("format.message", "@+channel &f+p+dn+s&f => +msg");

        checkOption("format.join", "&f+p+dn+s&f has joined!");
        checkOption("format.leave", "&f+p+dn+s&f has left!");

        save();
    }
}