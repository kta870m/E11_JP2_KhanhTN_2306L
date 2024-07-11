package Repository;

import Entity.Account;
import Entity.Currency;
import Entity.Status;
import Entity.Transaction;
import Entity.Type;
import Global.Date;
import IGeneric.FileGeneric;

import java.io.*;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionRepo implements FileGeneric<Transaction> {
    public static List<Account> accounts;
    public static List<Transaction> transactions;
    public static String rootPath;


    public static Account getAccountById(String id){
        return accounts.stream()
                .filter(a->a.getId().equals(id))
                .findFirst().orElse(null);
    }

    @Override
    public List<Transaction> getData(String filePath) {
        filePath = rootPath.replace("\\","/") + "/data/Transaction.txt";
        try{
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            String line = null;
            while((line = br.readLine())!=null){
                Transaction t = new Transaction();
                String[] data = line.split("; ");
                t.setId(Integer.parseInt(data[0]));
                t.setAccount(getAccountById(data[1]));
                t.setAmount(Double.parseDouble(data[2]));
                t.setType(Type.valueOf(data[3]));
                t.setDateTime(Date.parseDateTime(data[4]));
                t.setStatus(Status.valueOf(data[5]));
                transactions.add(t);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

        return transactions;
    }

    public Map<Account, List<Transaction>> getTransactionByDate(String id, LocalDateTime start, LocalDateTime end){
        Set<Account> accountSet = transactions.stream()
                .filter(t->t.getAccount().getId().equals(id))
                .map(Transaction::getAccount)
                .collect(Collectors.toSet());

        Map<Account, List<Transaction>> accountListMap = transactions.stream()
                .filter(t->accountSet.contains(t.getAccount()))
                .filter(t->(t.getDateTime().isAfter(start) || t.getDateTime().equals(start))
                        && (t.getDateTime().isBefore(end) || t.getDateTime().equals(end)))
                .collect(Collectors.groupingBy(
                        Transaction::getAccount,
                        Collectors.toList()
                ));
        return accountListMap;
    }

    @Override
    public List<Transaction> writeData(String filePath) {
        return List.of();
    }

    public Map<Account, List<Transaction>> saveData(String filePath, Map<Account, List<Transaction>> accountListMap){
        try{
            FileWriter fw = new FileWriter(filePath);
            BufferedWriter bw = new BufferedWriter(fw);
            Iterator<Map.Entry<Account, List<Transaction>>> it = accountListMap.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry<Account, List<Transaction>> entry = it.next();
                String objString = "Account " + entry.getKey().getId() + ": " + entry.getValue();
                bw.append(objString);
                bw.newLine();
                bw.close();
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

        return  accountListMap;
    }


    public Map<Account, Transaction> getTransaction30Days(){
        Set<Account> accountSet = transactions
                .stream()
                .filter(t->t.getDateTime().isAfter(LocalDateTime.now().minus(Period.ofDays(30))))
                .map(Transaction::getAccount)
                .collect(Collectors.toSet());

        Map<Account, Transaction> accountListMap = new HashMap<>();
        transactions.stream()
                .filter(t->t.getDateTime().isAfter(LocalDateTime.now().minus(Period.ofDays(30))))
                .forEach(t->{
                    if(t.getAccount().getCurrency().equals(Currency.VND)){
                        t.getAccount().setBalance(t.getAccount().getBalance() + (t.getAccount().getBalance() * 0.2/100));
                    }else if(t.getAccount().getCurrency().equals(Currency.USD)){
                        t.getAccount().setBalance(t.getAccount().getBalance() + (t.getAccount().getBalance() * 0/100));
                    }
                    accountListMap.put(t.getAccount(), t);
                });

        return accountListMap;

    }


}
