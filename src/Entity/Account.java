package Entity;

public class Account {
    private String id;
    private Customer customer;
    private double balance;
    private Currency currency;

    public Account() {;
    }

    public Account(String id, Customer customer, double balance, Currency currency) {
        this.id = id;
        this.customer = customer;
        this.balance = balance;
        this.currency = currency;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
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
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", customer=" + customer +
                ", balance=" + balance +
                ", currency=" + currency +
                '}';
    }
}
