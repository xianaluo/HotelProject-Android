package payments;

public class PaymentHandler {

	public PaymentHandler() {
		// TODO Auto-generated constructor stub
	}
	public boolean getPaymentByCash(double amount){
		
		return true;
		
	}
	public boolean getPaymentByCreditDebitCard(double amount){
		return false;
		
	}
	
	public boolean getPaymentByPayPal(double amount){
		return false;
		
	}
	
	public boolean getPaymentByMobilePay(double amount){
		return false;
		
	}
	public boolean getPaymentByGiftCard(double amount){
		return false;
		
	}
	public boolean getPaymentByBlank(double amount){
		return false;
		
	}
	
}
