package model;

public class PlayerProjectionModel extends PrintableData {
    private final String name;
    private final int jerseyNum;
    private final String position;
    private final float ppg;

    public PlayerProjectionModel(String name, Integer jerseyNum, String position, float ppg) {
        this.name = name;
        this.jerseyNum = jerseyNum;
        this.position = position;
        this.ppg = ppg;
    }

    public String getName() {
        return name;
    }

    public int getJerseyNum() {
        return jerseyNum;
    }

    public String getPosition() {
        return position;
    }

    public float getPpg() {
        return ppg;
    }

    @Override
    public String getDataString() {
        return name + " " + Integer.toString(jerseyNum) + " " + position + " " + ppg;
    }
}
