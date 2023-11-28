package DSCoinPackage;

import HelperClasses.MerkleTree;
import HelperClasses.CRF;
import HelperClasses.TreeNode;

import java.util.Arrays;
public class TransactionBlock {

  public Transaction[] trarray;
  public TransactionBlock previous;
  public MerkleTree Tree;
  public String trsummary;
  public String nonce;
  public String dgst;

  TransactionBlock(Transaction[] t) {
    trarray = Arrays.copyOf(t,t.length);
    previous = null;
    MerkleTree f = new MerkleTree();
    f.Build(trarray);
    Tree = f;
    trsummary = Tree.rootnode.val;
    dgst = null;
  }

  public boolean checkTransaction (Transaction t) {
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
      TransactionBlock temp = this.previous;
      while (temp != t.coinsrc_block) {
        Transaction[] arr = temp.trarray;
        for (int i = 0; i < arr.length; i++) {
          if (arr[i].coinID.equals(t.coinID)) {
            return false;
          }
//          temp = temp.previous;
        }
        temp = temp.previous;
      }
      return flag;
    }
  }


}
