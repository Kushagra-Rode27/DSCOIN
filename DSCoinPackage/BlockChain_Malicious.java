package DSCoinPackage;

import HelperClasses.CRF;
import HelperClasses.MerkleTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockChain_Malicious {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock[] lastBlocksList;
  public int index;

  public static boolean checkTransactionBlock (TransactionBlock tB) {
    CRF o = new CRF(64);
    boolean f;
    if(tB.previous == null){
      f = tB.dgst.substring(0,4).equals("0000") && (tB.dgst.equals(o.Fn(start_string + "#" + tB.trsummary + "#" + tB.nonce)));
    }
    else{
      f = tB.dgst.substring(0,4).equals("0000") && (tB.dgst.equals(o.Fn(tB.previous.dgst + "#" + tB.trsummary + "#" + tB.nonce)));
    }
    MerkleTree tr = new MerkleTree();
    String k = tr.Build(tB.trarray);
    boolean g = tB.trsummary.equals(k);
    boolean h = true;
    Transaction[] a = tB.trarray;
    for (int i = 0;i<a.length;i++){
      if(!tB.checkTransaction(a[i])){
        h = false;
        break;
      }
    }
    return f&g&h;
  }

  public TransactionBlock FindLongestValidChain () {
    TransactionBlock lastBlock = null;
    int mx = 0;
    for (int i = 0;i<lastBlocksList.length;i++) {
      int l = 0;
      TransactionBlock j = lastBlocksList[i];
      TransactionBlock t = lastBlocksList[i];
      int p = 0;
      while(t!=null){
        if(checkTransactionBlock(t)){
          p+=1;
          if(t.previous == null){
            l = Math.max(l,p);
          }
        }
        else if(!checkTransactionBlock(t)){
//            l = Math.max(l,p);
            p = 0;
            j = t.previous;
        }
        t = t.previous;
      }
      if(l > mx){
        mx = l;
        lastBlock = j;
        index = i;
      }
    }
    return lastBlock;
  }

  public void InsertBlock_Malicious (TransactionBlock newBlock) {
    TransactionBlock lastBlock = FindLongestValidChain();

    CRF o = new CRF(64);
    newBlock.nonce = "1000000001";
    while(!newBlock.nonce.equals("9999999999")){
      String s;
      if(lastBlock == null) {
        s = o.Fn(start_string + "#" + newBlock.trsummary + "#" + newBlock.nonce);
      }
      else{
        s = o.Fn(lastBlock.dgst + "#" + newBlock.trsummary + "#" + newBlock.nonce);
      }

      if(s.substring(0,4).equals("0000")){
        newBlock.dgst = s;
        break;
      }
      newBlock.nonce = "" + (Long.parseLong(newBlock.nonce.replaceAll("[^0-9]", "")) + 1);
    }
    newBlock.previous = lastBlock;

    boolean t = false;
    for (int i = 0;i<lastBlocksList.length;i++){
      if(lastBlocksList[i] == lastBlock){
        t = true;
        break;
      }
    }


    if(t){
      lastBlocksList[index] = newBlock;
    }
    else{
//      TransactionBlock[] ar = new TransactionBlock[lastBlocksList.length+1];
      for (int i = 0;i< lastBlocksList.length;i++){
//        ar[i] = lastBlocksList[i];
        if(lastBlocksList[i] == null){
          lastBlocksList[i] =  newBlock;
          break;
        }
      }
//      ar[ar.length-1] = null;
    }
  }
}
