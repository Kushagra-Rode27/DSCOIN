package DSCoinPackage;

import HelperClasses.CRF;

public class BlockChain_Honest {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock lastBlock;

  public void InsertBlock_Honest (TransactionBlock newBlock) {

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
    lastBlock = newBlock;
  }
}
