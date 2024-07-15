package Entity;

import java.time.LocalDateTime;
import java.util.Optional;

public class Transaction {
    private int id;
    private Optional<Account> account;
    private double amount;
    private Type type;
    private LocalDateTime localDateTime;
    private Status status;

    public Transaction() {;
    }

    public Transaction(int id, Optional<Account> account,double amount ,Type type, LocalDateTime localDateTime, Status status) {
        this.id = id;
        this.account = account;
        this.amount = amount;
        this.type = type;
        this.localDateTime = localDateTime;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Optional<Account> getAccount() {
        return account;
    }

    public void setAccount(Optional<Account> account) {
        this.account = account;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        return sb.append(id)
                .append("; ")
                .append(account.get().getId())
                .append("; ")
                .append(amount)
                .append("; ")
                .append(type)
                .append("; ")
                .append(localDateTime)
                .append("; ")
                .append(status)
                .toString();
    }
}
