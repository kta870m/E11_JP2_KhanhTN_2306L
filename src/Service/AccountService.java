package Service;

import Entity.Account;
import Entity.Transaction;

public abstract class AccountService {
    private Account account;
    private Transaction transaction;

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


    public abstract Account transactionRequest();
}
