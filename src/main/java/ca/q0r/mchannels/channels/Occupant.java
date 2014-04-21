package ca.q0r.mchannels.channels;

import java.util.UUID;

public class Occupant {
    UUID uuid;
    Boolean state;

    public Occupant(UUID uuid, Boolean state) {
        this.uuid = uuid;
        this.state = state;
    }

    public Occupant(UUID uuid) {
        this.uuid = uuid;
        this.state = true;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}