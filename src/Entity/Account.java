package Entity;
import java.util.Optional;

public class Account {
    private String id;
    private Optional<Customer> customer;
    private double balance;
    private Currency currency;

    public Account() {;
    }

    public Account(String id, Currency currency, double balance, Optional<Customer> customer) {
        this.id = id;
        this.currency = currency;
        this.balance = balance;
        this.customer = customer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Optional<Customer> getCustomer() {
        return customer;
    }

    public void setCustomer(Optional<Customer> customer) {
        this.customer = customer;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        return sb.append(id)
                .append("; ")
                .append(customer.get().getId())
                .append("; ")
                .append(balance)
                .append("; ")
                .append(currency)
                .toString();
    }
}
