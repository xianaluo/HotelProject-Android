package serverutil;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HandleNoOfTables {
	public volatile boolean parsingComplete = true;
	int no_of_tables;
	String urlString;
	private static java.util.Scanner scanner;
	public HandleNoOfTables() {
		// TODO Auto-generated constructor stub
		urlString = "http://calculator.gr-digital.org/html/php/get_tables.php";
	}
	
	public int getNoOfTables(){
		return no_of_tables;
	}
	public void parseNoOfTables(String data) {
		// TODO Auto-generated method stub
		no_of_tables=Integer.parseInt(data.trim());
		parsingComplete=false;
	}
	
	 public void fetchDataFromServer(){
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
	            parseNoOfTables(data);
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
		      scanner = new java.util.Scanner(is);
			java.util.Scanner s = scanner.useDelimiter("\\A");
		      return s.hasNext() ? s.next() : "";
	   }
	
	
}
