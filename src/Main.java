import java.util.Scanner;

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
        return username + " (Acc#" + accountNumber + ") -- Balance: " + (int) balance;
    }
}

class Node<T> {
    T data;
    Node<T> next;

    Node(T data) {
        this.data = data;
        this.next = null;
    }
}

class MyLinkedList<T> {
    Node<T> head;
    int size;

    MyLinkedList() {
        head = null;
        size = 0;
    }

    void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    T removeLast() {
        if (head == null) return null;
        if (head.next == null) {
            T data = head.data;
            head = null;
            size--;
            return data;
        }
        Node<T> current = head;
        while (current.next.next != null) {
            current = current.next;
        }
        T data = current.next.data;
        current.next = null;
        size--;
        return data;
    }

    T peekLast() {
        if (head == null) return null;
        Node<T> current = head;
        while (current.next != null) {
            current = current.next;
        }
        return current.data;
    }

    T removeFirst() {
        if (head == null) return null;
        T data = head.data;
        head = head.next;
        size--;
        return data;
    }

    T peekFirst() {
        if (head == null) return null;
        return head.data;
    }

    boolean isEmpty() {
        return head == null;
    }

    int size() {
        return size;
    }
}

class MyStack<T> {
    MyLinkedList<T> list = new MyLinkedList<>();

    void push(T data) {
        list.add(data); // top = last node
    }

    T pop() {
        return list.removeLast();
    }

    T peek() {
        return list.peekLast();
    }

    boolean isEmpty() {
        return list.isEmpty();
    }
}


class MyQueue<T> {
    MyLinkedList<T> list = new MyLinkedList<>();

    void add(T data) {
        list.add(data); // back = last node
    }

    T poll() {
        return list.removeFirst(); // front = first node
    }

    T peek() {
        return list.peekFirst();
    }

    boolean isEmpty() {
        return list.isEmpty();
    }
}

public class Main {

    // ── Shared data structures (all manual, no import) ─
    static MyLinkedList<BankAccount> accounts        = new MyLinkedList<>();
    static MyStack<String>           transHistory    = new MyStack<>();
    static MyQueue<String>           billQueue       = new MyQueue<>();
    static MyQueue<BankAccount>      accountRequests = new MyQueue<>();

    static Scanner sc = new Scanner(System.in);


    static void addAccount(String accNum, String username, double balance) {
        accounts.add(new BankAccount(accNum, username, balance));
        System.out.println("Account added successfully");
    }

    static void displayAccounts() {
        if (accounts.isEmpty()) { System.out.println("No accounts found."); return; }
        System.out.println("Accounts List:");
        int i = 1;
        Node<BankAccount> current = accounts.head;
        while (current != null) {
            BankAccount a = current.data;
            System.out.println(i++ + ". " + a.username + " -- Balance: " + (int) a.balance);
            current = current.next;
        }
    }

    static BankAccount searchByUsername(String username) {
        Node<BankAccount> current = accounts.head;
        while (current != null) {
            if (current.data.username.equalsIgnoreCase(username)) return current.data;
            current = current.next;
        }
        return null;
    }


    static void deposit(String username, double amount) {
        BankAccount a = searchByUsername(username);
        if (a == null) { System.out.println("Account not found."); return; }
        a.balance += amount;
        System.out.println("New balance: " + (int) a.balance);
        transHistory.push("Deposit " + (int) amount + " to " + username);
    }

    static void withdraw(String username, double amount) {
        BankAccount a = searchByUsername(username);
        if (a == null) { System.out.println("Account not found."); return; }
        if (a.balance < amount) { System.out.println("Insufficient funds."); return; }
        a.balance -= amount;
        System.out.println("New balance: " + (int) a.balance);
        transHistory.push("Withdraw " + (int) amount + " from " + username);
    }


    static void displayLastTransaction() {
        if (transHistory.isEmpty()) { System.out.println("No transactions yet."); return; }
        System.out.println("Last transaction: " + transHistory.peek());
    }

    static void undoLastTransaction() {
        if (transHistory.isEmpty()) { System.out.println("No transactions to undo."); return; }
        System.out.println("Undo → " + transHistory.pop() + " removed");
    }

    static void displayAllTransactions() {
        if (transHistory.isEmpty()) { System.out.println("No transactions yet."); return; }
        int size = transHistory.list.size();
        String[] arr = new String[size];
        Node<String> current = transHistory.list.head;
        int i = 0;
        while (current != null) {
            arr[i++] = current.data;
            current = current.next;
        }
        System.out.println("Transaction History (newest first):");
        for (int j = size - 1; j >= 0; j--) {
            System.out.println((size - j) + ". " + arr[j]);
        }
    }


    static void addBill(String bill) {
        billQueue.add(bill);
        System.out.println("Added: " + bill);
        transHistory.push("Bill payment: " + bill);
    }

    static void processNextBill() {
        if (billQueue.isEmpty()) { System.out.println("No bills in queue."); return; }
        System.out.println("Processing: " + billQueue.poll());
        if (!billQueue.isEmpty()) System.out.println("Remaining: " + billQueue.peek());
        else System.out.println("No remaining bills.");
    }

    static void displayBillQueue() {
        if (billQueue.isEmpty()) { System.out.println("Bill queue is empty."); return; }
        System.out.println("Bill Queue:");
        int i = 1;
        Node<String> current = billQueue.list.head;
        while (current != null) {
            System.out.println(i++ + ". " + current.data);
            current = current.next;
        }
    }

    static void submitAccountRequest(String accNum, String username, double balance) {
        accountRequests.add(new BankAccount(accNum, username, balance));
        System.out.println("Request submitted for: " + username);
    }

    static void processAccountRequest() {
        if (accountRequests.isEmpty()) { System.out.println("No pending requests."); return; }
        BankAccount a = accountRequests.poll();
        accounts.add(a);
        System.out.println("Account approved and created for: " + a.username);
    }

    static void displayPendingRequests() {
        if (accountRequests.isEmpty()) { System.out.println("No pending requests."); return; }
        System.out.println("Pending Account Requests:");
        int i = 1;
        Node<BankAccount> current = accountRequests.list.head;
        while (current != null) {
            System.out.println(i++ + ". " + current.data.username);
            current = current.next;
        }
    }


    static void runTask6() {
        System.out.println("\n-- Task 6: BankAccount Array --");
        BankAccount[] arr = new BankAccount[3];
        arr[0] = new BankAccount("001", "Ali",   150000);
        arr[1] = new BankAccount("002", "Sara",  220000);
        arr[2] = new BankAccount("003", "Damir", 310000);
        for (int i = 0; i < arr.length; i++)
            System.out.println((i + 1) + ". " + arr[i]);
    }

    static void bankMenu() {
        while (true) {
            System.out.println("\n=== BANK MENU ===");
            System.out.println("1 -- Add new account");
            System.out.println("2 -- Display all accounts");
            System.out.println("3 -- Search account by username");
            System.out.println("4 -- Submit account opening request");
            System.out.println("5 -- Deposit money");
            System.out.println("6 -- Withdraw money");
            System.out.println("7 -- View last transaction (peek)");
            System.out.println("8 -- Undo last transaction (pop)");
            System.out.println("9 -- View all transactions");
            System.out.println("0 -- Back");
            System.out.print("Choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Account Number: "); String an = sc.nextLine();
                    System.out.print("Username: ");       String un = sc.nextLine();
                    System.out.print("Initial Balance: ");double bl = Double.parseDouble(sc.nextLine());
                    addAccount(an, un, bl);
                    break;
                case "2": displayAccounts(); break;
                case "3":
                    System.out.print("Enter username: ");
                    BankAccount found = searchByUsername(sc.nextLine());
                    if (found == null) System.out.println("Account not found.");
                    else System.out.println("Found: " + found);
                    break;
                case "4":
                    System.out.print("Account Number: "); String rn = sc.nextLine();
                    System.out.print("Username: ");       String ru = sc.nextLine();
                    System.out.print("Initial Balance: ");double rb = Double.parseDouble(sc.nextLine());
                    submitAccountRequest(rn, ru, rb);
                    break;
                case "5":
                    System.out.print("Enter username: "); String du = sc.nextLine();
                    System.out.print("Deposit: ");        double da = Double.parseDouble(sc.nextLine());
                    deposit(du, da);
                    break;
                case "6":
                    System.out.print("Enter username: "); String wu = sc.nextLine();
                    System.out.print("Amount: ");         double wa = Double.parseDouble(sc.nextLine());
                    withdraw(wu, wa);
                    break;
                case "7": displayLastTransaction(); break;
                case "8": undoLastTransaction();    break;
                case "9": displayAllTransactions(); break;
                case "0": return;
                default:  System.out.println("Invalid option.");
            }
        }
    }

    static void atmMenu() {
        while (true) {
            System.out.println("\n=== ATM MENU ===");
            System.out.println("1 -- Balance enquiry");
            System.out.println("2 -- Withdraw");
            System.out.println("0 -- Back");
            System.out.print("Choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Enter username: ");
                    BankAccount a = searchByUsername(sc.nextLine());
                    if (a == null) System.out.println("Account not found.");
                    else System.out.println("Balance: " + (int) a.balance);
                    break;
                case "2":
                    System.out.print("Enter username: "); String wu = sc.nextLine();
                    System.out.print("Amount: ");         double wa = Double.parseDouble(sc.nextLine());
                    withdraw(wu, wa);
                    break;
                case "0": return;
                default:  System.out.println("Invalid option.");
            }
        }
    }

    static void adminMenu() {
        while (true) {
            System.out.println("\n=== ADMIN MENU ===");
            System.out.println("1 -- View pending account requests");
            System.out.println("2 -- Process next account request");
            System.out.println("3 -- View bill payment queue");
            System.out.println("4 -- Process next bill payment");
            System.out.println("5 -- Add bill to queue");
            System.out.println("0 -- Back");
            System.out.print("Choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1": displayPendingRequests(); break;
                case "2": processAccountRequest();  break;
                case "3": displayBillQueue();        break;
                case "4": processNextBill();         break;
                case "5":
                    System.out.print("Bill name: ");
                    addBill(sc.nextLine());
                    break;
                case "0": return;
                default:  System.out.println("Invalid option.");
            }
        }
    }

    public static void main(String[] args) {
        addAccount("001", "Ali",  150000);
        addAccount("002", "Sara", 220000);
        runTask6();

        while (true) {
            System.out.println("\n╔══════════════════════╗");
            System.out.println("║   BANKING SYSTEM     ║");
            System.out.println("╠══════════════════════╣");
            System.out.println("║ 1 -- Enter Bank      ║");
            System.out.println("║ 2 -- Enter ATM       ║");
            System.out.println("║ 3 -- Admin Area      ║");
            System.out.println("║ 4 -- Exit            ║");
            System.out.println("╚══════════════════════╝");
            System.out.print("Choice: ");

            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": bankMenu();  break;
                case "2": atmMenu();   break;
                case "3": adminMenu(); break;
                case "4":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please enter 1-4.");
            }
        }
    }
}