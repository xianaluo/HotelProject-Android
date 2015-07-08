package singlepayerpaymentscreens;

import com.example.hotelproject.R;
import com.example.hotelproject.R.drawable;
import com.example.hotelproject.R.id;
import com.example.hotelproject.R.layout;
import com.example.hotelproject.R.menu;

import payments.PaymentSettings;
import serverutil.HandleCoupons;
import singlepayerpaymentscreens.RecieptScreen.EmailValidator;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TipCoupon extends ActionBarActivity {
	final Context context = this;
	Button bt_show_amount,bt_total_amount,bt_tipamountabsolute,bt_agreepay;
	EditText et_custom_tip;
	double billAmount,totaldue,tipAmount,discount_amount;
	ImageView iv_couponcode;
	TextView tv_coupon_applied;
	String order_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sp_tip_coupon);
		tipAmount=0;
		discount_amount=0;
		Intent intent = getIntent();
		String amt = intent.getStringExtra("totaldue");
		order_id=intent.getStringExtra("order_id");
		billAmount=Double.parseDouble(amt);
		
		bt_show_amount=(Button)findViewById(R.id.bt_amount_tipcoupon);
		bt_show_amount.setText("Total: "+PaymentSettings.CURRENCY_SIGN+ amt);
		bt_total_amount=(Button)findViewById(R.id.bt_totaldue);
		
		tipAmount=(billAmount*15)/100;
		totaldue=(billAmount+tipAmount);
		bt_total_amount.setText("Total due: "+ PaymentSettings.CURRENCY_SIGN+totaldue);
		
		bt_tipamountabsolute=(Button)findViewById(R.id.bt_tipamountabsolute);
		bt_tipamountabsolute.setText("Tip @ 15%");
		bt_tipamountabsolute.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PopupMenu popupMenu = new PopupMenu(TipCoupon.this, v);
				popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					
					
					@Override
					public boolean onMenuItemClick(MenuItem arg0) {
						// TODO Auto-generated method stub
						
						switch (arg0.getItemId()) {
						case R.id.tip_rate_5:
							tipAmount=(billAmount*5)/100;
							totaldue=(billAmount+tipAmount-discount_amount);
							bt_total_amount.setText("Total due: "+PaymentSettings.CURRENCY_SIGN+ totaldue);
							bt_tipamountabsolute.setText("Tip @ 5%");
							break;
						case R.id.tip_rate_10:
							tipAmount=(billAmount*10)/100;
							totaldue=(billAmount+tipAmount-discount_amount);
							bt_total_amount.setText("Total due: "+PaymentSettings.CURRENCY_SIGN+ totaldue);
							bt_tipamountabsolute.setText("Tip @ 10%");
							break;
						case R.id.tip_rate_15:
							tipAmount=(billAmount*15)/100;
							totaldue=(billAmount+tipAmount-discount_amount);
							bt_total_amount.setText("Total due: "+PaymentSettings.CURRENCY_SIGN+ totaldue);
							bt_tipamountabsolute.setText("Tip @ 15%");
							break;
						case R.id.tip_rate_20:
							tipAmount=(billAmount*20)/100;
							totaldue=(billAmount+tipAmount-discount_amount);
							bt_total_amount.setText("Total due: "+PaymentSettings.CURRENCY_SIGN+ totaldue);;
							bt_tipamountabsolute.setText("Tip @ 20%");
							break;
						case R.id.tip_rate_25:
							tipAmount=(billAmount*25)/100;
							totaldue=(billAmount+tipAmount-discount_amount);
							bt_total_amount.setText("Total due: "+PaymentSettings.CURRENCY_SIGN+ totaldue);
							bt_tipamountabsolute.setText("Tip @ 25%");
							break;
						}
						return false;
					}
				});
				popupMenu.inflate(R.menu.rate_popup_menu);
				popupMenu.show();
			}
		});
		
		et_custom_tip=(EditText)findViewById(R.id.et_custom_tip_amount);
		et_custom_tip.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
	        	if(!et_custom_tip.getText().toString().equals(""))
	        	{ 
	        		double ct=Double.parseDouble(et_custom_tip.getText().toString());
	        		tipAmount=ct;
	        		totaldue=(billAmount+tipAmount-discount_amount);
	        		bt_total_amount.setText("Total due: "+PaymentSettings.CURRENCY_SIGN+ totaldue);
	        		bt_tipamountabsolute.setText("Flat Rate");
            
	        	}
	        	else
	        		{
	        		tipAmount=0;
	        		bt_total_amount.setText("Total due: "+PaymentSettings.CURRENCY_SIGN+ (billAmount));
	        		bt_tipamountabsolute.setText("Flat Rate");
	        		
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
		bt_agreepay=(Button)findViewById(R.id.bt_agreeandpay);
		bt_agreepay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				
				bundle.putString("totaldue",""+totaldue);
				bundle.putString("order_id", order_id);
				bundle.putString("discount",""+ discount_amount);
				
				Bundle extrabundle=new Bundle();
				extrabundle.putBundle("databundle", bundle);
				Intent myIntent = new Intent(TipCoupon.this, RecieptScreen.class);
				myIntent.putExtras(extrabundle);
				
				startActivity( myIntent);
			}
		});
		
		tv_coupon_applied=(TextView)findViewById(R.id.tv_coupon_applied_tipcoupon);
		tv_coupon_applied.setText("No Coupon Applied");
		iv_couponcode=(ImageView)findViewById(R.id.iv_coupon_tipcoupon);
		iv_couponcode.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Dialog dialog =new Dialog(context);
				 
                //tell the Dialog to use the dialog.xml as it's layout description
                dialog.setContentView(R.layout.coupon_dialog);
                dialog.setTitle("Enter Coupon Code");
 
                final TextView tv_coupon_code = (TextView) dialog.findViewById(R.id.et_coupon_code);
 
                Button bt_apply = (Button) dialog.findViewById(R.id.bt_apply_coupon);
 
                bt_apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        HandleCoupons handleCoupons=new HandleCoupons(tv_coupon_code.getText().toString());
                        handleCoupons.fetchDataFromServer();
                        while(handleCoupons.parsingComplete);
                        
                        if(handleCoupons.isCouponValid()){
                        	if(handleCoupons.isCouponAbsolute()){
                        		double absolute_amount=handleCoupons.getDiscountUnit();
                        		
                        		if(billAmount<absolute_amount)
                        		{
                        			discount_amount=0;
                            		totaldue=(billAmount-discount_amount+tipAmount);
                        			bt_total_amount.setText("Total due: "+PaymentSettings.CURRENCY_SIGN+ totaldue);
                        			tv_coupon_applied.setText("No Coupon Applied");
                                	AlertDialog.Builder alertDialog = new AlertDialog.Builder(TipCoupon.this);
                                    alertDialog.setTitle("Error");
                                    alertDialog.setMessage("This Coupon Code is Not Applicable...!");
                                    alertDialog.setIcon(R.drawable.error);
                                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        // User pressed YES button. Write Logic Here
                                        }
                                    	});
                                    // Showing Alert Message
                                    alertDialog.show();	
                        		}
                        		else{
                        			discount_amount=absolute_amount;
                        			totaldue=(billAmount-discount_amount+tipAmount);
                        			tv_coupon_applied.setText("Coupon Applied \""+tv_coupon_code.getText() +"\" you got " + PaymentSettings.CURRENCY_SIGN+ discount_amount +" Discount");
                        			bt_total_amount.setText("Total due: "+PaymentSettings.CURRENCY_SIGN+ totaldue);
                        		}
    							
                        	}
                        	else{
                        		tv_coupon_applied.setText("Coupon Applied \""+tv_coupon_code+"\" you got ");
                        		discount_amount=(billAmount*handleCoupons.getDiscountUnit())/100;
                        		totaldue=(billAmount-discount_amount+tipAmount);
                        		tv_coupon_applied.setText("Coupon Applied \""+tv_coupon_code.getText()+"\" you got " + PaymentSettings.CURRENCY_SIGN+ discount_amount +" Discount");
                    			bt_total_amount.setText("Total due: "+PaymentSettings.CURRENCY_SIGN+ totaldue);

                        	}
                        	
                        }else{
                        	discount_amount=0;
                    		totaldue=(billAmount-discount_amount+tipAmount);
                			bt_total_amount.setText("Total due: "+PaymentSettings.CURRENCY_SIGN+ totaldue);
                			tv_coupon_applied.setText("No Coupon Applied");
                        	AlertDialog.Builder alertDialog = new AlertDialog.Builder(TipCoupon.this);
                            alertDialog.setTitle("Error");
                            alertDialog.setMessage("Invalid Coupon Code...!");
                            alertDialog.setIcon(R.drawable.error);
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
		
	}
	
	
	public void setAmountTipButtonsText(double totaldue,int tip){
		bt_total_amount.setText("Total due: "+PaymentSettings.CURRENCY_SIGN+ totaldue);
		if(tip==0)
			bt_tipamountabsolute.setText("Flat Rate");
		else{
	    	bt_tipamountabsolute.setText("Tip @ "+tip+"%");
		}
	}
	
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
		
		}
		return false;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tip_coupon, menu);
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
