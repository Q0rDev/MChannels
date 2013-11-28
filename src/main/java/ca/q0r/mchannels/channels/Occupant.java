package ca.q0r.mchannels.channels;

public class Occupant {
    String name;
    Boolean state;

    public Occupant(String name, Boolean state) {
        this.name = name;
        this.state = state;
    }

    public Occupant(String name) {
        this.name = name;
        this.state = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}