package multipayerpaymentscreens;


import java.util.ArrayList;

import payments.PaymentSettings;
import serverutil.HandlePaymentPaid;
import singlepayerpaymentscreens.SinglePayerCardSnapshotScreen;

import com.example.hotelproject.LetsGoDutchScreen;
import com.example.hotelproject.R;
import com.example.hotelproject.WhoHadTheLobster;

import databaseutil.DBHelperLetsGoDutch;
import databaseutil.DBHelperWhoHadTheLobster;
import databaseutil.PersonRowDS;
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

public class SignatureScreenMultiPayer extends ActionBarActivity {

	Bundle extraBundle;
	EditText et_sign;
	Button bt_checkout;
	DBHelperLetsGoDutch dbHelperLetsGoDutch;
	DBHelperWhoHadTheLobster dbHelperWhoHadTheLobster;
	ArrayList<PersonRowDS> allPersonData;
	int order_id;
	private void loadData(){   
		   extraBundle=getIntent().getBundleExtra("databundle");
		   dbHelperLetsGoDutch=new DBHelperLetsGoDutch(this);
		   dbHelperWhoHadTheLobster=new DBHelperWhoHadTheLobster(this);
		   et_sign=(EditText)findViewById(R.id.et_mp_signature);
		   bt_checkout=(Button)findViewById(R.id.bt_mp_checkout);
		   order_id=extraBundle.getInt("order_id");
	       Toast.makeText(this,"Signature Screen : "+extraBundle.getDouble("totaldue"), Toast.LENGTH_LONG).show();
	   }
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mp_signature_screen);
		
		loadData();
		
		bt_checkout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(et_sign.getText().toString().trim().equals("")){
					showAlertBox("Error","Please Sign first...!");
				}else{
					
	    				
	    				String who_is_paying=extraBundle.getString("who_is_paying");
	    				String payment_type=extraBundle.getString("payment_type");
	    				
						String payer_name=extraBundle.getString("payer_name");
						double paid_amount=extraBundle.getDouble("totaldue");
						String signature=et_sign.getText().toString();
						String email_id="";
						if(extraBundle.containsKey("email_id"))
							email_id=extraBundle.getString("email_id");
	    				if(who_is_paying.equals("lets_go_dutch")){
	    					
	    					if(payment_type.equals(PaymentSettings.CASH_PAYMENT)){
	    						double cash_due_back=extraBundle.getDouble("cash_due_back");
	    						//System.out.println(order_id  + " , " + payer_name  + " , " +  true + " , " +  paid_amount + " , " +  payment_type + " , " +  cash_due_back + " , " +  signature);
	    						dbHelperLetsGoDutch.updateData(order_id, payer_name , true, paid_amount, payment_type, cash_due_back, signature,email_id);
	    						
	    						if(dbHelperLetsGoDutch.isAllPaid(order_id)){
	    								
	    							updateToServerAllPaymentPaidLetsGoDutch();
	    							startSnapshotActivity();
	    						}
	    						else
	    							 startLetsGoDutchActivity();
	    					}else if(payment_type.equals(PaymentSettings.CREDIT_DEBIT_CARD_PAYMENT)){
	    						dbHelperLetsGoDutch.updateData(order_id, payer_name , true, paid_amount, payment_type, 0, signature,email_id);
	    						if(dbHelperLetsGoDutch.isAllPaid(order_id)){
	    							updateToServerAllPaymentPaidLetsGoDutch();
	    							startSnapshotActivity();
	    						}
	    						else
	    							 startLetsGoDutchActivity();
	    					}else if(payment_type.equals(PaymentSettings.PAYPAL_PAYMENT)){
	    						dbHelperLetsGoDutch.updateData(order_id, payer_name , true, paid_amount, payment_type, 0, signature,email_id);
	    						if(dbHelperLetsGoDutch.isAllPaid(order_id)){
	    							updateToServerAllPaymentPaidLetsGoDutch();
	    							startSnapshotActivity();
	    						}
	    						else
	    							 startLetsGoDutchActivity();
	    					}else if(payment_type.equals(PaymentSettings.MOBILE_PAY_PAYMENT)){
	    						dbHelperLetsGoDutch.updateData(order_id, payer_name , true, paid_amount, payment_type, 0, signature,email_id);
	    						if(dbHelperLetsGoDutch.isAllPaid(order_id)){
	    							updateToServerAllPaymentPaidLetsGoDutch();
	    							startSnapshotActivity();
	    						}
	    						else
	    							 startLetsGoDutchActivity();
	    					}else if(payment_type.equals(PaymentSettings.GIFT_CARD_PAYMENT)){
	    						dbHelperLetsGoDutch.updateData(order_id, payer_name , true, paid_amount, payment_type, 0, signature,email_id);
	    						if(dbHelperLetsGoDutch.isAllPaid(order_id)){
	    							updateToServerAllPaymentPaidLetsGoDutch();
	    							startSnapshotActivity();
	    						}
	    						else
	    							 startLetsGoDutchActivity();
	    					}else if(payment_type.equals(PaymentSettings.BLANK_PAYMENT)){
	    						showAlertBox("Error","Blank Payment is Not Valid go back and Choose correct Payment Way");
	    					}
	    						
	    				}else if(who_is_paying.equals("who_had_the_lobster")){
	    					
	    					if(payment_type.equals(PaymentSettings.CASH_PAYMENT)){
	    						double cash_due_back=extraBundle.getDouble("cash_due_back");
	    						//System.out.println(order_id  + " , " + payer_name  + " , " +  true + " , " +  paid_amount + " , " +  payment_type + " , " +  cash_due_back + " , " +  signature);
	    						dbHelperWhoHadTheLobster.updateData(order_id, payer_name , true, paid_amount, payment_type, cash_due_back, signature,email_id);
	    						
	    						if(dbHelperWhoHadTheLobster.isAllPaid(order_id)){
	    							updateToServerAllPaymentPaidWhoHadTheLobster();
	    							startSnapshotActivity();
	    						}
	    						else
	    							startWhoHadTheLobsterActivity();
	    					}else if(payment_type.equals(PaymentSettings.CREDIT_DEBIT_CARD_PAYMENT)){
	    						dbHelperWhoHadTheLobster.updateData(order_id, payer_name , true, paid_amount, payment_type, 0, signature,email_id);
	    						if(dbHelperWhoHadTheLobster.isAllPaid(order_id)){
	    							updateToServerAllPaymentPaidWhoHadTheLobster();
	    							startSnapshotActivity();
	    						}
	    						else
	    							startWhoHadTheLobsterActivity();
	    					}else if(payment_type.equals(PaymentSettings.PAYPAL_PAYMENT)){
	    						dbHelperWhoHadTheLobster.updateData(order_id, payer_name , true, paid_amount, payment_type, 0, signature,email_id);
	    						if(dbHelperWhoHadTheLobster.isAllPaid(order_id)){
	    							updateToServerAllPaymentPaidWhoHadTheLobster();
	    							startSnapshotActivity();
	    						}
	    						else
	    							startWhoHadTheLobsterActivity();
	    					}else if(payment_type.equals(PaymentSettings.MOBILE_PAY_PAYMENT)){
	    						dbHelperWhoHadTheLobster.updateData(order_id, payer_name , true, paid_amount, payment_type, 0, signature,email_id);
	    						if(dbHelperWhoHadTheLobster.isAllPaid(order_id)){
	    							updateToServerAllPaymentPaidWhoHadTheLobster();
	    							startSnapshotActivity();
	    						}
	    						else
	    							startWhoHadTheLobsterActivity();
	    					}else if(payment_type.equals(PaymentSettings.GIFT_CARD_PAYMENT)){
	    						dbHelperWhoHadTheLobster.updateData(order_id, payer_name , true, paid_amount, payment_type, 0, signature,email_id);
	    						if(dbHelperWhoHadTheLobster.isAllPaid(order_id)){
	    							updateToServerAllPaymentPaidWhoHadTheLobster();
	    							startSnapshotActivity();
	    						}
	    						else
	    							startWhoHadTheLobsterActivity();
	    					}else if(payment_type.equals(PaymentSettings.BLANK_PAYMENT)){
	    						showAlertBox("Error","Blank Payment is Not Valid go back and Choose correct Payment Way");
	    					}
	    					
	    					
	    				}else if(who_is_paying.equals("its_on_me")){
	    					if(payment_type.equals(PaymentSettings.CASH_PAYMENT)){
	    						double cash_due_back=extraBundle.getDouble("cash_due_back");
	    						HandlePaymentPaid hpp=new HandlePaymentPaid(order_id,paid_amount, 0, signature);
	    						hpp.updateDataToServer();
	    						while(hpp.parsingComplete);
	    						startSinglePayerCardSnapshotScreen();
	    						
	    					}else if(payment_type.equals(PaymentSettings.CREDIT_DEBIT_CARD_PAYMENT)){
	    						HandlePaymentPaid hpp=new HandlePaymentPaid(order_id,paid_amount, 0, signature);
	    						hpp.updateDataToServer();
	    						while(hpp.parsingComplete);
	    						startSinglePayerCardSnapshotScreen();
	    					}else if(payment_type.equals(PaymentSettings.PAYPAL_PAYMENT)){
	    						HandlePaymentPaid hpp=new HandlePaymentPaid(order_id,paid_amount, 0, signature);
	    						hpp.updateDataToServer();
	    						while(hpp.parsingComplete);
	    						startSinglePayerCardSnapshotScreen();
	    					}else if(payment_type.equals(PaymentSettings.MOBILE_PAY_PAYMENT)){
	    						HandlePaymentPaid hpp=new HandlePaymentPaid(order_id,paid_amount, 0, signature);
	    						hpp.updateDataToServer();
	    						while(hpp.parsingComplete);
	    						startSinglePayerCardSnapshotScreen();
	    					}else if(payment_type.equals(PaymentSettings.GIFT_CARD_PAYMENT)){
	    						HandlePaymentPaid hpp=new HandlePaymentPaid(order_id,paid_amount, 0, signature);
	    						hpp.updateDataToServer();
	    						while(hpp.parsingComplete);
	    						startSinglePayerCardSnapshotScreen();
	    					}else if(payment_type.equals(PaymentSettings.BLANK_PAYMENT)){
	    						showAlertBox("Error","Blank Payment is Not Valid go back and Choose correct Payment Way");
	    					}
	    				}
	
				}

			}
		});
		
	}

	private void updateToServerAllPaymentPaidLetsGoDutch(){
		allPersonData=dbHelperLetsGoDutch.getAllPersons(order_id);
		int no_of_persons=dbHelperLetsGoDutch.getNoPayers(order_id);
		double totalpaymentDoneByAllPayers=0;
		double discountAllPayers=0;
		String signatureAllPayers="";
		for(int i=0;i<no_of_persons;i++){
			totalpaymentDoneByAllPayers+=allPersonData.get(i).PAID_AMOUNT;
			discountAllPayers+=0;
			signatureAllPayers+=allPersonData.get(i).SIGNATURE + "#";
		}
		
		HandlePaymentPaid hpp=new HandlePaymentPaid(order_id,totalpaymentDoneByAllPayers, discountAllPayers, signatureAllPayers);
		hpp.updateDataToServer();
		while(hpp.parsingComplete);
	}
	
	private void updateToServerAllPaymentPaidWhoHadTheLobster(){
		allPersonData=dbHelperWhoHadTheLobster.getAllPersons(order_id);
		int no_of_persons=dbHelperWhoHadTheLobster.getNoPayers(order_id);
		double totalpaymentDoneByAllPayers=0;
		double discountAllPayers=0;
		String signatureAllPayers="";
		for(int i=0;i<no_of_persons;i++){
			totalpaymentDoneByAllPayers+=allPersonData.get(i).PAID_AMOUNT;
			discountAllPayers+=0;
			signatureAllPayers+=allPersonData.get(i).SIGNATURE + "#";
		}
		
		HandlePaymentPaid hpp=new HandlePaymentPaid(order_id,totalpaymentDoneByAllPayers, discountAllPayers, signatureAllPayers);
		hpp.updateDataToServer();
		while(hpp.parsingComplete);
	}
	
//	private void updateToServerAllPaymentPaidItsOnMe(){
//
//	}
	
	
	
	private void startLetsGoDutchActivity(){
		Intent myIntent = new Intent(SignatureScreenMultiPayer.this,LetsGoDutchScreen.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity( myIntent);
	}
	
	private void startWhoHadTheLobsterActivity(){
		Intent myIntent = new Intent(SignatureScreenMultiPayer.this,WhoHadTheLobster.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity( myIntent);
	}
	
	private void startSinglePayerCardSnapshotScreen(){
		String signature=et_sign.getText().toString();
		extraBundle.putString("signature",signature);
		Bundle bundle=new Bundle();
		bundle.putBundle("databundle",extraBundle);
		Intent myIntent = new Intent(SignatureScreenMultiPayer.this,SinglePayerCardSnapshotScreen.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		myIntent.putExtras(bundle);
		startActivity( myIntent);
	}
	
	private void startSnapshotActivity(){
		Bundle bundle=new Bundle();
		bundle.putBundle("databundle",extraBundle);
		Intent myIntent = new Intent(SignatureScreenMultiPayer.this,SnapshotScreenMultipayer.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		myIntent.putExtras(bundle);
		startActivity( myIntent);
	}
	private void showAlertBox(String title,String message){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignatureScreenMultiPayer.this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.drawable.error);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            // User pressed YES button. Write Logic Here
            }
        	});
        alertDialog.show();	
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.signature_screen_multi_payer, menu);
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
