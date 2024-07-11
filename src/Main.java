import Entity.*;
import Entity.Currency;
import Global.Date;
import Repository.AccountRepo;
import Repository.CustomerRepo;
import Repository.TransactionRepo;
import Service.DepositThread;
import Service.WithdrawThread;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOError;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Account> accounts = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();
        List<Transaction> transactions = new ArrayList<>();

        String rootPath = System.getProperty("user.dir");
        String CustomerPath= rootPath.replace("\\","/") + "/data/Customer.txt";
        String AccountPath = rootPath.replace("\\","/") + "/data/Account.txt";
        String TransactionPath = rootPath.replace("\\","/") + "/data/Transaction.txt";

        File myObj = new File("Account.id_transaction_history.txt");
        String filePathOut = rootPath.replace("\\", "/") + "/data/" + myObj.getName();


        boolean flag = false;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        CustomerRepo cr = new CustomerRepo();
        cr.customers = customers;
        cr.rootPath = rootPath;

        AccountRepo ac = new AccountRepo();
        ac.accounts = accounts;
        ac.customers = customers;
        ac.rootPath = rootPath;

        TransactionRepo tr = new TransactionRepo();
        tr.accounts = accounts;
        tr.transactions = transactions;
        tr.rootPath = rootPath;

        cr.getData(CustomerPath);
        ac.getData(AccountPath);
        tr.getData(TransactionPath);

        int choice;
        String id;
        String name;
        String accId;
        Account acc;
        String option;
        double amount;
        Iterator<Map.Entry<String,Double>> it;

        try{
            do{
                System.out.println("1 - Withdraw");
                System.out.println("2 - Deposit");
                System.out.println("3 - Find Account By Name");
                System.out.println("4 - Find Transaction By Date");
                System.out.println("5 - List All Transaction");
                System.out.println("6 - Display Balance Last 30 Days");
                System.out.println("7 - Exit");
                System.out.print("Enter your choice: ");
                choice = Integer.parseInt(br.readLine());
                switch (choice){
                    case 1:
                        Transaction transaction = new Transaction();
                        System.out.print("Enter your name: ");
                        name = br.readLine();
                        it = ac.getAccountByName(name).entrySet().iterator();
                        while (it.hasNext()){
                            Map.Entry<String, Double> entry = it.next();
                            System.out.println("Account " + entry.getKey() + ": " + entry.getValue());
                        }
                        System.out.print("Enter Account Id: ");
                        accId = br.readLine();
                        transaction.setStatus(Status.C);
                        
                        if(tr.getAccountById(accId) == null){
                            System.out.println("Cannot find an Account");
                            transaction.setStatus(Status.P);
                        }
                        acc = tr.getAccountById(accId);

                        System.out.print("Enter Amount: ");
                        amount = Double.parseDouble(br.readLine());
                        transaction.setAmount(amount);

                        WithdrawThread wd = new WithdrawThread(acc, transaction);

                        Thread t1 = new Thread(wd);
                        try{
                            t1.start();
                            t1.join();
                        }catch (IOError e){
                            System.out.println(e.getMessage());
                        }

                        transaction.setId(transactions.size() + 1);
                        transaction.setAccount(acc);
                        transaction.setType(Type.WITHDRAWAL);
                        transaction.setDateTime(LocalDateTime.now());
                        transactions.add(transaction);

                        System.out.println("Account" + acc.getId() + ": " + acc.getBalance());
                        break;
                    case 2:
                        Transaction transaction2 = new Transaction();
                        System.out.print("Enter your name: ");
                        name = br.readLine();
                        it = ac.getAccountByName(name).entrySet().iterator();
                        while (it.hasNext()){
                            Map.Entry<String, Double> entry = it.next();
                            System.out.println("Account " + entry.getKey() + ": " + entry.getValue());
                        }

                        System.out.print("Enter Account Id: ");
                        accId = br.readLine();

                        if(tr.getAccountById(accId) == null){
                            System.out.println("Cannot find an Account");
                            transaction2.setStatus(Status.P);
                        }
                        acc = tr.getAccountById(accId);

                        System.out.print("Enter Amount: ");
                        amount = Double.parseDouble(br.readLine());
                        transaction2.setAmount(amount);

                        DepositThread dt = new DepositThread(acc, transaction2);

                        Thread t2 = new Thread(dt);
                        try{
                            t2.start();
                            t2.join();
                        }catch (IOError e){
                            System.out.println(e.getMessage());
                        }

                        transaction2.setId(transactions.size() + 1);
                        transaction2.setAccount(acc);
                        transaction2.setType(Type.DEPOSIT);
                        transaction2.setDateTime(LocalDateTime.now());
                        transactions.add(transaction2);

                        System.out.println("Account " + acc.getId() + ": " + acc.getBalance());
                        break;
                    case 3:
                        System.out.print("Enter Account Name: ");
                        name = br.readLine();
                        it = ac.getAccountByName(name).entrySet().iterator();
                        while (it.hasNext()){
                            Map.Entry<String, Double> entry = it.next();
                            System.out.println("Account " + entry.getKey() + ": " + entry.getValue());
                        }
                        break;
                    case 4:
                        System.out.print("Enter Customer Name: ");
                        name = br.readLine();
                        it = ac.getAccountByName(name).entrySet().iterator();
                        while(it.hasNext()){
                            Map.Entry<String, Double> entry = it.next();
                            System.out.println("Account " + entry.getKey() + ": " + entry.getValue());
                        }
                        System.out.print("Enter Account Id: ");
                        id = br.readLine();
                        System.out.print("Enter Start Date: ");
                        LocalDateTime start = Date.parseDateTime(br.readLine());
                        System.out.print("Enter End Date: ");
                        LocalDateTime end = Date.parseDateTime(br.readLine());
                        System.out.println(tr.getTransactionByDate(id, start, end));
                        Iterator<Map.Entry<Account, List<Transaction>>> accountIt = tr.getTransactionByDate(id, start, end).entrySet().iterator();
                        while (accountIt.hasNext()){
                            Map.Entry<Account, List<Transaction>> entry = accountIt.next();
                            System.out.println("Account " + entry.getKey().getId() + ": " + entry.getValue());
                        }
                        System.out.println("Do you want to save this transaction(Y or N): ");
                        option = br.readLine();
                        switch (option){
                            case "Y":
                                tr.saveData(filePathOut, tr.getTransactionByDate(id, start, end));
                                break;
                            case "N":
                                break;
                            default:
                                System.out.println("Invalid option");
                                break;
                        }
                        break;
                    case 5:
                        transactions.forEach(System.out::println);
                        break;
                    case 6:
                        Set<Account> accountSet = transactions
                                .stream()
                                .filter(t->t.getDateTime().isAfter(LocalDateTime.now().minus(Period.ofDays(30))))
                                .filter(t->t.getType() == Type.WITHDRAWAL)
                                .map(Transaction::getAccount)
                                .collect(Collectors.toSet());
                        System.out.println(accountSet);
                        break;
                }
            }while (!flag);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}