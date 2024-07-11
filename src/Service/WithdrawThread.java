package Service;

import Entity.Account;
import Entity.Status;
import Entity.Transaction;

import java.util.List;

public class WithdrawThread extends AccountService implements Runnable{
    public static List<Transaction> transactionList;

    public WithdrawThread(Account acc, Transaction transaction){
        super(acc, transaction);
    }

    @Override
    public Account transactionRequest() {
            if(super.getAccount().getBalance() > super.getTransaction().getAmount()){
                super.getAccount().setBalance(
                        super.getAccount().getBalance() - super.getTransaction().getAmount()
                );
                super.getTransaction().setStatus(Status.C);
            }else{
                System.out.println("Not Enough Balance");
                super.getTransaction().setStatus(Status.R);
            }

        return super.getAccount();
    }


    @Override
    public void run() {
        transactionRequest();
    }
}
