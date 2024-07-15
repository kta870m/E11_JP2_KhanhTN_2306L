package Service;

import Entity.Account;
import Entity.Currency;
import Entity.Status;
import Entity.Transaction;
import Entity.Type;
import Global.Date;
import IGeneral.IGeneric;

import javax.swing.text.html.Option;
import java.io.*;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionService implements IGeneric<Transaction> {
    public static List<Transaction> transactions;
    public static List<Account> accounts;

    @Override
    public List<Transaction> readData(String filePath) {
        List<Transaction> transactions = new ArrayList<>();
        try{
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line=br.readLine())!=null){
                if(line.length() > 0){
                    Transaction transaction = new Transaction();
                    Account account = new Account();
                    String[] data = line.split(";\\s*");
                    transaction.setId(Integer.parseInt(data[0]));
                    account.setId(data[1]);
                    transaction.setAccount(Optional.of(account));
                    transaction.setAmount(Double.parseDouble(data[2]));
                    transaction.setType(Type.valueOf(data[3]));
                    transaction.setLocalDateTime(Date.formatLocalDateTime(data[4]));
                    transaction.setStatus(Status.valueOf(data[5]));
                    transactions.add(transaction);
                }
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return transactions;
    }

    @Override
    public void saveData(String filePath, List<Transaction> list) {
        File f = new File(filePath);
        if(f.exists()){
            try{
                FileWriter fw = new FileWriter(filePath, true);
                BufferedWriter bw = new BufferedWriter(fw);
                list.forEach(l->{
                    try{
                        bw.append(l.toString());
                        bw.newLine();
                        bw.flush();
                    }catch (IOException e){
                        System.out.println(e.getMessage());
                    }
                });
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public Map<LocalDateTime, List<Transaction>> getTransactionByDate(String id, LocalDateTime start, LocalDateTime end){
        Set<String> accountSet = accounts.stream()
                .map(Account::getId)
                .filter(aId -> aId.equals(id))
                .collect(Collectors.toSet());

        Map<LocalDateTime, List<Transaction>> localDateTimeListMap = transactions.stream()
                .filter(p->accountSet.contains(p.getAccount().get().getId()))
                .filter(p->(p.getLocalDateTime().equals(start) || p.getLocalDateTime().isAfter(start))
                        && (p.getLocalDateTime().equals(end) || p.getLocalDateTime().isBefore(end)))
                .collect(Collectors.groupingBy(
                        Transaction::getLocalDateTime,
                        Collectors.toList()
                ));
            return localDateTimeListMap;
    }

    public Map<String, Double> getTransactionPast30Days(){
        Set<String> accountSet = transactions.stream()
                .filter(t->t.getLocalDateTime().equals(LocalDateTime.now().minus(Period.ofDays(30))))
                .filter(t->t.getType().equals(Type.WITHDRAWAL))
                .map(t->t.getAccount().get().getId())
                .collect(Collectors.toSet());

        Map<String, Double> map = new HashMap<>();
        accounts.stream()
                .filter(a->!accountSet.contains(a.getId()))
                .forEach(a->{
                    if(a.getCurrency().equals(Currency.VND)){
                        a.setBalance(
                                a.getBalance() + (a.getBalance() * 0.2/100)
                        );
                    }else{
                        a.setBalance(a.getBalance());
                    }
                    map.put(a.getId(), a.getBalance());
                });

        return map;
    }
}
