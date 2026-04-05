import java.util.*;
class BankAccount {
    String accountNumber;
    String username;
    double balance;

    BankAccount(String accountNumber, String username, double balance) {
        this.accountNumber = accountNumber;
        this.username      = username;
        this.balance       = balance;
    }
    @Override
    public String toString() {
        return "Account[" + accountNumber + "] " + username + " — Balance: " + (int) balance;
    }
}

public class Main {
    static LinkedList<BankAccount>  accounts           = new LinkedList<>();
    static Stack<String>            transactionHistory = new Stack<>();
    static Queue<String>            billQueue          = new LinkedList<>();
    static Queue<BankAccount>       accountRequests    = new LinkedList<>();
    static Scanner                  sc                 = new Scanner(System.in);
    static int accountCounter = 1001;

    static void addAccount(String username, double balance) {
        String number = "ACC" + accountCounter++;
        accounts.add(new BankAccount(number, username, balance));
        System.out.println("Account added successfully.");
    }

    static void displayAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }
        System.out.println("Accounts List:");
        int i = 1;
        for (BankAccount acc : accounts) {
            System.out.println(i++ + ". " + acc.username + " — Balance: " + (int) acc.balance);
        }
    }

    static BankAccount searchByUsername(String username) {
        for (BankAccount acc : accounts) {
            if (acc.username.equalsIgnoreCase(username)) return acc;
        }
        return null;
    }

    static void deposit(String username, double amount) {
        BankAccount acc = searchByUsername(username);
        if (acc == null) { System.out.println("Account not found."); return; }
        acc.balance += amount;
        String record = "Deposit " + (int) amount + " to " + username;
        transactionHistory.push(record);
        System.out.println("New balance: " + (int) acc.balance);
    }

    static void withdraw(String username, double amount) {
        BankAccount acc = searchByUsername(username);
        if (acc == null) { System.out.println("Account not found."); return; }
        if (acc.balance < amount) { System.out.println("Insufficient funds."); return; }
        acc.balance -= amount;
        String record = "Withdraw " + (int) amount + " from " + username;
        transactionHistory.push(record);
        System.out.println("New balance: " + (int) acc.balance);
    }

    static void addTransaction(String description) {
        transactionHistory.push(description);
        System.out.println("Transaction recorded: " + description);
    }

    static void undoLastTransaction() {
        if (transactionHistory.isEmpty()) { System.out.println("No transactions to undo."); return; }
        String removed = transactionHistory.pop();
        System.out.println("Undo → " + removed + " removed.");
    }

    static void peekLastTransaction() {
        if (transactionHistory.isEmpty()) { System.out.println("No transactions yet."); return; }
        System.out.println("Last transaction: " + transactionHistory.peek());
    }

    static void displayTransactionHistory() {
        if (transactionHistory.isEmpty()) { System.out.println("History is empty."); return; }
        System.out.println("Transaction History (newest first):");
        List<String> list = new ArrayList<>(transactionHistory);
        for (int i = list.size() - 1; i >= 0; i--) {
            System.out.println("  • " + list.get(i));
        }
    }

    static void addBill(String billName) {
        billQueue.offer(billName);
        System.out.println("Added: " + billName);
    }

    static void processNextBill() {
        if (billQueue.isEmpty()) { System.out.println("No bills in queue."); return; }
        String bill = billQueue.poll();
        System.out.println("Processing: " + bill);
        transactionHistory.push("Bill payment: " + bill);
        if (!billQueue.isEmpty()) {
            System.out.println("Remaining: " + String.join(", ", billQueue));
        } else {
            System.out.println("No remaining bills.");
        }
    }

    static void displayBillQueue() {
        if (billQueue.isEmpty()) { System.out.println("Bill queue is empty."); return; }
        System.out.println("Pending Bills:");
        int i = 1;
        for (String bill : billQueue) System.out.println(i++ + ". " + bill);
    }

    static void submitAccountRequest(String username, double initialBalance) {
        BankAccount req = new BankAccount("PENDING", username, initialBalance);
        accountRequests.offer(req);
        System.out.println("Request submitted for: " + username);
    }

    static void processNextRequest() {
        if (accountRequests.isEmpty()) { System.out.println("No pending requests."); return; }
        BankAccount req = accountRequests.poll();
        req.accountNumber = "ACC" + accountCounter++;
        accounts.add(req);
        System.out.println("Approved & added: " + req);
    }

    static void displayPendingRequests() {
        if (accountRequests.isEmpty()) { System.out.println("No pending requests."); return; }
        System.out.println("Pending Account Requests:");
        int i = 1;
        for (BankAccount req : accountRequests) {
            System.out.println(i++ + ". " + req.username + " (initial balance: " + (int) req.balance + ")");
        }
    }

    static void task6PhysicalArray() {
        System.out.println("\n=== Task 6: Physical Array Demo ===");
        BankAccount[] bankArray = new BankAccount[3];
        bankArray[0] = new BankAccount("ARR001", "Amir",   500000);
        bankArray[1] = new BankAccount("ARR002", "Dina",   320000);
        bankArray[2] = new BankAccount("ARR003", "Zarina", 750000);

        System.out.println("Stored Accounts (Physical Array):");
        for (int i = 0; i < bankArray.length; i++) {
            System.out.println((i + 1) + ". " + bankArray[i]);
        }
    }

    static void bankMenu() {
        while (true) {
            System.out.println("\n--- Bank Menu ---");
            System.out.println("1. Submit account opening request");
            System.out.println("2. Deposit money");
            System.out.println("3. Withdraw money");
            System.out.println("4. Add bill payment");
            System.out.println("5. View transaction history");
            System.out.println("6. Undo last transaction");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> {
                    System.out.print("Username: ");
                    String name = sc.nextLine().trim();
                    System.out.print("Initial balance: ");
                    double bal = readDouble();
                    submitAccountRequest(name, bal);
                }
                case 2 -> {
                    System.out.print("Username: ");
                    String name = sc.nextLine().trim();
                    System.out.print("Deposit amount: ");
                    double amt = readDouble();
                    deposit(name, amt);
                }
                case 3 -> {
                    System.out.print("Username: ");
                    String name = sc.nextLine().trim();
                    System.out.print("Withdraw amount: ");
                    double amt = readDouble();
                    withdraw(name, amt);
                }
                case 4 -> {
                    System.out.print("Bill name: ");
                    String bill = sc.nextLine().trim();
                    addBill(bill);
                }
                case 5 -> displayTransactionHistory();
                case 6 -> undoLastTransaction();
                case 0 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    static void atmMenu() {
        while (true) {
            System.out.println("\n--- ATM Menu ---");
            System.out.println("1. Balance enquiry");
            System.out.println("2. Withdraw");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> {
                    System.out.print("Username: ");
                    String name = sc.nextLine().trim();
                    BankAccount acc = searchByUsername(name);
                    if (acc == null) System.out.println("Account not found.");
                    else System.out.println("Balance for " + acc.username + ": " + (int) acc.balance);
                }
                case 2 -> {
                    System.out.print("Username: ");
                    String name = sc.nextLine().trim();
                    System.out.print("Withdraw amount: ");
                    double amt = readDouble();
                    withdraw(name, amt);
                }
                case 0 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    static void adminMenu() {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. View pending account requests");
            System.out.println("2. Process next account request");
            System.out.println("3. View bill payment queue");
            System.out.println("4. Process next bill payment");
            System.out.println("5. View all accounts");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> displayPendingRequests();
                case 2 -> processNextRequest();
                case 3 -> displayBillQueue();
                case 4 -> processNextBill();
                case 5 -> displayAllAccounts();
                case 0 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    static void mainMenu() {
        while (true) {
            System.out.println("\n=============================");
            System.out.println("   AITU Banking System");
            System.out.println("=============================");
            System.out.println("1. Enter Bank");
            System.out.println("2. Enter ATM");
            System.out.println("3. Admin Area");
            System.out.println("4. Exit");
            System.out.print("Choose: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> bankMenu();
                case 2 -> atmMenu();
                case 3 -> adminMenu();
                case 4 -> { System.out.println("Goodbye!"); return; }
                default -> System.out.println("Invalid option, try again.");
            }
        }
    }

    static int readInt() {
        while (true) {
            try {
                int val = Integer.parseInt(sc.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }

    static double readDouble() {
        while (true) {
            try {
                double val = Double.parseDouble(sc.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid amount: ");
            }
        }
    }

    public static void main(String[] args) {

        accounts.add(new BankAccount("ACC1000", "Ali",  150000));
        accounts.add(new BankAccount("ACC1001", "Sara", 220000));
        accountCounter = 1002;

        task6PhysicalArray();

        mainMenu();
    }
}