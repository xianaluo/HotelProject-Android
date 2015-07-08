package multipayerpaymentscreens;

import payments.PaymentSettings;

import com.example.hotelproject.R;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CashPaymentScreenMultipayer extends ActionBarActivity {
	
	Button bt_proceed;
	EditText et_roundupto,et_leaving;
	Bundle extraBundle;
	TextView tv_amount,tv_dueback;
	double roundupto,totaldue,dueback,leaving;
	
	private void loadData(){   
		   
		   extraBundle=getIntent().getBundleExtra("databundle");
		   totaldue=extraBundle.getDouble("totaldue");
		   
		   bt_proceed=(Button)findViewById(R.id.bt_mp_proceed_cash_payment_screen);
		   tv_amount=(TextView)findViewById(R.id.tv_mp_pleasepay_cash_payment_screen);
		   tv_dueback=(TextView)findViewById(R.id.tv_mp_cash_due_back_cashpaymentscreen);
		   et_roundupto=(EditText)findViewById(R.id.et_mp_roundupto_cash_payment_screen);
		   et_leaving=(EditText)findViewById(R.id.et_mp_leaving_cash_payment_screen);
		   
	       Toast.makeText(this,"Cash payment Screen : "+extraBundle.getDouble("totaldue"), Toast.LENGTH_LONG).show();
	   }
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mp_cash_payment_screen);
		
		loadData();
		
		tv_amount.setText(PaymentSettings.CURRENCY_SIGN+ totaldue);
		roundupto=totaldue-(totaldue%50)+50;
		et_roundupto.setText(PaymentSettings.CURRENCY_SIGN+roundupto);
		dueback=0;
		tv_dueback.setText("Cash Due Back: "+PaymentSettings.CURRENCY_SIGN + dueback);
		et_leaving.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
		        	if(!et_leaving.getText().toString().trim().equals(""))
		        	{ 
		        		leaving=Double.parseDouble(et_leaving.getText().toString());
		        		if(leaving<totaldue){
		        			dueback=0;
		        			tv_dueback.setText("Cash Due Back: "+PaymentSettings.CURRENCY_SIGN + dueback);
		        		}else{
		        			dueback=leaving-totaldue;
		        			tv_dueback.setText("Cash Due Back: "+PaymentSettings.CURRENCY_SIGN + dueback);
		        		}
	            
		        	}
		        	else
		        		{
		        		    leaving=0;
		        			dueback=0;
		        			tv_dueback.setText("Cash Due Back: "+PaymentSettings.CURRENCY_SIGN + dueback);
		        		}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}
		});
		
		bt_proceed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(leaving<totaldue)
					showAlertDialogBox();
				else{
					Bundle bundle = new Bundle();
	            	extraBundle.putDouble("cash_due_back",dueback);
	        		bundle.putBundle("databundle",extraBundle);
	        		final Intent myIntent = new Intent(CashPaymentScreenMultipayer.this,SignatureScreenMultiPayer.class);
	        		myIntent.putExtras(bundle);
					startActivity( myIntent);
				}
				
			}
		});
		
	}
		
	
	private void showAlertDialogBox(){
			final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CashPaymentScreenMultipayer.this);
	     alertDialog.setTitle("Error");
	     alertDialog.setMessage("Input Valid Leaving Amount...!");
	     alertDialog.setIcon(R.drawable.error);
	     alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	         public void onClick(DialogInterface dialog, int which) {
	         }
	     	});
	     alertDialog.show();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cash_payment_screen_multipayer, menu);
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
