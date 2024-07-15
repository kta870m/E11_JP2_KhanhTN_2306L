package Service;

import Entity.Account;
import Entity.Currency;
import Entity.Customer;
import Entity.Transaction;
import IGeneral.IGeneric;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class AccountService implements IGeneric<Account> {
    public static List<Account> accounts;
    public static List<Customer> customers;

    private Account account;
    private Transaction transaction;
    public AccountService(){;}

    public AccountService(Account account, Transaction transaction){
        this.account = account;
        this.transaction = transaction;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public List<Account> readData(String filePath) {
        List<Account> accounts = new ArrayList<>();
        File f = new File(filePath);
        if(f.exists()){
            try{
                FileReader fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);
                String line;
                while((line = br.readLine())!=null){
                    if(line.length() > 0){
                        Account account = new Account();
                        Customer customer = new Customer();
                        String[] data = line.split(";\\s*");
                        account.setId(data[0]);
                        customer.setId(Integer.parseInt(data[1]));
                        account.setCustomer(Optional.of(customer));
                        account.setBalance(Double.parseDouble(data[2]));
                        account.setCurrency(Currency.valueOf(data[3]));
                        accounts.add(account);
                    }
                }
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
        }
        return accounts;
    }

    @Override
    public void saveData(String filePath, List<Account> list) {

    }

    public Map<String, Double> getAccountByCusName(String name){
        Set<Integer> customerSet = customers
                .stream().filter(c->c.getName().equals(name))
                .map(Customer::getId)
                .collect(Collectors.toSet());

        Map<String, Double> stringDoubleMap = new HashMap<>();
        accounts.stream()
                .filter(a->customerSet.contains(a.getCustomer().get().getId()))
                .forEach(a->stringDoubleMap.put(a.getId(), a.getBalance()));

        return stringDoubleMap;
    }


    public Account findAccountById(String id){
        return accounts.stream()
                .filter(a->a.getId().equals(id))
                .findFirst().orElse(null);
    }

    public Map<String, Double> getAccountViaId(String name, String id){
        Set<Integer> customerSet = customers
                .stream().filter(c->c.getName().equals(name))
                .map(Customer::getId)
                .collect(Collectors.toSet());

        Map<String, Double> stringDoubleMap = new HashMap<>();
        accounts.stream()
                .filter(a->a.getId().equals(id))
                .filter(a->customerSet.contains(a.getCustomer().get().getId()))
                .forEach(a->stringDoubleMap.put(a.getId(), a.getBalance()));

        return stringDoubleMap;
    }


}
