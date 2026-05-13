from datetime import datetime
from pathlib import Path
import csv


DATA_FILE = Path("data/expenses.csv")
DATE_FORMAT = "%Y-%m-%d"


def create_expense(amount, category, description, date):
    return {
        "amount": amount,
        "category": category,
        "description": description,
        "date": date,
    }


def parse_date(date_text):
    try:
        parsed_date = datetime.strptime(date_text, DATE_FORMAT).date()
    except ValueError:
        return None

    if parsed_date.strftime(DATE_FORMAT) != date_text:
        return None

    return parsed_date


def prompt_for_amount(prompt, allow_blank=False):
    while True:
        value = input(prompt).strip()

        if allow_blank and value == "":
            return None

        try:
            amount = float(value)
        except ValueError:
            print("Invalid amount. Please enter a number.")
            continue

        if amount < 0:
            print("Amount cannot be negative.")
            continue

        return amount


def prompt_for_date(prompt, allow_blank=False):
    while True:
        value = input(prompt).strip()

        if allow_blank and value == "":
            return ""

        if parse_date(value) is None:
            print("Invalid date. Please use YYYY-MM-DD.")
            continue

        return value


def prompt_for_required_text(prompt):
    while True:
        value = input(prompt).strip()

        if value:
            return value

        print("This field cannot be blank.")


def prompt_for_expense_number(expenses):
    if not expenses:
        print("No expenses recorded.")
        return None

    view_expenses(expenses)

    try:
        number = int(input("Expense number: ").strip())
    except ValueError:
        print("Invalid expense number.")
        return None

    if number < 1 or number > len(expenses):
        print("Expense number out of range.")
        return None

    return number - 1


def add_expense(expenses):
    print("\nAdd Expense")

    amount = prompt_for_amount("Amount: $")
    category = prompt_for_required_text("Category: ")
    description = prompt_for_required_text("Description: ")
    date = prompt_for_date("Date (YYYY-MM-DD): ")

    expense = create_expense(amount, category, description, date)
    expenses.append(expense)
    print("Expense added.")


def edit_expense(expenses):
    print("\nEdit Expense")
    index = prompt_for_expense_number(expenses)

    if index is None:
        return

    expense = expenses[index]
    print("Press Enter to keep the current value.")

    amount = prompt_for_amount(
        f"Amount (${expense['amount']:.2f}): ",
        allow_blank=True,
    )
    category = input(f"Category ({expense['category']}): ").strip()
    description = input(f"Description ({expense['description']}): ").strip()
    date = prompt_for_date(
        f"Date ({expense['date']}): ",
        allow_blank=True,
    )

    if amount is not None:
        expense["amount"] = amount
    if category:
        expense["category"] = category
    if description:
        expense["description"] = description
    if date:
        expense["date"] = date

    print("Expense updated.")


def delete_expense(expenses):
    print("\nDelete Expense")
    index = prompt_for_expense_number(expenses)

    if index is None:
        return

    expense = expenses[index]
    print(
        f"Delete ${expense['amount']:.2f} | "
        f"{expense['category']} | "
        f"{expense['description']} | "
        f"{expense['date']}?"
    )
    confirm = input("Type yes to delete: ").strip().lower()

    if confirm == "yes":
        expenses.pop(index)
        print("Expense deleted.")
    else:
        print("Delete canceled.")


def format_expense(index, expense):
    return (
        f"{index}. ${expense['amount']:.2f} | "
        f"{expense['category']} | "
        f"{expense['description']} | "
        f"{expense['date']}"
    )


def view_expenses(expenses, title="All Expenses"):
    print(f"\n{title}")

    if not expenses:
        print("No expenses recorded.")
        return

    for index, expense in enumerate(expenses, start=1):
        print(format_expense(index, expense))


def search_by_category(expenses):
    print("\nSearch By Category")
    category = input("Enter category: ").strip().lower()

    matches = [
        expense
        for expense in expenses
        if expense["category"].lower() == category
    ]

    if not matches:
        print("No expenses found in that category.")
        return

    view_expenses(matches, "Matching Expenses")


def filter_by_date_range(expenses):
    print("\nFilter By Date Range")
    start_text = prompt_for_date("Start date (YYYY-MM-DD): ")
    end_text = prompt_for_date("End date (YYYY-MM-DD): ")
    start_date = parse_date(start_text)
    end_date = parse_date(end_text)

    if start_date > end_date:
        print("Start date cannot be after end date.")
        return

    matches = []

    for expense in expenses:
        expense_date = parse_date(expense["date"])

        if expense_date is None:
            continue

        if start_date <= expense_date <= end_date:
            matches.append(expense)

    if not matches:
        print("No expenses found in that date range.")
        return

    view_expenses(matches, f"Expenses From {start_text} To {end_text}")


def sort_expenses(expenses):
    print("\nSort Expenses")

    if not expenses:
        print("No expenses recorded.")
        return

    print("1. Date")
    print("2. Amount")
    print("3. Category")
    print("4. Description")
    choice = input("Sort by: ").strip()
    reverse = input("Descending? (y/n): ").strip().lower() == "y"

    if choice == "1":
        sorted_expenses = sorted(
            expenses,
            key=lambda expense: parse_date(expense["date"]),
            reverse=reverse,
        )
        title = "Expenses Sorted By Date"
    elif choice == "2":
        sorted_expenses = sorted(
            expenses,
            key=lambda expense: expense["amount"],
            reverse=reverse,
        )
        title = "Expenses Sorted By Amount"
    elif choice == "3":
        sorted_expenses = sorted(
            expenses,
            key=lambda expense: expense["category"].lower(),
            reverse=reverse,
        )
        title = "Expenses Sorted By Category"
    elif choice == "4":
        sorted_expenses = sorted(
            expenses,
            key=lambda expense: expense["description"].lower(),
            reverse=reverse,
        )
        title = "Expenses Sorted By Description"
    else:
        print("Invalid sort option.")
        return

    view_expenses(sorted_expenses, title)


def show_total(expenses):
    total = sum(expense["amount"] for expense in expenses)
    print(f"\nTotal spent: ${total:.2f}")


def show_totals_by_category(expenses):
    print("\nTotals By Category")

    if not expenses:
        print("No expenses recorded.")
        return

    totals = {}

    for expense in expenses:
        category = expense["category"]
        totals[category] = totals.get(category, 0) + expense["amount"]

    for category in sorted(totals):
        print(f"{category}: ${totals[category]:.2f}")


def save_expenses(expenses):
    DATA_FILE.parent.mkdir(exist_ok=True)

    with DATA_FILE.open("w", newline="") as file:
        writer = csv.DictWriter(
            file,
            fieldnames=["amount", "category", "description", "date"],
        )
        writer.writeheader()
        writer.writerows(expenses)

    print(f"Saved {len(expenses)} expenses to {DATA_FILE}.")


def load_expenses():
    expenses = []

    if not DATA_FILE.exists():
        return expenses

    with DATA_FILE.open("r", newline="") as file:
        reader = csv.DictReader(file)

        for row in reader:
            try:
                amount = float(row.get("amount", ""))
            except ValueError:
                continue

            category = row.get("category", "").strip()
            description = row.get("description", "").strip()
            date = row.get("date", "").strip()

            if amount < 0 or not category or not description:
                continue

            if parse_date(date) is None:
                continue

            expenses.append(create_expense(amount, category, description, date))

    return expenses


def print_menu():
    print("\nExpense Tracker")
    print("1. Add expense")
    print("2. Edit expense")
    print("3. Delete expense")
    print("4. View all expenses")
    print("5. Search by category")
    print("6. Filter by date range")
    print("7. Sort expenses")
    print("8. Show total spent")
    print("9. Show totals by category")
    print("10. Save expenses")
    print("11. Load expenses")
    print("12. Exit")


def main():
    expenses = load_expenses()
    print(f"Loaded {len(expenses)} expenses.")

    while True:
        print_menu()
        choice = input("Choose an option: ").strip()

        if choice == "1":
            add_expense(expenses)
        elif choice == "2":
            edit_expense(expenses)
        elif choice == "3":
            delete_expense(expenses)
        elif choice == "4":
            view_expenses(expenses)
        elif choice == "5":
            search_by_category(expenses)
        elif choice == "6":
            filter_by_date_range(expenses)
        elif choice == "7":
            sort_expenses(expenses)
        elif choice == "8":
            show_total(expenses)
        elif choice == "9":
            show_totals_by_category(expenses)
        elif choice == "10":
            save_expenses(expenses)
        elif choice == "11":
            expenses = load_expenses()
            print(f"Loaded {len(expenses)} expenses.")
        elif choice == "12":
            save_expenses(expenses)
            print("Goodbye.")
            break
        else:
            print("Invalid choice. Please choose 1-12.")


if __name__ == "__main__":
    main()
