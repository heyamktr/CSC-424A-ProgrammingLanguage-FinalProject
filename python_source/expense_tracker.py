from pathlib import Path
import csv


DATA_FILE = Path("data/expenses.csv")


def create_expense(amount, category, description, date):
    return {
        "amount": amount,
        "category": category,
        "description": description,
        "date": date,
    }


def add_expense(expenses):
    print("\nAdd Expense")

    try:
        amount = float(input("Amount: $"))
    except ValueError:
        print("Invalid amount. Please enter a number.")
        return

    category = input("Category: ").strip()
    description = input("Description: ").strip()
    date = input("Date (YYYY-MM-DD): ").strip()

    expense = create_expense(amount, category, description, date)
    expenses.append(expense)
    print("Expense added.")


def view_expenses(expenses):
    print("\nAll Expenses")

    if not expenses:
        print("No expenses recorded.")
        return

    for index, expense in enumerate(expenses, start=1):
        print(
            f"{index}. ${expense['amount']:.2f} | "
            f"{expense['category']} | "
            f"{expense['description']} | "
            f"{expense['date']}"
        )


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

    view_expenses(matches)


def show_total(expenses):
    total = sum(expense["amount"] for expense in expenses)
    print(f"\nTotal spent: ${total:.2f}")


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
                amount = float(row["amount"])
            except ValueError:
                continue

            expenses.append(
                create_expense(
                    amount,
                    row["category"],
                    row["description"],
                    row["date"],
                )
            )

    return expenses


def print_menu():
    print("\nExpense Tracker")
    print("1. Add expense")
    print("2. View all expenses")
    print("3. Search by category")
    print("4. Show total spent")
    print("5. Save expenses")
    print("6. Load expenses")
    print("7. Exit")


def main():
    expenses = load_expenses()
    print(f"Loaded {len(expenses)} expenses.")

    while True:
        print_menu()
        choice = input("Choose an option: ").strip()

        if choice == "1":
            add_expense(expenses)
        elif choice == "2":
            view_expenses(expenses)
        elif choice == "3":
            search_by_category(expenses)
        elif choice == "4":
            show_total(expenses)
        elif choice == "5":
            save_expenses(expenses)
        elif choice == "6":
            expenses = load_expenses()
            print(f"Loaded {len(expenses)} expenses.")
        elif choice == "7":
            save_expenses(expenses)
            print("Goodbye.")
            break
        else:
            print("Invalid choice. Please choose 1-7.")


if __name__ == "__main__":
    main()

