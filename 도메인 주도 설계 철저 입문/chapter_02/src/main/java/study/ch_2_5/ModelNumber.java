package study.ch_2_5;

public class ModelNumber {
    private String productCode;
    private String branch;
    private String lot;

    public ModelNumber(String productCode, String branch, String lot) {
        if (productCode == null)
            throw new IllegalArgumentException("productCode: " + productCode);
        if (branch == null)
            throw new IllegalArgumentException("branch: " + branch);
        if (lot == null)
            throw new IllegalArgumentException("lot: " + lot);

        this.productCode = productCode;
        this.branch = branch;
        this.lot = lot;
    }

    public String toString() {
        return productCode + "-" + branch + "-" + lot;
    }
}
