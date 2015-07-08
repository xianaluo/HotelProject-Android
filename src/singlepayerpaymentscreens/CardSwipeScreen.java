package singlepayerpaymentscreens;

import com.example.hotelproject.R;
import com.example.hotelproject.R.id;
import com.example.hotelproject.R.layout;
import com.example.hotelproject.R.menu;

import payments.PaymentHandler;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CardSwipeScreen extends ActionBarActivity {
	String totaldue;
	double totalamount;
	PaymentHandler paymentHandler;
	TextView tv_totalAmount;
	Button bt_proceed;
	Bundle extraBundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sp_card_swipe_screen);
		
		Intent intent = getIntent();
		extraBundle=intent.getBundleExtra("databundle");
		totaldue = extraBundle.getString("totaldue");
		totalamount=Double.parseDouble(totaldue);
		
		tv_totalAmount=(TextView)findViewById(R.id.tv_totalamount_swipescreen);
		tv_totalAmount.setText(totaldue);
		
		bt_proceed=(Button)findViewById(R.id.bt_proceed_card_swipe_screen);
		bt_proceed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
            	Bundle bundle = new Bundle();
        		bundle.putBundle("databundle",extraBundle);
        		final Intent myIntent = new Intent(CardSwipeScreen.this,SignatureScreen.class);
        		myIntent.putExtras(bundle);
				startActivity( myIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.card_swipe_screen, menu);
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
