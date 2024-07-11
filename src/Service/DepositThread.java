package Service;

import Entity.Account;
import Entity.Status;
import Entity.Transaction;

public class DepositThread extends AccountService implements Runnable{
    public DepositThread(Account acc, Transaction transaction){
        super(acc, transaction);
    }

    @Override
    public Account transactionRequest() {
        if(super.getTransaction().getAmount() > 0){
            super.getAccount().setBalance(
                    super.getAccount().getBalance() + super.getTransaction().getAmount()
            );
            super.getTransaction().setStatus(Status.C);
        }else{
            super.getTransaction().setStatus(Status.R);
        }
        return super.getAccount();
    }


    @Override
    public void run() {
        transactionRequest();
    }
}
