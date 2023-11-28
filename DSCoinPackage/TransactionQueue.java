package DSCoinPackage;

import java.util.ArrayList;

public class TransactionQueue {

  public Transaction firstTransaction;
  public Transaction lastTransaction;
  public int numTransactions;

  public void AddTransactions (Transaction transaction) {

    if(firstTransaction == null){
      firstTransaction = transaction;
      lastTransaction = transaction;
      lastTransaction.next = null;
      firstTransaction.previous = null;

    }
    else{
      lastTransaction.next = transaction;
      transaction.previous = lastTransaction;
      lastTransaction = transaction;
      lastTransaction.next = null;
    }
    numTransactions += 1;

  }

  public Transaction RemoveTransaction () throws EmptyQueueException{
    if(firstTransaction== null){
      throw new EmptyQueueException();
    }
    else if(firstTransaction == lastTransaction){
      Transaction t = firstTransaction;
      firstTransaction = null;
      lastTransaction = null;
      numTransactions -=1;
      return t;
    }
    else{
      Transaction t = firstTransaction;
      firstTransaction = firstTransaction.next;
      firstTransaction.previous = null;
      numTransactions -=1;
      return t;
      }
  }

  public int size() {
    return numTransactions;
  }

}
