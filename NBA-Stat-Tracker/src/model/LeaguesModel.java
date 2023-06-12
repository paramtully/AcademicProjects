package model;

public class LeaguesModel {
    private final String name;
    private final String hq;

    public LeaguesModel(String name, String hq) {
        this.name = name;
        this.hq = hq;
    }

    public String getName() {
        return name;
    }
    public String getHq() {
        return hq;
    }
}
