public class Expense {
    private double amount;
    private String category;
    private String description;
    private String date;

    public Expense(double amount, String category, String description, String date) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String toCsvLine() {
        return amount + "," + escapeCsv(category) + "," + escapeCsv(description) + "," + escapeCsv(date);
    }

    private String escapeCsv(String value) {
        if (value.contains(",") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }

        return value;
    }

    @Override
    public String toString() {
        return String.format("$%.2f | %s | %s | %s", amount, category, description, date);
    }
}
