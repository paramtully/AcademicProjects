package model;

public class AgentsModel extends PrintableData {
    private final String agency;
    private final String name;

    public AgentsModel(String agency, String name) {
        this.agency = agency;
        this.name = name;
    }

    public String getAgency() {
        return agency;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getDataString() {
        return agency + " " + name;
    }
}
