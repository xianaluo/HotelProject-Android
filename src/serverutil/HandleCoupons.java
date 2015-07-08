package serverutil;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONObject;



public class HandleCoupons {
	public volatile boolean parsingComplete = true;
	ArrayList<ItemDescDS> all_items;
	String coupon_code;
	boolean isvalidcoupon,coupon_absolute;
	double coupon_unit;
	String urlString;
	public HandleCoupons(String coupon_code) {
		// TODO Auto-generated constructor stub
		this.coupon_code=coupon_code;
		isvalidcoupon=false;
		coupon_absolute=false;
		coupon_unit=0;
		urlString = "http://calculator.gr-digital.org/html/php/coupon.php?coupon_id="+coupon_code.trim();
	}
	
	public boolean isCouponValid(){
		return isvalidcoupon;
	}
	public boolean isCouponAbsolute(){
		return coupon_absolute;
	}
	public double getDiscountUnit(){
		return coupon_unit;
	}
	public void readParseCoupon(String jsondata){

		try{
			JSONObject jsonObject = new JSONObject(jsondata);
			
			if(!jsonObject.has("invalid"))
			{

				    if(jsonObject.getInt("absolute")==1)
				    	coupon_absolute=true;  
				    else
				    	coupon_absolute=false;
				    coupon_unit=Double.parseDouble(""+jsonObject.getString("unit")); 
				    isvalidcoupon=true;
			   	  
			}else{
				isvalidcoupon=false;
			}
	         parsingComplete=false;
	      }catch(Exception e){
	         System.out.println(e.getMessage());
	         System.out.println(e);
	         isvalidcoupon=false;
	      }
		
	}
	
	
	
	
	 public void fetchDataFromServer(){
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
	            readParseCoupon(data);
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
			java.util.Scanner scanner = new java.util.Scanner(is);
			java.util.Scanner s = scanner.useDelimiter("\\A");
		      return s.hasNext() ? s.next() : "";
	   }
	
}
