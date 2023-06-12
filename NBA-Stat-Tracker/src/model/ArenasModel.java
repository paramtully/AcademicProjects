package model;

public class ArenasModel extends PrintableData {
    private final String name;
    private final String city;
    private final int numSeats;
    private final int seatPrice;
    private final int maxRevenue;

    public ArenasModel(String name, String city, int numSeats, int seatPrice, int maxRevenue) {
        this.name = name;
        this.city = city;
        this.numSeats = numSeats;
        this.seatPrice = seatPrice;
        this.maxRevenue = maxRevenue;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public int getNumSeats() {
        return numSeats;
    }

    public int getSeatPrice() {
        return seatPrice;
    }

    public int getMaxRevenue() {
        return maxRevenue;
    }


    @Override
    public String getDataString() {
        return name + " " + city + " " + Integer.toString(numSeats) + " " + Integer.toString(seatPrice) + " " + Integer.toString(maxRevenue);
    }
}
