# CSC 424A Final Project: Expense Tracker Translation

This project starts with a console-based Expense Tracker written in Python.
The same program is translated into Java to compare Python's function-based,
dictionary-based style with Java's class-based, statically typed style.

* Project Structure

- `python_source/expense_tracker.py` - original Python program
- `java_translation/src/Main.java` - Java program start file
- `java_translation/src/Expense.java` - Java expense data class
- `java_translation/src/ExpenseTracker.java` - Java menu and tracker logic
- `data/expenses.csv` - saved expense records created by the program ( used as the database)

* Program Features

- Add, edit, and delete expenses
- View all expenses
- Search expenses by category
- Filter expenses by date range
- Sort expenses by date, amount, category, or description
- Show total spending
- Show totals by category
- Save and load expenses with a CSV file
- Validate amounts and dates entered by the user

* Running the Python Version

From the project root folder, run:

```powershell
python .\python_source\expense_tracker.py
```
* Running the Java Version

Compile the Java files:

```powershell
javac .\java_translation\src\*.java
```

Run the Java program:

```powershell
java -cp .\java_translation\src Main
```
* Menu

Both versions use the same menu:

```text
1. Add expense
2. Edit expense
3. Delete expense
4. View all expenses
5. Search by category
6. Filter by date range
7. Sort expenses
8. Show total spent
9. Show totals by category
10. Save expenses
11. Load expenses
12. Exit
```

* Translation Notes

- Python dictionaries are translated into a Java `Expense` class.
- Python lists are translated into `ArrayList<Expense>`.
- Python functions are translated into methods in `ExpenseTracker`.
- Python `input()` is translated into Java `Scanner`.
- Python CSV file handling is translated into Java `BufferedReader` and `BufferedWriter`.
- Python date validation with `datetime` is translated into Java `LocalDate`.
