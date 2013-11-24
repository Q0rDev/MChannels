package ca.q0r.mchannels.yml.locale;

import ca.q0r.mchannels.yml.YmlManager;
import ca.q0r.mchannels.yml.YmlType;
import ca.q0r.mchat.util.MessageUtil;

public enum LocaleType {
    FORMAT_BROADCAST("format.broadcast"),
    FORMAT_MESSAGE("format.message"),
    FORMAT_JOIN("format.join"),
    FORMAT_LEAVE("format.leave");

    private final String option;

    private LocaleType(String option) {
        this.option = option;
    }

    public String getVal() {
        return MessageUtil.addColour(getRaw());
    }

    public String getRaw() {
        if (YmlManager.getYml(YmlType.LOCALE_YML).getConfig().isSet(option)) {
            return YmlManager.getYml(YmlType.LOCALE_YML).getConfig().getString(option);
        }

        return "Locale Option '" + option + "' not found!";
    }
}
