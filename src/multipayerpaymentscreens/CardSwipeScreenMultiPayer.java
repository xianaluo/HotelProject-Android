package multipayerpaymentscreens;

import payments.PaymentHandler;
import payments.PaymentSettings;
import com.example.hotelproject.R;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CardSwipeScreenMultiPayer extends ActionBarActivity {
	double totaldue;
	PaymentHandler paymentHandler;
	TextView tv_totalAmount;
	Button bt_proceed;
	Bundle extraBundle;
	
	private void LoadData(){   
		   extraBundle=getIntent().getBundleExtra("databundle");
		   totaldue=extraBundle.getDouble("totaldue");
		   paymentHandler=new PaymentHandler();

		   tv_totalAmount=(TextView)findViewById(R.id.tv_mp_totalamount_swipescreen);
		   bt_proceed=(Button)findViewById(R.id.bt_mp_proceed_card_swipe_screen);
	      //Toast.makeText(this,"Card Swipe Screen : "+extraBundle.getDouble("totaldue"), Toast.LENGTH_LONG).show();
	   }
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mp_card_swipe_screen);
		
		LoadData();
		tv_totalAmount.setText(PaymentSettings.CURRENCY_SIGN+totaldue);
		
		bt_proceed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
            	Bundle bundle = new Bundle();
        		bundle.putBundle("databundle",extraBundle);
        		final Intent myIntent = new Intent(CardSwipeScreenMultiPayer.this,SignatureScreenMultiPayer.class);
        		myIntent.putExtras(bundle);
				startActivity( myIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.card_swipe_screen_multi_payer, menu);
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
