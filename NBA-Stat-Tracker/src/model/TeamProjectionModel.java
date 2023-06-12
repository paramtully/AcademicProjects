package model;

public class TeamProjectionModel extends PrintableData {
    private final String name;
    private final String city;

    public TeamProjectionModel(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String getDataString() {
        return name + " " + city;
    }
}
