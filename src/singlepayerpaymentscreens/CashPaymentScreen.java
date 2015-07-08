package singlepayerpaymentscreens;

import com.example.hotelproject.R;
import com.example.hotelproject.R.id;
import com.example.hotelproject.R.layout;
import com.example.hotelproject.R.menu;

import payments.PaymentSettings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CashPaymentScreen extends ActionBarActivity {

	EditText et_roundupto,et_leaving;
	Button bt_proceed;
	Bundle extraBundle;
	TextView tv_amount,tv_dueback;
	double roundupto,totaldue,dueback;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sp_cash_payment_screen);
		
		extraBundle=getIntent().getBundleExtra("databundle");
		totaldue=Double.parseDouble(extraBundle.getString("totaldue"));
		
		tv_amount=(TextView)findViewById(R.id.tv_pleasepay_cash_payment_screen);
		tv_dueback=(TextView)findViewById(R.id.tv_cash_due_back_cashpaymentscreen);
		
		tv_amount.setText(PaymentSettings.CURRENCY_SIGN+ totaldue);
		et_roundupto=(EditText)findViewById(R.id.et_roundupto_cash_payment_screen);
		et_leaving=(EditText)findViewById(R.id.et_leaving_cash_payment_screen);
		
		roundupto=totaldue-(totaldue%50)+50;
		dueback=roundupto-totaldue;
		et_roundupto.setText(PaymentSettings.CURRENCY_SIGN+roundupto);
		
		tv_dueback.setText("Cash Due Back: "+PaymentSettings.CURRENCY_SIGN + dueback);
		bt_proceed=(Button)findViewById(R.id.bt_proceed_cash_payment_screen);
		bt_proceed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cash_payment_screen, menu);
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
