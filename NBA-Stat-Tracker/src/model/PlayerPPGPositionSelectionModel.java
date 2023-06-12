package model;

public class PlayerPPGPositionSelectionModel extends PrintableData {
    private final String position;
    private final float ppg;

    public PlayerPPGPositionSelectionModel(String position, float ppg) {
        this.position = position;
        this.ppg = ppg;
    }

    public String getPosition() {
        return position;
    }

    public float getPpg() {
        return ppg;
    }

    @Override
    public String getDataString() {
        return position + " " + ppg;
    }
}
