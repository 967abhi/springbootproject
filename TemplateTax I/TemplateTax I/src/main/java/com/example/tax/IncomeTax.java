public class IncomeTax implements Tax {

    @Override
    public void setTaxableAmount(int amount) {
        // ...
    }

    @Override
    public void calculateTaxAmount() {
        // ...
    }

    @Override
    public double getTaxAmount() {
        return taxAmount;
    }

    @Override
    public String getTaxType() {
        return "income";
    }

    @Override
    public boolean isTaxPayed() {
        return isTaxPayed;
    }

    @Override
    public void payTax() {
        System.out.println("Hi, your income tax is paid.");
        isTaxPayed = true;
    }
}