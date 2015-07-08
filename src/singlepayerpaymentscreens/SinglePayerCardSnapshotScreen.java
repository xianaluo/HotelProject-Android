package singlepayerpaymentscreens;

import multipayerpaymentscreens.SnapshotScreenMultipayer;
import payments.PaymentSettings;

import com.example.hotelproject.MainActivity;
import com.example.hotelproject.R;
import com.example.hotelproject.R.id;
import com.example.hotelproject.R.layout;
import com.example.hotelproject.R.menu;

import emailutil.EmailHandler;
import android.support.v7.app.ActionBarActivity;
import android.text.style.TtsSpan.OrdinalBuilder;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SinglePayerCardSnapshotScreen extends ActionBarActivity {

	EditText et_totalamount,et_signature,et_cashpaid,et_changedue,et_balance;
	Button bt_checkout;
	
	Bundle extraBundle;
	Double totaldue,totalpaid,changedueback;
	String sign;
	String emailid;
	int order_id;
	
	int MY_REQUEST_CODE_EMAIL=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sp_card_snapshot_screen);
		
		
		Intent intent = getIntent();
		extraBundle=intent.getBundleExtra("databundle");
		totaldue=extraBundle.getDouble("totaldue");
		changedueback=extraBundle.getDouble("cash_due_back");
		sign=extraBundle.getString("signature");
		emailid=extraBundle.getString("email_id");
		order_id=extraBundle.getInt("order_id");
		
		et_signature= (EditText)findViewById(R.id.et_signature_su_snapshot_screen);
		et_totalamount= (EditText)findViewById(R.id.et_amount_su_snapshot_screen);
		et_cashpaid= (EditText)findViewById(R.id.et_cashpaid_su_snapshot_screen);
		et_changedue= (EditText)findViewById(R.id.et_changedue_su_snapshot_screen);
		et_balance= (EditText)findViewById(R.id.et_balance_su_snapshot_screen);
		
		et_signature.setText(sign);
		et_totalamount.setText(PaymentSettings.CURRENCY_SIGN+ totaldue);
		et_cashpaid.setText("CASH PAID : " +PaymentSettings.CURRENCY_SIGN+ (totaldue+changedueback));
		et_balance.setText("BALANCE : 0");
		et_changedue.setText("CHANGE DUE :"+PaymentSettings.CURRENCY_SIGN+changedueback);
		
		bt_checkout=(Button)findViewById(R.id.bt_ok_su_snapshot_screen);
		bt_checkout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loadAndSendEmails();
			}
		});
		
	}

	
	private void loadAndSendEmails(){
		
		String emailid=extraBundle.getString("email_id");
		String message="Hi,\n This is auto generated bill. "
				+ "\n Your Order_id: "+ order_id
				+ "\n Total amount Paid: "+ totaldue
				+ "\n\n\n\nso do not reply for this email";
		String subject="Bill for the ORDER_ID="+ order_id;
		if(emailid!=null){
			String[] emails={emailid};
			sendEmailTo(emails, subject, message);
		}else
		{
			Intent myIntent = new Intent(SinglePayerCardSnapshotScreen.this,MainActivity.class);
			myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity( myIntent);
		}
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
				Intent myIntent = new Intent(SinglePayerCardSnapshotScreen.this,MainActivity.class);
				myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity( myIntent);
	    }
	    finish(); // to end your activity when toast is shown
	}
	
	
	
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.single_payer_card_snapshot_screen,
				menu);
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
