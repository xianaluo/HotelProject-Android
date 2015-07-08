package serverutil;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;




import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;


public class HandleServerDataTable {
	public volatile boolean parsingComplete = true;
	ArrayList<ItemDescDS> all_items;
	int table_no;
	int orderid;
	String urlString;
	public HandleServerDataTable(int tableno) {
		// TODO Auto-generated constructor stub
		table_no=tableno;
		urlString = "http://calculator.gr-digital.org/html/php/get_order.php?table_id="+table_no;
	}
	
	public ArrayList<ItemDescDS> getTableItems(){
		return all_items;
	}
	
	public int getOrderId(){
		return orderid;
	}
	
	@SuppressLint("NewApi")
	public void readParseTableItems(String jsondata){
		all_items=new ArrayList<ItemDescDS>();
		try{
			//JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = new JSONObject(jsondata);
			
			if(!jsonObject.has("Paid"))
			{

					JSONArray json2=jsonObject.getJSONArray("orders");
					orderid=Integer.parseInt(""+jsonObject.get("order_id"));
					ItemDescDS item;

					for(int i=0;i<json2.length();i++){
						JSONObject innerObj=json2.getJSONObject(i);
						item=new ItemDescDS();
						item.units=Integer.parseInt(""+innerObj.get("item_qty"));
						item.item_name=(String) innerObj.get("item_name");
						item.price_per_unit= Double.parseDouble(""+innerObj.get("price_per_unit"));
						item.total_price=item.units*item.price_per_unit;
						item.total_price=(double) Math.round(item.total_price * 100) / 100;
						all_items.add(item);    	  
					}
			
			}
			else
				all_items=null;
	         parsingComplete=false;
	      }catch(Exception e){
	         e.printStackTrace();
	      }
		
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
	            readParseTableItems(data);
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
