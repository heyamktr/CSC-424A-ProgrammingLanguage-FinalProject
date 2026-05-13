# CSC 424A Final Project: Expense Tracker Translation

This project starts with a console-based Expense Tracker written in Python.
The goal is to translate the same program into Java while learning how Python
features map to Java classes, collections, file handling, and input handling.

* Project Structure

- `python_source/expense_tracker.py` - original Python program
- `java_translation/` - place your Java translation here
- `data/expenses.csv` - saved expense records created by the program

* Python Features Used

- Console menu input
- Lists of expense records
- Dictionaries for simple record storage
- File save/load using CSV
- Loops, conditionals, and functions
- Input validation for amounts and dates
- Expense editing and deletion
- Category searching and date-range filtering
- Sorting by date, amount, category, or description
- Summary totals by category

* Java Translation Ideas

When translating, consider these Python-to-Java mappings:

- Python dictionary expense -> Java `Expense` class
- Python list -> Java `ArrayList<Expense>`
- Python functions -> Java static methods or instance methods
- Python `input()` -> Java `Scanner`
- Python CSV file handling -> Java `BufferedReader` and `BufferedWriter`

* Running the Python Version

``` Type into powershell
python .\python_source\expense_tracker.py
```

* How To Run

From the project root folder, run the Python starter program with:

```Type into powershell
python .\python_source\expense_tracker.py
```

The program will open a text menu in the terminal. Enter a number from `1` to
`12` to add, edit, delete, view, search, filter, sort, total, save, load, or
exit.

The Java version is in `java_translation/src`. Compile and run it with:

```Type into powershell
javac .\java_translation\src\*.java
java -cp .\java_translation\src Main
```

* Java Files

The Java version uses these files in `java_translation/src`:

- `Expense.java`
- `ExpenseTracker.java`
- `Main.java`
