package multipayerpaymentscreens;

import java.util.ArrayList;

import com.example.hotelproject.MainActivity;
import com.example.hotelproject.R;

import payments.PaymentSettings;
import databaseutil.DBHelperLetsGoDutch;
import databaseutil.DBHelperWhoHadTheLobster;
import databaseutil.PersonRowDS;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class SnapshotScreenMultipayer extends ActionBarActivity {

	Bundle extraBundle;
	Button bt_ok;
	DBHelperLetsGoDutch dbHelperLetsGoDutch;
	DBHelperWhoHadTheLobster dbHelperWhoHadTheLobster;
	TableLayout table_layout,table_layout_total_show;
	int order_id;
	String who_is_paying;
	double cash_paid,cash_balance,cash_due;
	double paid_by_card;
	
	ArrayList<String> emails;
	
	int MY_REQUEST_CODE_EMAIL=1;
	private void loadData(){   
		   extraBundle=getIntent().getBundleExtra("databundle");
		   order_id=extraBundle.getInt("order_id");
		   dbHelperLetsGoDutch=new DBHelperLetsGoDutch(this);
		   dbHelperWhoHadTheLobster=new DBHelperWhoHadTheLobster(this);
		   
		   table_layout=(TableLayout)findViewById(R.id.tbl_snapshot_mp);
		   table_layout_total_show=(TableLayout)findViewById(R.id.tbl_mp_total_snapshot);
		   
		   bt_ok=(Button)findViewById(R.id.bt_mp_ok_snapshot_screen);
		   

		   cash_paid=0;
		   cash_balance=0;
		   cash_due=0;
		   paid_by_card=0;
		   emails=new ArrayList<String>();

	   }
	
	
	
	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mp_snapshot_screen);
		
		loadData();
		
		who_is_paying=extraBundle.getString("who_is_paying");
		if(who_is_paying.equals("lets_go_dutch")){
			Toast.makeText(this,"Snapshot Screen : lets go dutch", Toast.LENGTH_LONG).show();
			
			table_layout.removeAllViews();
			ArrayList<PersonRowDS> array_list=dbHelperLetsGoDutch.getAllPersons(order_id);
			TextView tv_signature;
			TextView tv_amount_paid;
			TextView tv_cash_due_back;
			for(int i=0;i<array_list.size();i++){
  		//	  System.out.println(array_list.get(i).SIGNATURE  + array_list.get(i).PERSON_NAME + array_list.get(i).PAID_AMOUNT+ array_list.get(i).CASH_DUE_BACK);
  			  TableRow tr=(TableRow)LayoutInflater.from(SnapshotScreenMultipayer.this).inflate(R.layout.snapshot_screen_row_multipayer, null);
  			  
  			  tv_signature=(TextView)tr.findViewById(R.id.tv_snapshot_signature);
  			  tv_signature.setText(array_list.get(i).PERSON_NAME+" & " + array_list.get(i).SIGNATURE);
  			  
  			  tv_amount_paid=(TextView)tr.findViewById(R.id.tv_snapshot_amount_paid);
  			  tv_amount_paid.setText(PaymentSettings.CURRENCY_SIGN+ array_list.get(i).PAID_AMOUNT);
  			  
  			  tv_cash_due_back=(TextView)tr.findViewById(R.id.tv_snapshot_cash_due_back);
  			  
  			  if(array_list.get(i).CASH_DUE_BACK==0.0)
  				  tv_cash_due_back.setVisibility(TRIM_MEMORY_UI_HIDDEN);
  			  else
  				  tv_cash_due_back.setText(PaymentSettings.CURRENCY_SIGN+array_list.get(i).CASH_DUE_BACK);
  			 
  			  if(array_list.get(i).PAYMENT_TYPE.equals(PaymentSettings.CREDIT_DEBIT_CARD_PAYMENT)){
				  paid_by_card+=array_list.get(i).PAID_AMOUNT;
			  }
			  
			  if(array_list.get(i).PAYMENT_TYPE.equals(PaymentSettings.CASH_PAYMENT))
			  {
				  cash_paid+=array_list.get(i).PAID_AMOUNT;
				  cash_due+=array_list.get(i).CASH_DUE_BACK;
			  }
			  
			  if(array_list.get(i).EMAIL_ID!=null && !array_list.get(i).EMAIL_ID.equals(""))
				  emails.add(array_list.get(i).EMAIL_ID);
			  
			  System.out.println("emails>>>>>"+ array_list.get(i).EMAIL_ID);
  			  table_layout.addView(tr);
  		     }
			setupTotalTable(paid_by_card,cash_paid, cash_due);
			
		}else if(who_is_paying.equals("who_had_the_lobster")){
			Toast.makeText(this,"Snapshot Screen : lets go dutch", Toast.LENGTH_LONG).show();
			
			table_layout.removeAllViews();
			ArrayList<PersonRowDS> array_list=dbHelperWhoHadTheLobster.getAllPersons(order_id);
			TextView tv_signature;
			TextView tv_amount_paid;
			TextView tv_cash_due_back;
			for(int i=0;i<array_list.size();i++){
  			  //System.out.println(array_list.get(i).SIGNATURE  + array_list.get(i).PERSON_NAME + array_list.get(i).PAID_AMOUNT+ array_list.get(i).CASH_DUE_BACK);
  			  TableRow tr=(TableRow)LayoutInflater.from(SnapshotScreenMultipayer.this).inflate(R.layout.snapshot_screen_row_multipayer, null);
  			  
  			  tv_signature=(TextView)tr.findViewById(R.id.tv_snapshot_signature);
  			  tv_signature.setText(array_list.get(i).PERSON_NAME+" & " + array_list.get(i).SIGNATURE);
  			  
  			  tv_amount_paid=(TextView)tr.findViewById(R.id.tv_snapshot_amount_paid);
  			  tv_amount_paid.setText(PaymentSettings.CURRENCY_SIGN+ array_list.get(i).PAID_AMOUNT);
  			  
  			  tv_cash_due_back=(TextView)tr.findViewById(R.id.tv_snapshot_cash_due_back);
  			  
  			  if(array_list.get(i).CASH_DUE_BACK==0.0)
  				  tv_cash_due_back.setVisibility(TRIM_MEMORY_UI_HIDDEN);
  			  else
  				  tv_cash_due_back.setText(PaymentSettings.CURRENCY_SIGN+array_list.get(i).CASH_DUE_BACK);
  			  
			  
			  if(array_list.get(i).PAYMENT_TYPE.equals(PaymentSettings.CREDIT_DEBIT_CARD_PAYMENT)){
				  paid_by_card+=array_list.get(i).PAID_AMOUNT;
			  }
			  
			  if(array_list.get(i).PAYMENT_TYPE.equals(PaymentSettings.CASH_PAYMENT))
			  {
				  cash_paid+=array_list.get(i).PAID_AMOUNT;
				  cash_due+=array_list.get(i).CASH_DUE_BACK;
			  }
			  if(array_list.get(i).EMAIL_ID!=null && !array_list.get(i).EMAIL_ID.equals(""))
				  emails.add(array_list.get(i).EMAIL_ID);
  			  table_layout.addView(tr);
  		     }
			
			setupTotalTable(paid_by_card,cash_paid, cash_due);
		}
		
		bt_ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(emails.size()!=0)
				  loadAndSendEmails();
				else
				{
					if(who_is_paying.equals("lets_go_dutch")){
						dbHelperLetsGoDutch.deleteData(order_id);
					}else if(who_is_paying.equals("who_had_the_lobster")){
						dbHelperWhoHadTheLobster.deleteData(order_id);
					}
					Intent myIntent = new Intent(SnapshotScreenMultipayer.this,MainActivity.class);
					myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity( myIntent);
					
				}
			}
		});
	}

	@SuppressLint("InflateParams")
	public void setupTotalTable(double paid_by_card,double cash_paid,double cash_due){
		 TableRow tr=(TableRow)LayoutInflater.from(SnapshotScreenMultipayer.this).inflate(R.layout.snapshot_screen_row_multipayer, null);
			TextView tv_signature;
			TextView tv_amount_paid;
			TextView tv_cash_due_back;
		  tv_signature=(TextView)tr.findViewById(R.id.tv_snapshot_signature);
		  tv_signature.setText("TOTAL ON CARD: " + PaymentSettings.CURRENCY_SIGN+paid_by_card);
		  
		  tv_amount_paid=(TextView)tr.findViewById(R.id.tv_snapshot_amount_paid);
		  tv_amount_paid.setText("TOTAL IN CASH: "+PaymentSettings.CURRENCY_SIGN+(cash_paid+cash_due));
		  
		  tv_cash_due_back=(TextView)tr.findViewById(R.id.tv_snapshot_cash_due_back);
	      tv_cash_due_back.setText("CASH DUE BACK: "+PaymentSettings.CURRENCY_SIGN+ (cash_due));
		  
		  table_layout_total_show.addView(tr);
	}
	
	
	private void loadAndSendEmails(){
		
		String subject="Payment Reciept > Order ID: "+ order_id;
		String message="This is auto generated email so please do not reply";
		sendEmailTo(emails.toArray(new String[0]),subject,message);
	}
	
	private void sendEmailTo(String emailAddresses[] ,String subject,String message){
		Intent emailIntent=new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailAddresses);
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,subject);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
		startActivityForResult(Intent.createChooser(emailIntent, "Send email..."),MY_REQUEST_CODE_EMAIL);
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    Log.i("SS", "onActivityResult: " + requestCode + ", " + resultCode + ", " 
	        + (data != null ? data.toString() : "empty intent"));
	    
	    if(requestCode == MY_REQUEST_CODE_EMAIL) {
	        
	            Toast.makeText(getApplicationContext(), "Email Sent",
	                Toast.LENGTH_SHORT).show();
				Intent myIntent = new Intent(SnapshotScreenMultipayer.this,MainActivity.class);
				
				if(who_is_paying.equals("lets_go_dutch")){
					dbHelperLetsGoDutch.deleteData(order_id);
				}else if(who_is_paying.equals("who_had_the_lobster")){
					dbHelperWhoHadTheLobster.deleteData(order_id);
				}
				
				myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity( myIntent);
				
	    }
	     // to end your activity when toast is shown
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.snapshot_screen_multipayer, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
