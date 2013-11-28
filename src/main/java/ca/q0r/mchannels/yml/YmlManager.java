package ca.q0r.mchannels.yml;

import ca.q0r.mchannels.yml.locale.LocaleYml;
import ca.q0r.mchat.yml.Yml;

public class YmlManager {
    static LocaleYml localeYml;

    public static void initialize() {
        localeYml = new LocaleYml();
        localeYml.loadDefaults();
    }

    public static Yml getYml(YmlType type) {
        switch (type) {
            case LOCALE_YML:
                return localeYml;
        }

        return null;
    }

    public static void reloadYml(YmlType type) {
        switch (type) {
            case LOCALE_YML:
                localeYml = new LocaleYml();
                localeYml.loadDefaults();
                break;
        }
    }

    public static void unload() {
        localeYml = null;
    }
}