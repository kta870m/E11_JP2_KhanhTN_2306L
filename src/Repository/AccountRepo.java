package Repository;

import Entity.Account;
import Entity.Currency;
import Entity.Customer;
import IGeneric.FileGeneric;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountRepo implements FileGeneric<Account> {
    public static List<Account> accounts;
    public static List<Customer> customers;
    public static String rootPath;

    public static Customer getCustomerById(int id){
        return customers.stream()
                .filter(c->c.getId() == id)
                .findFirst().orElse(null);
    }

    @Override
    public List<Account> getData(String filePath) {
        filePath = rootPath.replace("\\","/") + "/data/Account.txt";
        try{
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            String line = null;
            while ((line = br.readLine())!=null){
                String[] data = line.split("; ");
                Account a = new Account();
                a.setId(data[0]);
                a.setCustomer(getCustomerById(Integer.parseInt(data[1])));
                a.setBalance(Double.parseDouble(data[2]));
                a.setCurrency(Currency.valueOf(data[3]));
                accounts.add(a);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    @Override
    public List<Account> writeData(String filePath) {
        return List.of();
    }

    public Map<String, Double> getAccountByName(String name){
        Set<Integer> nameSet = customers.stream()
                .filter(c->c.getName().equals(name))
                .map(Customer::getId)
                .collect(Collectors.toSet());

        Map<String, Double> accountBalance = new HashMap<>();
        accounts.stream()
                .filter(a->nameSet.contains(a.getCustomer().getId()))
                .forEach(a->{
                    accountBalance.put(a.getId(),a.getBalance());
                });
        return accountBalance;
    }

}
