package singlepayerpaymentscreens;

import com.example.hotelproject.R;
import com.example.hotelproject.R.drawable;
import com.example.hotelproject.R.id;
import com.example.hotelproject.R.layout;
import com.example.hotelproject.R.menu;

import payments.PaymentHandler;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class PaymentOption extends ActionBarActivity {
	ImageView iv_creditdebit,iv_cash,iv_paypal,iv_mobilepay,iv_giftcard,iv_blankpay;
	String totaldue;
	double totalamount;
	PaymentHandler paymentHandler;
	Bundle extraBundle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sp_payment_option);
		paymentHandler=new PaymentHandler();
		Intent intent = getIntent();
		
		
		extraBundle=intent.getBundleExtra("databundle");
		totaldue = extraBundle.getString("totaldue");
	//	Toast.makeText(PaymentOption.this, "Order id "+ extraBundle.getString("order_id") + " email "+ extraBundle.getString("email_id"), Toast.LENGTH_SHORT).show();
		
		
		
		
		totalamount=Double.parseDouble(totaldue);
		iv_creditdebit=(ImageView)findViewById(R.id.iv_creditdebit);
		iv_paypal=(ImageView)findViewById(R.id.iv_paypal);
		iv_cash=(ImageView)findViewById(R.id.iv_cash);
		iv_mobilepay=(ImageView)findViewById(R.id.iv_mobilepay);
		iv_giftcard=(ImageView)findViewById(R.id.iv_giftcard);
		iv_blankpay=(ImageView)findViewById(R.id.iv_blankpay);
		

		
		final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PaymentOption.this);
        alertDialog.setTitle("Payment Failure");
        alertDialog.setMessage("Payment Failed please go for other option or retry...!");
        alertDialog.setIcon(R.drawable.error);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        	});
		
		
		iv_creditdebit.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {
	        	
	            	Bundle bundle = new Bundle();
	        		bundle.putBundle("databundle",extraBundle);
	        		final Intent myIntent = new Intent(PaymentOption.this,CardSwipeScreen.class);
	        		myIntent.putExtras(bundle);
					startActivity( myIntent);
	        }
	    });
		iv_paypal.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {

	        	if(paymentHandler.getPaymentByPayPal(totalamount)){
	            	Bundle bundle = new Bundle();
	        		bundle.putBundle("databundle",extraBundle);
	        		final Intent myIntent = new Intent(PaymentOption.this,SignatureScreen.class);
	        		myIntent.putExtras(bundle);
					startActivity( myIntent);
	        	}else{

                    alertDialog.show();	
	        	}
	        }
	    });		
		iv_cash.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {
	            	Bundle bundle = new Bundle();
	        		bundle.putBundle("databundle",extraBundle);
	        		final Intent myIntent = new Intent(PaymentOption.this,CashPaymentScreen.class);
	        		myIntent.putExtras(bundle);
					startActivity( myIntent);

	        }
	    });
		iv_mobilepay.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {

	        	if(paymentHandler.getPaymentByMobilePay(totalamount)){
	            	Bundle bundle = new Bundle();
	        		bundle.putBundle("databundle",extraBundle);
	        		final Intent myIntent = new Intent(PaymentOption.this,SignatureScreen.class);
	        		myIntent.putExtras(bundle);
					startActivity( myIntent);
	        	}else{

                    alertDialog.show();	
	        	}
	        }
	    });
		iv_giftcard.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {

	        	if(paymentHandler.getPaymentByGiftCard(totalamount)){
	            	Bundle bundle = new Bundle();
	        		bundle.putBundle("databundle",extraBundle);
	        		final Intent myIntent = new Intent(PaymentOption.this,SignatureScreen.class);
	        		myIntent.putExtras(bundle);
					startActivity( myIntent);
	        	}else{

                    alertDialog.show();	
	        	}
	        }
	    });
		iv_blankpay.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {
	        	if(paymentHandler.getPaymentByBlank(totalamount)){
	            	Bundle bundle = new Bundle();
	        		bundle.putBundle("databundle",extraBundle);
	        		final Intent myIntent = new Intent(PaymentOption.this,SignatureScreen.class);
	        		myIntent.putExtras(bundle);
					startActivity( myIntent);
	        	}else{

                    alertDialog.show();	
	        	}
	        }
	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.payment_option, menu);
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
