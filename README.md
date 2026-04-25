# CSC 424A Final Project: Expense Tracker Translation

This project starts with a small console-based Expense Tracker written in Python.
The goal is to translate the same program into Java while learning how Python
features map to Java classes, collections, file handling, and input handling.

## Project Structure

- `python_source/expense_tracker.py` - original Python program
- `java_translation/` - place your Java translation here
- `data/expenses.csv` - saved expense records created by the program

## Python Features Used

- Console menu input
- Lists of expense records
- Dictionaries for simple record storage
- File save/load using CSV
- Loops, conditionals, and functions

## Java Translation Ideas

When translating, consider these Python-to-Java mappings:

- Python dictionary expense -> Java `Expense` class
- Python list -> Java `ArrayList<Expense>`
- Python functions -> Java static methods or instance methods
- Python `input()` -> Java `Scanner`
- Python CSV file handling -> Java `BufferedReader` and `BufferedWriter`

## Running the Python Version

```powershell
python .\python_source\expense_tracker.py
```

## Suggested Java Files

You can create these files in `java_translation/src`:

- `Expense.java`
- `ExpenseTracker.java`
- `Main.java`

