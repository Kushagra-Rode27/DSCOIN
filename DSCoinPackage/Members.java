package DSCoinPackage;

import java.util.*;
import HelperClasses.Pair;

public class Members
 {

  public String UID;
  public List<Pair<String, TransactionBlock>> mycoins;
  public Transaction[] in_process_trans;
  public int ind = 0;
//   public int ind1 = 0;

  public void InsertCoin(Pair<String, TransactionBlock> p , List<Pair<String, TransactionBlock>> mc){
   if(mc.size() == 0 ){
    mc.add(p);
   }
   else{
    int i;
    for (i = 0;i<mc.size();i++){
     if(mc.get(i).get_first().compareTo(p.first) > 0){
      mc.add(i,p);
      break;
     }
    }
    if(i == mc.size()){
     mc.add(p);
    }
   }

  }


  public void initiateCoinsend(String destUID, DSCoin_Honest DSobj) {
   for (int i = 0;i<in_process_trans.length;i++){
    if(in_process_trans[i] == null){
     ind = i;
     break;
    }
   }
   Pair<String, TransactionBlock> p = mycoins.remove(0);
   Members[] mem = DSobj.memberlist;
   Members o = new Members();
   for (int i = 0;i< mem.length;i++){
    if(mem[i].UID.equals(destUID)){
     o = mem[i];
     break;
    }
   }
   Transaction tobj = new Transaction();
   tobj.Source = this;
   tobj.Destination = o;
   tobj.coinID = p.first;


//   TransactionBlock objj = DSobj.bChain.lastBlock;
//   TransactionBlock objt = null;
//   while(objj!=null){
//    Transaction[] tmp = objj.trarray;
//    boolean found = false;
//    for (int i = 0;i< tmp.length;i++){
//     if(tmp[i].coinID.equals(tobj.coinID)){
//      found = true;
//      objt = objj;
//      break;
//     }
//    }
//    if(found){
//     break;
//    }
//    else{
//     objj = objj.previous;
//    }
//
//   }

   tobj.coinsrc_block = p.second;

//   int ind = 0;

   in_process_trans[ind] = tobj;
//   int n = in_process_trans.length;
//   Transaction[] temp = new Transaction[n+1];
//   temp[n] = tobj;
//   in_process_trans = temp;
   DSobj.pendingTransactions.AddTransactions(tobj);
   ind++;
  }



//   public void initiateCoinsend(String destUID, DSCoin_Malicious DSobj) {
//    for (int i = 0;i<in_process_trans.length;i++){
//     if(in_process_trans[i] == null){
//      ind1 = i;
//      break;
//     }
//    }


//    Pair<String, TransactionBlock> p = mycoins.remove(0);
//    Members[] mem = DSobj.memberlist;
//    Members o = new Members();
//    for (int i = 0;i< mem.length;i++){
//     if(mem[i].UID.equals(destUID)){
//      o = mem[i];
//      break;
//     }
//    }
//    Transaction tobj = new Transaction();
//    tobj.Source = this;
//    tobj.Destination = o;
//    tobj.coinID = p.first;


// //   TransactionBlock objj = DSobj.bChain.FindLongestValidChain();
// //   TransactionBlock objt = null;
// //   while(objj!=null){
// //    Transaction[] tmp = objj.trarray;
// //    boolean found = false;
// //    for (int i = 0;i< tmp.length;i++){
// //     if(tmp[i].coinID.equals(tobj.coinID)){
// //      found = true;
// //      objt = objj;
// //      break;
// //     }
// //    }
// //    if(found){
// //     break;
// //    }
// //    else{
// //     objj = objj.previous;
// //    }
// //
// //   }

//    tobj.coinsrc_block = p.second;

// //   int ind = 0;

//    in_process_trans[ind1] = tobj;
// //   int n = in_process_trans.length;
// //   Transaction[] temp = new Transaction[n+1];
// //   temp[n] = tobj;
// //   in_process_trans = temp;
//    DSobj.pendingTransactions.AddTransactions(tobj);
//    ind1++;
//   }




  public Pair<List<Pair<String, String>>, List<Pair<String, String>>> finalizeCoinsend (Transaction tobj, DSCoin_Honest DSObj) throws MissingTransactionException {
   BlockChain_Honest temp = DSObj.bChain;

   TransactionBlock t = temp.lastBlock;
   boolean found = false;
   TransactionBlock tB = null;
   int ind1 = 0;

   while(t!=null){

     for(int i = 0;i<t.trarray.length;i++){
      if(t.trarray[i] == tobj){
       found = true;
       tB = t;
       ind1 = i;
       break;
      }
     }
     if(found){
      break;
     }
     else{
      t = t.previous;
     }
   }
   if(!found){
    throw new MissingTransactionException();
   }
   else{

    List<Pair<String, String>> p1 =tB.Tree.PathToRoot(ind1);
    List<Pair<String, String>> p2 = new ArrayList<>();
    Pair<String,String> m = new Pair<>(tB.previous.dgst,null);
    p2.add(m);
    Pair<String,String> m1 = new Pair<>(tB.dgst, tB.previous.dgst + "#" + tB.trsummary + "#" + tB.nonce);
    p2.add(m1);
    TransactionBlock k = DSObj.bChain.lastBlock;
    List<Pair<String, String>> p3 = new ArrayList<>();
    while(k!= tB){
     Pair<String,String> s = new Pair<>(k.dgst,k.previous.dgst + "#" + k.trsummary + "#" + k.nonce);
     p3.add(s);
     k = k.previous;
    }
    for(int i = p3.size()-1;i>=0;i--){
     p2.add(p3.get(i));
    }
//    TransactionBlock k = tB;
//    while(k!=null){
//     if(k.previous == null){
//      Pair<String,String> s = new Pair<>(k.dgst,BlockChain_Honest.start_string + "#" + k.trsummary + "#" + k.nonce);
//      p2.add(s);
//     }
//     else{
//      Pair<String,String> s = new Pair<>(k.dgst,k.previous.dgst + "#" + k.trsummary + "#" + k.nonce);
//      p2.add(s);
//     }
//     k = k.previous;
//    }

    for (int i = 0;i<in_process_trans.length;i++){
     if(in_process_trans[i] == tobj){
      in_process_trans[i] = null;
      InsertCoin(new Pair<>(tobj.coinID, DSObj.bChain.lastBlock), tobj.Destination.mycoins);
      break;
     }
    }

    Pair<List<Pair<String, String>>,List<Pair<String, String>>> out = new Pair<>(p1,p2);
    return out;
   }
  }
  public boolean checkTr(Transaction t, TransactionBlock tB) {
   TransactionBlock obj = t.coinsrc_block;
   if(obj==null){
    return true;
   }
   else {
    Transaction[] a = obj.trarray;
    boolean flag = false;
    for (int i = 0; i < a.length; i++) {
     if (a[i].coinID.equals(t.coinID) && (a[i].Destination == t.Source)) {
      flag = true;
      break;
     }
    }
    if(flag == false){
     return flag;
    }
    TransactionBlock temp = tB;
    while (temp != t.coinsrc_block) {
     Transaction[] arr = temp.trarray;
     for (int i = 0; i < arr.length; i++) {
      if (arr[i].coinID.equals(t.coinID)) {
       return false;
      }
     }
     temp = temp.previous;
    }
    return flag;
   }
  }

  public void MineCoin(DSCoin_Honest DSObj) {
   int n = DSObj.bChain.tr_count - 1;
   ArrayList<String> hm1 = new ArrayList<>();
   Transaction[] arr = new Transaction[DSObj.bChain.tr_count];
   int i = 0;

    try {
     while(n>0) {
      Transaction t = DSObj.pendingTransactions.RemoveTransaction();

      if (!hm1.contains(t.coinID) && checkTr(t, DSObj.bChain.lastBlock)) {
       hm1.add(t.coinID);
       arr[i] = t;
       i++;
       n--;
      }
     }
    }
    catch (EmptyQueueException e) {
     e.printStackTrace();
    }

   Transaction j = new Transaction();
   String s = DSObj.latestCoinID;
   j.coinID = Integer.toString(Integer.parseInt(s) + 1);
   DSObj.latestCoinID = j.coinID;
   j.Source = null;
   j.Destination = this;
   j.coinsrc_block = null;
   arr[arr.length-1] = j;
   TransactionBlock tB = new TransactionBlock(arr);
   DSObj.bChain.InsertBlock_Honest(tB);
//   for (int l = 0;l< arr.length-1;l++){
//    Pair<String,TransactionBlock> m = new Pair<>(arr[l].coinID,tB);
//    InsertCoin(m,arr[l].Destination.mycoins);
//   }
   Pair<String, TransactionBlock> p1 = new Pair<>(j.coinID,tB);
   InsertCoin(p1,this.mycoins);
//   this.mycoins.add(p1);

  }


  public void MineCoin(DSCoin_Malicious DSObj) {
   int n = DSObj.bChain.tr_count - 1;
   TransactionBlock tbbb = DSObj.bChain.FindLongestValidChain();
//   System.out.println(tbbb.trarray[0].Destination.UID);
   ArrayList<String> hm1 = new ArrayList<>();
   Transaction[] arr = new Transaction[DSObj.bChain.tr_count];
   int i = 0;
//   while(n>0){
    try {
     while(n>0) {

      Transaction t = DSObj.pendingTransactions.RemoveTransaction();

      if (!hm1.contains(t.coinID) && checkTr(t, tbbb)) {
       hm1.add(t.coinID);
       arr[i] = t;
       i++;
       n--;
      }
     }
    }
    catch (EmptyQueueException e) {
     e.printStackTrace();
    }

   Transaction j = new Transaction();
   String s = DSObj.latestCoinID;
   j.coinID = Integer.toString(Integer.parseInt(s) + 1);
   DSObj.latestCoinID = j.coinID;
   j.Source = null;
   j.Destination = this;
   j.coinsrc_block = null;
   arr[arr.length-1] = j;
   TransactionBlock tB = new TransactionBlock(arr);
   DSObj.bChain.InsertBlock_Malicious(tB);
//   for (int l = 0;l< arr.length-1;l++){
//    Pair<String,TransactionBlock> m = new Pair<>(arr[l].coinID,tB);
//    InsertCoin(m,arr[l].Destination.mycoins);
//   }
   Pair<String, TransactionBlock> p1 = new Pair<>(j.coinID,tB);
   InsertCoin(p1,this.mycoins);

  }  
}
