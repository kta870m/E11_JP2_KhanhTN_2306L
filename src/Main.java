import Entity.*;
import Global.Date;
import Service.AccountService;
import Service.CustomerService;
import Service.Thread.DepositThread;
import Service.Thread.WithdrawThread;
import Service.TransactionService;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        String rootPath = System.getProperty("user.dir");
        String accountFilePath = rootPath.replace("\\","/") + "/data/Account.txt";
        String transactionFilePath = rootPath.replace("\\","/") + "/data/Transaction.txt";
        String customerFilePath = rootPath.replace("\\","/") + "/data/Customer.txt";

        File myObj = new File("Account.id_transaction_history.txt");
        String fileOutPath = rootPath.replace("\\", "/") + "/data/" + myObj.getName();

        List<Account> accounts = new ArrayList<>();
        List<Transaction> transactions = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();

        AccountService as = new AccountService();
        TransactionService ts = new TransactionService();
        CustomerService cs = new CustomerService();

        accounts = as.readData(accountFilePath);
        transactions = ts.readData(transactionFilePath);
        customers = cs.readData(customerFilePath);

        as.customers = customers;
        as.accounts = accounts;

        ts.accounts = accounts;
        ts.transactions = transactions;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean flag = false;
        int choice;
        String accId;
        String name;
        Double amount;
        Account acc;

        try{
            do{
                System.out.println("1 - Withdraw");
                System.out.println("2 - Deposit");
                System.out.println("3 - Display Balance via Account id");
                System.out.println("4 - Display Transaction By Date");
                System.out.println("5 - Display Account do not have trans past 30day");
                System.out.println("6 - Exit");
                System.out.print("Enter your choice: ");
                choice = Integer.parseInt(br.readLine());
                switch (choice){
                    case 1:
                        Transaction withdrawTran = new Transaction();
                        withdrawTran.setId(transactions.size() + 1);
                        Customer customer1 = new Customer();

                        System.out.print("Enter your Name: ");
                        name = br.readLine();
                        if(as.getAccountByCusName(name)!=null){
                            Iterator<Map.Entry<String, Double>> it = as.getAccountByCusName(name).entrySet().iterator();
                            while (it.hasNext()){
                                Map.Entry<String, Double> entry = it.next();
                                System.out.println("Account " + entry.getKey() + ": " + entry.getValue());
                            }
                        }
                        System.out.print("Enter Account id: ");
                        accId = br.readLine();
                        acc = as.findAccountById(accId);
                        if(acc==null){
                            System.out.println("Account not found !");
                        }
                        System.out.print("Enter Amount: ");
                        amount = Double.parseDouble(br.readLine());

                        withdrawTran.setAccount(Optional.of(acc));
                        withdrawTran.setAmount(amount);
                        withdrawTran.setType(Type.WITHDRAWAL);
                        withdrawTran.setLocalDateTime(LocalDateTime.now());
                        withdrawTran.setStatus(Status.C);

                        WithdrawThread wd = new WithdrawThread(acc, withdrawTran);
                        Thread t1 = new Thread(wd);
                        try{
                            t1.start();
                            t1.join();
                        }catch (InterruptedException e){
                            System.out.println(e.getMessage());
                        }
                        transactions.add(withdrawTran);
                        System.out.println(acc);
                        break;
                    case 2:
                        Transaction depositTran = new Transaction();
                        depositTran.setId(transactions.size() + 1);

                        System.out.print("Enter your Name: ");
                        name = br.readLine();
                        if(as.getAccountByCusName(name)!=null){
                            Iterator<Map.Entry<String, Double>> it = as.getAccountByCusName(name).entrySet().iterator();
                            while (it.hasNext()){
                                Map.Entry<String, Double> entry = it.next();
                                System.out.println("Account " + entry.getKey() + ": " + entry.getValue());
                            }
                        }
                        System.out.print("Enter Account id: ");
                        accId = br.readLine();
                        acc = as.findAccountById(accId);
                        if(acc==null){
                            System.out.println("Account not found !");
                        }
                        System.out.print("Enter Amount: ");
                        amount = Double.parseDouble(br.readLine());

                        depositTran.setAccount(Optional.of(acc));
                        depositTran.setAmount(amount);
                        depositTran.setType(Type.WITHDRAWAL);
                        depositTran.setLocalDateTime(LocalDateTime.now());
                        depositTran.setStatus(Status.C);

                        DepositThread dp = new DepositThread(acc, depositTran);
                        Thread t2 = new Thread(dp);
                        try{
                            t2.start();
                            t2.join();
                        }catch (InterruptedException e){
                            System.out.println(e.getMessage());
                        }
                        transactions.add(depositTran);
                        System.out.println(acc);
                        break;
                    case 3:
                        System.out.print("Enter Your Name: ");
                        name = br.readLine();
                        System.out.print("Enter Account Id: ");
                        accId = br.readLine();
                        Iterator<Map.Entry<String, Double>> it = as.getAccountViaId(name, accId).entrySet().iterator();
                        while (it.hasNext()){
                            Map.Entry<String, Double> entry = it.next();
                            System.out.println("Account " + entry.getKey() + ": " + entry.getValue());
                        }

                        break;
                    case 4:
                        System.out.print("Enter your Name: ");
                        name = br.readLine();
                        if(as.getAccountByCusName(name)!=null){
                            Iterator<Map.Entry<String, Double>> it2 = as.getAccountByCusName(name).entrySet().iterator();
                            while (it2.hasNext()){
                                Map.Entry<String, Double> entry = it2.next();
                                System.out.println("Account " + entry.getKey() + ": " + entry.getValue());
                            }
                        }
                        System.out.print("Enter Account id: ");
                        accId = br.readLine();
                        System.out.print("Enter Start Date: ");
                        LocalDateTime start = Date.formatLocalDateTime(br.readLine());
                        System.out.print("Enter End Date: ");
                        LocalDateTime end = Date.formatLocalDateTime(br.readLine());
                        Map<LocalDateTime, List<Transaction>> tranMap = ts.getTransactionByDate(accId, start, end);
                        Iterator<Map.Entry<LocalDateTime,List<Transaction>>> tranIt = tranMap.entrySet().iterator();
                        while(tranIt.hasNext()){
                            Map.Entry<LocalDateTime, List<Transaction>> entry = tranIt.next();
                            System.out.println(entry.getKey()+ ": " + entry.getValue());
                            ts.saveData(fileOutPath, entry.getValue());
                        }
                        break;

                    case 5:
                        Iterator<Map.Entry<String, Double>> tranIts = ts.getTransactionPast30Days().entrySet().iterator();
                        while (tranIts.hasNext()){
                            Map.Entry<String, Double> entry = tranIts.next();
                            System.out.println(entry.getKey() + ": " + entry.getValue());
                        }
                        break;
                }
            }while (!flag);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

    }
}