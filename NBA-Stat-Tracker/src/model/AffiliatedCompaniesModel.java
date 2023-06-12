package model;

public class AffiliatedCompaniesModel extends PrintableData {
    private final String name;
    private final String productType;
    private final int valuation;

    public AffiliatedCompaniesModel(String name, String productType, int valuation) {
        this.name = name;
        this.productType = productType;
        this.valuation = valuation;
    }

    public String getName() {
        return name;
    }

    public String getProductType() {
        return productType;
    }

    public int getValuation() {
        return valuation;
    }

    @Override
    public String getDataString() {
        return name + " " + productType + " " + Integer.toString(valuation);
    }
}
