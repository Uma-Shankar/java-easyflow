package easyflow;



public class CardOps {
	public static int balance = 1000;
	public static String cardId = "123456789";
	public static int payAmount = 100;
	
	public static boolean balanceCheck()	{
		if(balance > payAmount)
			return true;
		else
			return false;
	}
	
	public static boolean isCardValid()	{
		if(cardId.equals("123456789"))
			return true;
		else
			return false;
	}
	
	
	public static void transaction() {
		balance-=payAmount;
	}
}
