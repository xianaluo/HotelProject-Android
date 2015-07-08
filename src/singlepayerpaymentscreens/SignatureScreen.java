package singlepayerpaymentscreens;

import com.example.hotelproject.R;
import com.example.hotelproject.R.drawable;
import com.example.hotelproject.R.id;
import com.example.hotelproject.R.layout;
import com.example.hotelproject.R.menu;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignatureScreen extends ActionBarActivity {
	String totaldue;
	Button bt_checkout;
	EditText et_sign;
	Bundle extraBundle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sp_signature_screen);
		
		Intent intent = getIntent();
		extraBundle=intent.getBundleExtra("databundle");
		totaldue = extraBundle.getString("totaldue");
		//Toast.makeText(SignatureScreen.this, "Total Due :" + totaldue, Toast.LENGTH_SHORT).show();
		
		Toast.makeText(SignatureScreen.this, "Order id "+ extraBundle.getString("order_id") + " email "+ extraBundle.getString("email_id")
					+ "discount : "+ extraBundle.getString("discount")
					+ "Total Due : "+ extraBundle.getString("totaldue")			
							, Toast.LENGTH_SHORT).show();
		
		et_sign=(EditText)findViewById(R.id.et_signature);
		
		bt_checkout=(Button)findViewById(R.id.bt_checkout);
		bt_checkout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(et_sign.getText().toString().trim().equals("")){
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignatureScreen.this);
	                alertDialog.setTitle("Oops..!");
	                alertDialog.setMessage("Please Sign first");
	                alertDialog.setIcon(R.drawable.error);
	                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int which) {
	                    // User pressed YES button. Write Logic Here
	                    }
	                	});

	                alertDialog.show();	
				}else{
					
    				Bundle bundle = new Bundle();
    				bundle.putString("totaldue",totaldue);
    				bundle.putString("signature", et_sign.getText().toString());
    				Intent myIntent = new Intent(SignatureScreen.this,SinglePayerCardSnapshotScreen.class);
    				myIntent.putExtras(bundle);
    				startActivity( myIntent);
					
					
				}

			}
		});
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.signature_screen, menu);
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
