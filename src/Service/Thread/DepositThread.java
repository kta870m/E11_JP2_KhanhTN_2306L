package Service.Thread;

import Entity.Account;
import Entity.Status;
import Entity.Transaction;
import Service.AccountService;

public class DepositThread extends AccountService implements Runnable {
    public DepositThread(Account account, Transaction transaction) {
        super(account, transaction);
    }

    public Account transactionRequest(){
        if(super.getTransaction().getAmount() > 0){
            super.getAccount().setBalance(
                    super.getAccount().getBalance() + super.getTransaction().getAmount()
            );
            super.getTransaction().setStatus(Status.C);
        }else{
            System.out.println("Invalid Amount");
            super.getTransaction().setStatus(Status.R);
        }
        return super.getAccount();
    }


    @Override
    public void run() {
        transactionRequest();
    }
}
