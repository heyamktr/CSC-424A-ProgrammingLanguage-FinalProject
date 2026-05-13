# Java Translation Notes

This folder contains the Java version of the console-based Expense Tracker.

## Files

- `src/Main.java` - starts the program
- `src/Expense.java` - stores one expense record
- `src/ExpenseTracker.java` - handles the menu, input, file saving/loading, searching, sorting, filtering, and totals

## Compile

From the project root folder:

```powershell
javac .\java_translation\src\*.java
```

## Run

From the project root folder:

```powershell
java -cp .\java_translation\src Main
```

The Java version saves and loads the same CSV file as the Python version:

```text
data/expenses.csv
```
