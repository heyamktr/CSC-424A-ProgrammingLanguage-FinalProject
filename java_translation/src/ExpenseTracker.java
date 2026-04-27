import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
                viewExpenses(expenses);
            } else if (choice.equals("3")) {
                searchByCategory();
            } else if (choice.equals("4")) {
                showTotal();
            } else if (choice.equals("5")) {
                saveExpenses();
            } else if (choice.equals("6")) {
                loadExpenses();
                System.out.println("Loaded " + expenses.size() + " expenses.");
            } else if (choice.equals("7")) {
                saveExpenses();
                System.out.println("Goodbye.");
                break;
            } else {
                System.out.println("Invalid choice. Please choose 1-7.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\nExpense Tracker");
        System.out.println("1. Add expense");
        System.out.println("2. View all expenses");
        System.out.println("3. Search by category");
        System.out.println("4. Show total spent");
        System.out.println("5. Save expenses");
        System.out.println("6. Load expenses");
        System.out.println("7. Exit");
    }

    private void addExpense() {
        System.out.println("\nAdd Expense");
        System.out.print("Amount: $");

        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException error) {
            System.out.println("Invalid amount. Please enter a number.");
            return;
        }

        System.out.print("Category: ");
        String category = scanner.nextLine().trim();

        System.out.print("Description: ");
        String description = scanner.nextLine().trim();

        System.out.print("Date (YYYY-MM-DD): ");
        String date = scanner.nextLine().trim();

        Expense expense = new Expense(amount, category, description, date);
        expenses.add(expense);
        System.out.println("Expense added.");
    }

    private void viewExpenses(List<Expense> expenseList) {
        System.out.println("\nAll Expenses");

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
        String category = scanner.nextLine().trim().toLowerCase();

        ArrayList<Expense> matches = new ArrayList<>();

        for (Expense expense : expenses) {
            if (expense.getCategory().toLowerCase().equals(category)) {
                matches.add(expense);
            }
        }

        if (matches.isEmpty()) {
            System.out.println("No expenses found in that category.");
            return;
        }

        viewExpenses(matches);
    }

    private void showTotal() {
        double total = 0;

        for (Expense expense : expenses) {
            total += expense.getAmount();
        }

        System.out.printf("%nTotal spent: $%.2f%n", total);
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
            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] parts = splitCsvLine(line);

                if (parts.length != 4) {
                    continue;
                }

                try {
                    double amount = Double.parseDouble(parts[0]);
                    expenses.add(new Expense(amount, parts[1], parts[2], parts[3]));
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
