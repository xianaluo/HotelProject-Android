package com.example.hotelproject;


import java.util.ArrayList;
import multipayerpaymentscreens.TipCouponMultipayer;
import databaseutil.DBHelperLetsGoDutch;
import databaseutil.DBHelperWhoHadTheLobster;
import payments.PaymentSettings;
import serverutil.HandleServerDataTable;
import serverutil.ItemDescDS;
import singlepayerpaymentscreens.TipCoupon;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Whoispaying extends ActionBarActivity {
	final Context context = this;
	Button bt_itsonme,bt_letsgodutch,bt_whohadthelobster;
	double totalbillAmount;
	TextView billAmount,tv_orderid;
	int order_id;
	int tableno;
	ListView lv_item_table;
	ArrayList<ItemDescDS> all_items;
	DBHelperLetsGoDutch dBHelperLetsGoDutch;
	DBHelperWhoHadTheLobster dBHelperWhoHadTheLobster;
	InternetConnectionChecker ir;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_whoispaying);

		ir=new InternetConnectionChecker(this);
		
		if(ir.isNetworkAvailable())
			loadData();
		else{
			showNoInternetConnectionDialog();
		}
	    
   }
	private void showNoInternetConnectionDialog(){
		final Dialog dialog = new Dialog(this);
		Button dialogButton;
		dialog.setContentView(R.layout.custom_msg_box_no_internet_connection);
		dialog.setTitle("Oops...!");
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
		
		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
				Whoispaying.this.finish();
			}
		});
		dialogButton = (Button) dialog.findViewById(R.id.bt_retry);
		dialogButton.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
				if(ir.isNetworkAvailable())
					{	loadData();
						dialog.dismiss();
					}
				}
		});
		
	dialog.show();
	}
	
	
  private void loadData(){
	
	dBHelperLetsGoDutch=new DBHelperLetsGoDutch(this);
	dBHelperWhoHadTheLobster=new DBHelperWhoHadTheLobster(this);
	Bundle extras = getIntent().getExtras();
	tableno = Integer.parseInt(extras.getString("table_no").trim());
	HandleServerDataTable handleServerDataTable=new HandleServerDataTable(tableno);
	
	handleServerDataTable.fetchDataFromServer();
	while(handleServerDataTable.parsingComplete);
	
	all_items=handleServerDataTable.getTableItems();

	if(all_items==null)
		{
			Toast.makeText(this,"Bill For Table No "+tableno+ " has already paid",Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		
		}
	else
	{

			tv_orderid=(TextView)findViewById(R.id.tv_order_id_whoispaying);
			
			order_id=handleServerDataTable.getOrderId();
			tv_orderid.setText("Order ID : "+order_id);
			
			totalbillAmount=0;
			for(int i=0;i<all_items.size();i++)
				totalbillAmount+=all_items.get(i).total_price;
			//bill = XlsHandler.loadBill(billfilename);
			totalbillAmount=(double) Math.round(totalbillAmount * 100) / 100;
			billAmount = (TextView) findViewById(R.id.bt_amount_whoispaying);
			billAmount.setText(PaymentSettings.CURRENCY_SIGN+totalbillAmount);
			
			//TableLayout tb1=(TableLayout)findViewById(R.id.tbl_items);
		    lv_item_table=(ListView)findViewById(R.id.lv_item_table);
		    //lv_item_table.setAdapter(new MyAdapter(getApplicationContext(),currencyCodeNamefrom));
			
			MyListAdapter adapter = new MyListAdapter(Whoispaying.this, R.layout.food_items_row, all_items);
		    ListView billListView = (ListView)findViewById(R.id.lv_item_table);
		    billListView.setAdapter(adapter);
		    
		    
		    
		    bt_itsonme=(Button)findViewById(R.id.bt_itsonme);
		    bt_itsonme.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					boolean isAnyOnepaid=false;
					
					if(dBHelperLetsGoDutch.isThereAnyRecord(order_id)){
						if(dBHelperLetsGoDutch.isAnyOnePaid(order_id)){
							isAnyOnepaid=true;
							showAlertBox("Error","Some payment is Already Made Through \"Lets Go Dutch\" Option So You Can't Choose \"Its on Me\"");
							return;
						}
					}
					
					if(dBHelperWhoHadTheLobster.isThereAnyRecord(order_id)){
						if(dBHelperWhoHadTheLobster.isAnyOnePaid(order_id)){
							isAnyOnepaid=true;
							showAlertBox("Error","Some payment is Already Made Through \"Who Had The Lobster\" Option So You Can't Choose \"Its on Me\"");
							return;
						}
					}
					
					if(isAnyOnepaid==false){
						dBHelperLetsGoDutch.deleteData(order_id);
						dBHelperWhoHadTheLobster.deleteData(order_id);
						Bundle extradataBundle=new Bundle();
						extradataBundle.putInt("order_id", order_id);
						extradataBundle.putDouble("totaldue", totalbillAmount );
						extradataBundle.putString("who_is_paying","its_on_me");
						Bundle bundle=new Bundle();
						bundle.putBundle("databundle",extradataBundle);
				        Intent sd=new Intent(Whoispaying.this,TipCouponMultipayer.class);
				        sd.putExtras(bundle);
				        startActivity(sd);
						
					}
					
				}
			});
			
		    bt_letsgodutch=(Button)findViewById(R.id.bt_letsgoductch);
		    bt_letsgodutch.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					if (dBHelperWhoHadTheLobster.isThereAnyRecord(order_id)) {
						   if(!dBHelperWhoHadTheLobster.isAnyOnePaid(order_id)){
							   dBHelperWhoHadTheLobster.deleteData(order_id);
							   
								if (dBHelperLetsGoDutch.isThereAnyRecord(order_id)) {
									   if(!dBHelperLetsGoDutch.isAnyOnePaid(order_id)){
										    showNoOfPersonDailogBox(1);
									   }else{
										   
										    Toast.makeText(Whoispaying.this,"Some Payment is Already Done so You cannot change No Persons", Toast.LENGTH_LONG).show();
										    SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences",Context.MODE_PRIVATE);
					                		SharedPreferences.Editor editor = sharedPreferences.edit();
					                	    editor.clear();
					                	    editor.putString("totaldue",""+totalbillAmount);
					                	    editor.putInt("order_id", order_id);
					                	    editor.putInt("no_of_payers", 0);
					                	    
					                	    editor.commit();
					                		Intent myIntent = new Intent(Whoispaying.this, LetsGoDutchScreen.class);
					                		startActivity( myIntent);
											
									   }
							    }else{
							    	showNoOfPersonDailogBox(1);
							    }   
						   
						   }else{
							   showAlertBox("Error","Some payment is Already Made Through \"Who Had The Lobster\" Option So You Can't Choose \"Lets Go Dutch\"");
						   }
				    }else{
						if (dBHelperLetsGoDutch.isThereAnyRecord(order_id)) {
							   if(!dBHelperLetsGoDutch.isAnyOnePaid(order_id)){
								    showNoOfPersonDailogBox(1);
							   }else{
								   
								    Toast.makeText(Whoispaying.this,"Some Payment is Already Done so You cannot change No Persons", Toast.LENGTH_LONG).show();
								    SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences",Context.MODE_PRIVATE);
			                		SharedPreferences.Editor editor = sharedPreferences.edit();
			                	    editor.clear();
			                	    editor.putString("totaldue",""+totalbillAmount);
			                	    editor.putInt("order_id", order_id);
			                	    editor.putInt("no_of_payers", 0);
			                	    
			                	    editor.commit();
			                		Intent myIntent = new Intent(Whoispaying.this, LetsGoDutchScreen.class);
			                		startActivity( myIntent);
									
							   }
					    }else{
					    	showNoOfPersonDailogBox(1);
					    }
				    }

					
	                
				}
	     
			});
		    
		    bt_whohadthelobster=(Button)findViewById(R.id.bt_whohadthelobster);
		    bt_whohadthelobster.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (dBHelperLetsGoDutch.isThereAnyRecord(order_id)) {
						   if(!dBHelperLetsGoDutch.isAnyOnePaid(order_id)){
							    dBHelperLetsGoDutch.deleteData(order_id);
								    if (dBHelperWhoHadTheLobster.isThereAnyRecord(order_id)) {
										   if(!dBHelperWhoHadTheLobster.isAnyOnePaid(order_id)){
											    showNoOfPersonDailogBox(2);
										   }else{
											   
											    Toast.makeText(Whoispaying.this,"Some Payment is Already Done so You cannot change No Persons", Toast.LENGTH_LONG).show();
											    SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences",Context.MODE_PRIVATE);
						                		SharedPreferences.Editor editor = sharedPreferences.edit();
						                	    editor.clear();
						                	    editor.putString("totaldue",""+totalbillAmount);
						                	    editor.putInt("order_id", order_id);
						                	    editor.putInt("no_of_payers", 0);
						                	    editor.putInt("table_no",tableno);
						                	    editor.commit();
						                		Intent myIntent = new Intent(Whoispaying.this, WhoHadTheLobster.class);
						                		startActivity( myIntent);
												
										   }
								    }else{
								    	showNoOfPersonDailogBox(2);
								    }  
							 }else{
								 showAlertBox("Error","Some payment is Already Made Through \"Lets Go Dutch\" Option So You Can't Choose \"Who Had The Lobster\"");
							 }
					}else{
						if (dBHelperWhoHadTheLobster.isThereAnyRecord(order_id)) {
							   if(!dBHelperWhoHadTheLobster.isAnyOnePaid(order_id)){
								    showNoOfPersonDailogBox(2);
							   }else{
								   
								    Toast.makeText(Whoispaying.this,"Some Payment is Already Done so You cannot change No Persons", Toast.LENGTH_LONG).show();
								    SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences",Context.MODE_PRIVATE);
			                		SharedPreferences.Editor editor = sharedPreferences.edit();
			                	    editor.clear();
			                	    editor.putString("totaldue",""+totalbillAmount);
			                	    editor.putInt("order_id", order_id);
			                	    editor.putInt("no_of_payers", 0);
			                	    editor.putInt("table_no",tableno);
			                	    editor.commit();
			                		Intent myIntent = new Intent(Whoispaying.this, WhoHadTheLobster.class);
			                		startActivity( myIntent);
									
							   }
					    }else{
					    	showNoOfPersonDailogBox(2);
					    }  

					}
					

				}
			});
    
   }
		
}
	private void showNoOfPersonDailogBox(final int opt){
		final Dialog dialog =new Dialog(context);
		 
        //tell the Dialog to use the dialog.xml as it's layout description
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.no_of_persons_are_paying_dialog);
    //    String title="Total: "+ PaymentSettings.CURRENCY_SIGN+totalbillAmount;
        

        final TextView tv_no_of_persons = (TextView) dialog.findViewById(R.id.et_no_of_persons);

        Button bt_go = (Button) dialog.findViewById(R.id.bt_go_no_of_persons);
        
        bt_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(!tv_no_of_persons.getText().toString().trim().equals(""))
                {
                	
                	int n=Integer.parseInt(tv_no_of_persons.getText().toString());
                
                	if(n<=0)
                	{
                		showAlertBox("Error", "Invalid No. of Persons....!");
                	}
                	else if(n==1)
                	{
                		Bundle bundle = new Bundle();
                		bundle.putString("totaldue",""+totalbillAmount);
                		bundle.putString("order_id", ""+order_id );
                		Intent myIntent = new Intent(Whoispaying.this, TipCoupon.class);
                		myIntent.putExtras(bundle);
                		startActivity( myIntent);
                	}
                	else{
                		
                		SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences",Context.MODE_PRIVATE);
                		SharedPreferences.Editor editor = sharedPreferences.edit();
                	    editor.clear();
                	    editor.putString("totaldue",""+totalbillAmount);
                	    editor.putInt("order_id", order_id);
                	    editor.putInt("no_of_payers", Integer.parseInt(tv_no_of_persons.getText().toString()));
                	    editor.putInt("table_no",tableno);
                	    editor.commit();
                	    
	                	    if(opt==1){
	                		       Intent myIntent = new Intent(Whoispaying.this, LetsGoDutchScreen.class);
	                		       startActivity( myIntent);
	                	    }else{
	                	    	   Intent myIntent = new Intent(Whoispaying.this, WhoHadTheLobster.class);
	                		       startActivity( myIntent);
	                	    }
                		
                	    }
                	}else{
                		showAlertBox("Error","Field Cannot be Blank");
                	}
                }
                
                
        		});

        	dialog.show();
	}
	
	private void showAlertBox(String title,String message){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(Whoispaying.this);
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
		getMenuInflater().inflate(R.menu.whoispaying, menu);
		return super.onCreateOptionsMenu(menu);
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
	
	public class MyListAdapter extends ArrayAdapter<ItemDescDS>{

		private ArrayList<ItemDescDS> items;
		private int layoutResourceId;
		public MyListAdapter(Context context, int layoutResourceId, ArrayList<ItemDescDS> items) {
			super(context, layoutResourceId, items);
			this.layoutResourceId = layoutResourceId;
			this.items = items;
		}
		
		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View row = convertView;
			
			LayoutInflater inflater = getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			
			TextView tv_qty = (TextView)row.findViewById(R.id.tv_item_qty);
			TextView tv_name = (TextView)row.findViewById(R.id.tv_item_name);
			TextView tv_amount = (TextView)row.findViewById(R.id.tv_item_rate);
			
			tv_qty.setText(""+items.get(position).units);
			tv_name.setText(items.get(position).item_name);
			tv_amount.setText(PaymentSettings.CURRENCY_SIGN+items.get(position).total_price);

			return row;
			
		}

	}

	
	
	
	
	
	
	
	
	
}
