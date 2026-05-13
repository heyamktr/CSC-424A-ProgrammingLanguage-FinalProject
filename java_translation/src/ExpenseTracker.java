import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class ExpenseTracker {
    private static final String DATA_FILE = "data/expenses.csv";

    private ArrayList<Expense> expenses;
    private Scanner scanner;

    public ExpenseTracker() {
        expenses = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void run() {
        loadExpenses();
        System.out.println("Loaded " + expenses.size() + " expenses.");

        while (true) {
            printMenu();
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                addExpense();
            } else if (choice.equals("2")) {
                editExpense();
            } else if (choice.equals("3")) {
                deleteExpense();
            } else if (choice.equals("4")) {
                viewExpenses(expenses);
            } else if (choice.equals("5")) {
                searchByCategory();
            } else if (choice.equals("6")) {
                filterByDateRange();
            } else if (choice.equals("7")) {
                sortExpenses();
            } else if (choice.equals("8")) {
                showTotal();
            } else if (choice.equals("9")) {
                showTotalsByCategory();
            } else if (choice.equals("10")) {
                saveExpenses();
            } else if (choice.equals("11")) {
                loadExpenses();
                System.out.println("Loaded " + expenses.size() + " expenses.");
            } else if (choice.equals("12")) {
                saveExpenses();
                System.out.println("Goodbye. Thanks for using this expense tracker by the handsome Khanh!");
                break;
            } else {
                System.out.println("Invalid choice. Please choose 1-12.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\nExpense Tracker");
        System.out.println("1. Add expense");
        System.out.println("2. Edit expense");
        System.out.println("3. Delete expense");
        System.out.println("4. View all expenses");
        System.out.println("5. Search by category");
        System.out.println("6. Filter by date range");
        System.out.println("7. Sort expenses");
        System.out.println("8. Show total spent");
        System.out.println("9. Show totals by category");
        System.out.println("10. Save expenses");
        System.out.println("11. Load expenses");
        System.out.println("12. Exit");
    }

    private LocalDate parseDate(String dateText) {
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException error) {
            return null;
        }
    }

    private Double promptForAmount(String prompt, boolean allowBlank) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();

            if (allowBlank && value.equals("")) {
                return null;
            }

            try {
                double amount = Double.parseDouble(value);

                if (amount < 0) {
                    System.out.println("Amount cannot be negative.");
                    continue;
                }

                return amount;
            } catch (NumberFormatException error) {
                System.out.println("Invalid amount. Please enter a number.");
            }
        }
    }

    private String promptForDate(String prompt, boolean allowBlank) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();

            if (allowBlank && value.equals("")) {
                return "";
            }

            if (parseDate(value) == null) {
                System.out.println("Invalid date. Please use YYYY-MM-DD.");
                continue;
            }

            return value;
        }
    }

    private String promptForRequiredText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();

            if (!value.equals("")) {
                return value;
            }

            System.out.println("This field cannot be blank.");
        }
    }

    private int promptForExpenseIndex() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
            return -1;
        }

        viewExpenses(expenses);
        System.out.print("Expense number: ");

        int number;
        try {
            number = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException error) {
            System.out.println("Invalid expense number.");
            return -1;
        }

        if (number < 1 || number > expenses.size()) {
            System.out.println("Expense number out of range.");
            return -1;
        }

        return number - 1;
    }

    private void addExpense() {
        System.out.println("\nAdd Expense");

        double amount = promptForAmount("Amount: $", false);
        String category = promptForRequiredText("Category: ");
        String description = promptForRequiredText("Description: ");
        String date = promptForDate("Date (YYYY-MM-DD): ", false);

        Expense expense = new Expense(amount, category, description, date);
        expenses.add(expense);
        System.out.println("Expense added.");
    }

    private void editExpense() {
        System.out.println("\nEdit Expense");
        int index = promptForExpenseIndex();

        if (index == -1) {
            return;
        }

        Expense expense = expenses.get(index);
        System.out.println("Press Enter to keep the current value.");

        Double amount = promptForAmount(
            String.format("Amount ($%.2f): ", expense.getAmount()),
            true
        );

        System.out.print("Category (" + expense.getCategory() + "): ");
        String category = scanner.nextLine().trim();

        System.out.print("Description (" + expense.getDescription() + "): ");
        String description = scanner.nextLine().trim();

        String date = promptForDate("Date (" + expense.getDate() + "): ", true);

        if (amount != null) {
            expense.setAmount(amount);
        }
        if (!category.equals("")) {
            expense.setCategory(category);
        }
        if (!description.equals("")) {
            expense.setDescription(description);
        }
        if (!date.equals("")) {
            expense.setDate(date);
        }

        System.out.println("Expense updated.");
    }

    private void deleteExpense() {
        System.out.println("\nDelete Expense");
        int index = promptForExpenseIndex();

        if (index == -1) {
            return;
        }

        Expense expense = expenses.get(index);
        System.out.println("Delete " + expense + "?");
        System.out.print("Type yes to delete: ");
        String confirm = scanner.nextLine().trim().toLowerCase(Locale.ROOT);

        if (confirm.equals("yes")) {
            expenses.remove(index);
            System.out.println("Expense deleted.");
        } else {
            System.out.println("Delete canceled.");
        }
    }

    private void viewExpenses(List<Expense> expenseList) {
        viewExpenses(expenseList, "All Expenses");
    }

    private void viewExpenses(List<Expense> expenseList, String title) {
        System.out.println("\n" + title);

        if (expenseList.isEmpty()) {
            System.out.println("No expenses recorded.");
            return;
        }

        for (int i = 0; i < expenseList.size(); i++) {
            System.out.println((i + 1) + ". " + expenseList.get(i));
        }
    }

    private void searchByCategory() {
        System.out.println("\nSearch By Category");
        System.out.print("Enter category: ");
        String category = scanner.nextLine().trim().toLowerCase(Locale.ROOT);

        ArrayList<Expense> matches = new ArrayList<>();

        for (Expense expense : expenses) {
            if (expense.getCategory().toLowerCase(Locale.ROOT).equals(category)) {
                matches.add(expense);
            }
        }

        if (matches.isEmpty()) {
            System.out.println("No expenses found in that category.");
            return;
        }

        viewExpenses(matches, "Matching Expenses");
    }

    private void filterByDateRange() {
        System.out.println("\nFilter By Date Range");
        String startText = promptForDate("Start date (YYYY-MM-DD): ", false);
        String endText = promptForDate("End date (YYYY-MM-DD): ", false);
        LocalDate startDate = parseDate(startText);
        LocalDate endDate = parseDate(endText);

        if (startDate.isAfter(endDate)) {
            System.out.println("Start date cannot be after end date.");
            return;
        }

        ArrayList<Expense> matches = new ArrayList<>();

        for (Expense expense : expenses) {
            LocalDate expenseDate = parseDate(expense.getDate());

            if (expenseDate == null) {
                continue;
            }

            if (!expenseDate.isBefore(startDate) && !expenseDate.isAfter(endDate)) {
                matches.add(expense);
            }
        }

        if (matches.isEmpty()) {
            System.out.println("No expenses found in that date range.");
            return;
        }

        viewExpenses(matches, "Expenses From " + startText + " To " + endText);
    }

    private void sortExpenses() {
        System.out.println("\nSort Expenses");

        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
            return;
        }

        System.out.println("1. Date");
        System.out.println("2. Amount");
        System.out.println("3. Category");
        System.out.println("4. Description");
        System.out.print("Sort by: ");
        String choice = scanner.nextLine().trim();

        System.out.print("Descending? (y/n): ");
        boolean reverse = scanner.nextLine().trim().toLowerCase(Locale.ROOT).equals("y");

        Comparator<Expense> comparator;
        String title;

        if (choice.equals("1")) {
            comparator = Comparator.comparing(expense -> parseDate(expense.getDate()));
            title = "Expenses Sorted By Date";
        } else if (choice.equals("2")) {
            comparator = Comparator.comparingDouble(Expense::getAmount);
            title = "Expenses Sorted By Amount";
        } else if (choice.equals("3")) {
            comparator = Comparator.comparing(
                expense -> expense.getCategory().toLowerCase(Locale.ROOT)
            );
            title = "Expenses Sorted By Category";
        } else if (choice.equals("4")) {
            comparator = Comparator.comparing(
                expense -> expense.getDescription().toLowerCase(Locale.ROOT)
            );
            title = "Expenses Sorted By Description";
        } else {
            System.out.println("Invalid sort option.");



            return;
        }

        ArrayList<Expense> sortedExpenses = new ArrayList<>(expenses);

        if (reverse) {
            comparator = comparator.reversed();
        }

        sortedExpenses.sort(comparator);
        viewExpenses(sortedExpenses, title);
    }

    private void showTotal() {
        double total = 0;

        for (Expense expense : expenses) {
            total += expense.getAmount();
        }

        System.out.printf("%nTotal spent: $%.2f%n", total);
    }

    private void showTotalsByCategory() {
        System.out.println("\nTotals By Category");

        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded. Please add expenses to see totals by category:)");
            return;
        }

        TreeMap<String, Double> totals = new TreeMap<>();

        for (Expense expense : expenses) {
            String category = expense.getCategory();
            double categoryTotal = totals.getOrDefault(category, 0.0);
            totals.put(category, categoryTotal + expense.getAmount());
        }

        for (Map.Entry<String, Double> entry : totals.entrySet()) {
            System.out.printf("%s: $%.2f%n", entry.getKey(), entry.getValue());
        }
    }

    private void saveExpenses() {
        File dataFile = new File(DATA_FILE);
        File parentFolder = dataFile.getParentFile();

        if (parentFolder != null) {
            parentFolder.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile))) {
            writer.write("amount,category,description,date");
            writer.newLine();

            for (Expense expense : expenses) {
                writer.write(expense.toCsvLine());
                writer.newLine();
            }

            System.out.println("Saved " + expenses.size() + " expenses to " + DATA_FILE + ".");
        } catch (IOException error) {
            System.out.println("Could not save expenses: " + error.getMessage());
        }
    }

    private void loadExpenses() {
        expenses.clear();

        File dataFile = new File(DATA_FILE);

        if (!dataFile.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            reader.readLine();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = splitCsvLine(line);

                if (parts.length != 4) {
                    continue;
                }

                try {
                    double amount = Double.parseDouble(parts[0]);
                    String category = parts[1].trim();
                    String description = parts[2].trim();
                    String date = parts[3].trim();

                    if (amount < 0 || category.equals("") || description.equals("")) {
                        continue;
                    }

                    if (parseDate(date) == null) {
                        continue;
                    }

                    expenses.add(new Expense(amount, category, description, date));
                } catch (NumberFormatException error) {
                    // Skip invalid rows so one bad line does not break the program.
                }
            }
        } catch (IOException error) {
            System.out.println("Could not load expenses: " + error.getMessage());
        }
    }

    private String[] splitCsvLine(String line) {
        ArrayList<String> values = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char currentChar = line.charAt(i);

            if (currentChar == '"') {
                if (insideQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    currentValue.append('"');
                    i++;
                } else {
                    insideQuotes = !insideQuotes;
                }
            } else if (currentChar == ',' && !insideQuotes) {
                values.add(currentValue.toString());
                currentValue.setLength(0);
            } else {
                currentValue.append(currentChar);
            }
        }

        values.add(currentValue.toString());
        return values.toArray(new String[0]);
    }
}
