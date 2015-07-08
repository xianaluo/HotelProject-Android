package singlepayerpaymentscreens;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.hotelproject.R;
import com.example.hotelproject.R.drawable;
import com.example.hotelproject.R.id;
import com.example.hotelproject.R.layout;
import com.example.hotelproject.R.menu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RecieptScreen extends ActionBarActivity {
	final Context context = this;
	Button bt_ereciept,bt_noreciept,bt_hardcopy;
	String totaldue,order_id,discount;
	Bundle extraBundle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sp_receipt_screen);
		
		Intent intent = getIntent();
		extraBundle=intent.getBundleExtra("databundle");
		
		//Toast.makeText(RecieptScreen.this, "Order id "+ extraBundle.getString("order_id") + " Dis "+ discount, Toast.LENGTH_SHORT).show();
		bt_ereciept=(Button)findViewById(R.id.bt_e_reciept);
		
		bt_ereciept.setOnClickListener(new View.OnClickListener() {
			 
				            @Override
			            public void onClick(View view) {
				 
				                // create a Dialog component
				                final Dialog dialog =new Dialog(context);
				 
				                //tell the Dialog to use the dialog.xml as it's layout description
				                dialog.setContentView(R.layout.email_dailog);
				                dialog.setTitle("Enter E-Mail Address");
				 
				                TextView emailid = (TextView) dialog.findViewById(R.id.et_emailid);
				 
				                Button dialogButton = (Button) dialog.findViewById(R.id.bt_go_email);
				 
				                dialogButton.setOnClickListener(new View.OnClickListener() {
				                    @Override
				                    public void onClick(View v) {
				                        dialog.dismiss();
				                        EmailValidator em=new EmailValidator();
				                        TextView emailidtemp = (TextView) dialog.findViewById(R.id.et_emailid);
				                        if(em.validate(emailidtemp.getText().toString())){
					        				Bundle bundle = new Bundle();
					        				
					        				extraBundle.putString("email_id", emailidtemp.getText().toString());
					        				
					        				bundle.putBundle("databundle",extraBundle);
					        				
					        				Intent myIntent = new Intent(RecieptScreen.this,PaymentOption.class);
					        				myIntent.putExtras(bundle);
					        				startActivity( myIntent);
				                        }else{
				                        	AlertDialog.Builder alertDialog = new AlertDialog.Builder(RecieptScreen.this);
				                        	 
				                            // Setting Dialog Title
				                            alertDialog.setTitle("Error");
				             
				                            // Setting Dialog Message
				                            alertDialog.setMessage("Invalid Email ID...!");
				                            alertDialog.setIcon(R.drawable.error);
				             
				                            // Setting Positive "Yes" Button
				                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				                                public void onClick(DialogInterface dialog, int which) {
				                                // User pressed YES button. Write Logic Here
				                                }
				                            	});
				             
				             
				                            // Showing Alert Message
				                            alertDialog.show();	
				                        }
				                        
				                        
				                        

				                    }
				                });
				 
				                dialog.show();
				            }
				        });
			bt_noreciept=(Button)findViewById(R.id.bt_no_reciept);
			bt_noreciept.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
    				Bundle bundle = new Bundle();
    				bundle.putBundle("databundle",extraBundle);
    				Intent myIntent = new Intent(RecieptScreen.this,PaymentOption.class);
    				myIntent.putExtras(bundle);
    				startActivity( myIntent);
				}
			});
			
			bt_hardcopy=(Button)findViewById(R.id.bt_hard_copy_reciept);
			bt_hardcopy.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
    				Bundle bundle = new Bundle();
    				extraBundle.putBoolean("hard_copy", true);
    				bundle.putBundle("databundle",extraBundle);
    				Intent myIntent = new Intent(RecieptScreen.this,PaymentOption.class);
    				myIntent.putExtras(bundle);
    				startActivity( myIntent);
				}
			});
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reciept_screen, menu);
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
	
	public class EmailValidator {
		 
		private Pattern pattern;
		private Matcher matcher;
	 
		private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	 
		public EmailValidator() {
			pattern = Pattern.compile(EMAIL_PATTERN);
		}
	 
		/**
		 * Validate hex with regular expression
		 * 
		 * @param hex
		 *            hex for validation
		 * @return true valid hex, false invalid hex
		 */
		public boolean validate(String hex) {
	 
			matcher = pattern.matcher(hex);
			return matcher.matches();
	 
		}
	}
	
	
}
