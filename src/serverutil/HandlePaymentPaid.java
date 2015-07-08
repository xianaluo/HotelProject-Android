package serverutil;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HandlePaymentPaid {
	
	public volatile boolean parsingComplete = true;
	int orderid;
	String urlString;
	
	
	public HandlePaymentPaid(int order_id,double amount,double discount,String signature) {
		// TODO Auto-generated constructor stub
		urlString = "http://calculator.gr-digital.org/html/php/update_payment_table.php?order_id="+order_id;
		urlString+="&total_amount="+amount + "&discounted_amount="+discount +"&signature="+signature;
	}
	
	public void readDataFromServer(String data){
		parsingComplete=false;
	}
	
	
	 public void updateDataToServer(){
		 //String serverdata="";
	      Thread thread = new Thread(new Runnable(){
		         @Override
		       public void run() {
	         try {
	        	//establish connection to server for Currency Codes and Currency Names
	            URL url = new URL(urlString);
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setReadTimeout(10000 /* milliseconds */);
	            conn.setConnectTimeout(15000 /* milliseconds */);
	            conn.setRequestMethod("GET");
	            conn.setDoInput(true);
	            // Starts the query
	            conn.connect();
	            InputStream stream = conn.getInputStream();

	            // Convert the incoming Stream from the server to String form and store it to Data variable
	            String data = convertStreamToString(stream);
	            readDataFromServer(data);
	            stream.close();
	            
	         	} catch (Exception e) {
	         		e.printStackTrace();
	         	}
		         }
		      });
		       thread.start(); 
	  }
		// To convert the Input Stream from web to string form
	  static String convertStreamToString(java.io.InputStream is) {
		      @SuppressWarnings("resource")
			java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		      return s.hasNext() ? s.next() : "";
	   }
	
}
