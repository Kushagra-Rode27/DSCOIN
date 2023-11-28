package DSCoinPackage;

import HelperClasses.Pair;

public class Moderator
 {

  public void initializeDSCoin(DSCoin_Honest DSObj, int coinCount)  {
   Members mod = new Members();
   mod.UID = "Moderator";
   int k = 100000;
   for (int i = 0;i<coinCount;i++){
    Transaction t = new Transaction();
    t.Source = mod;
    t.Destination = DSObj.memberlist[i % DSObj.memberlist.length];
    t.coinID = Integer.toString(k);
    t.coinsrc_block = null;
//    Pair<String, TransactionBlock> p = new Pair<>(t.coinID,t.coinsrc_block);
//    DSObj.memberlist[i % DSObj.memberlist.length].mycoins.add(p);
    DSObj.pendingTransactions.AddTransactions(t);
    k++;
   }
   DSObj.latestCoinID = Integer.toString(k-1);

//   while(DSObj.pendingTransactions.size()!=0){
//    Transaction[] arr = new Transaction[DSObj.bChain.tr_count];
//    for (int i = 0;i<DSObj.bChain.tr_count;i++){
//     try {
//       Transaction t = DSObj.pendingTransactions.RemoveTransaction();
//       arr[i] = t;
//     }
//     catch (EmptyQueueException e) {
//      e.printStackTrace();
//     }
//    }
//    TransactionBlock tB = new TransactionBlock(arr);
//    DSObj.bChain.InsertBlock_Honest(tB);
//   }
   for(int a = 0;a<coinCount/DSObj.bChain.tr_count;a++){
    Transaction[] arr = new Transaction[DSObj.bChain.tr_count];
    int i = 0;
    while (i < DSObj.bChain.tr_count){
     try{
      Transaction t = DSObj.pendingTransactions.RemoveTransaction();
      if(true){
       arr[i] = t;
       i+=1;
      }
     }
     catch (EmptyQueueException e){
      e.printStackTrace();
     }
    }
    TransactionBlock tB = new TransactionBlock(arr);
    for(int j = 0;j<tB.trarray.length;j++){
     Pair<String, TransactionBlock> p = new Pair<>(tB.trarray[j].coinID,tB);
     arr[j].Destination.mycoins.add(p);
    }
    DSObj.bChain.InsertBlock_Honest(tB);
   }




  }
    
  public void initializeDSCoin(DSCoin_Malicious DSObj, int coinCount) {
   Members mod = new Members();
   mod.UID = "Moderator";
   int k = 100000;
   for (int i = 0;i<coinCount;i++){
    Transaction t = new Transaction();
    t.Source = mod;
    t.Destination = DSObj.memberlist[i % DSObj.memberlist.length];
    t.coinID = Integer.toString(k);
    t.coinsrc_block = null;
    DSObj.pendingTransactions.AddTransactions(t);
    k++;
   }

   DSObj.latestCoinID = Integer.toString(k-1);
   TransactionBlock tB = null;


   for(int a = 0;a<coinCount/DSObj.bChain.tr_count;a++){
    Transaction[] arr = new Transaction[DSObj.bChain.tr_count];
    int i = 0;
    while (i < DSObj.bChain.tr_count){
     try{
      Transaction t = DSObj.pendingTransactions.RemoveTransaction();
      if(true){
       arr[i] = t;
       i+=1;
      }
     }
     catch (EmptyQueueException e){
      e.printStackTrace();
     }
    }
    tB = new TransactionBlock(arr);
    for(int j = 0;j<tB.trarray.length;j++){
     Pair<String, TransactionBlock> p = new Pair<>(tB.trarray[j].coinID,tB);
     arr[j].Destination.mycoins.add(p);
    }
    DSObj.bChain.InsertBlock_Malicious(tB);
   }


   DSObj.bChain.lastBlocksList[0] = tB;
  }
}
