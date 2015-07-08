package com.example.hotelproject;


import serverutil.HandleNoOfTables;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;


@SuppressLint("InflateParams")
public class MainActivity extends ActionBarActivity {
	InternetConnectionChecker ir; 
	private int noOfTables = 0;

	@SuppressLint("InflateParams")
	private void loadData() {
		//Toast.makeText(MainActivity.this, "Dir: "+Environment.getExternalStoragePublicDirectory().getExternalStorageDirectory(), Toast.LENGTH_SHORT).show();
		
		HandleNoOfTables hd=new HandleNoOfTables();
		hd.fetchDataFromServer();
		while(hd.parsingComplete);
		
		TableLayout table_layout=(TableLayout)findViewById(R.id.main_menu_table);
		table_layout.removeAllViews();
		noOfTables=hd.getNoOfTables();

		
		int n=noOfTables;//20;
		int jn=n%3;
		int in= n - jn;
		
		int count=0;
		Button tempb = null;
		for(int i=0;i<in;i++ ) {
			
			TableRow tr=(TableRow)LayoutInflater.from(MainActivity.this).inflate(R.layout.main_menu_table_row_col3, null);
			
			tempb = (Button)tr.findViewById(R.id.bt_checkout);
			setUpButton(tempb,++count);			
			
			i++;
			tempb = (Button)tr.findViewById(R.id.button2);
			setUpButton(tempb,++count);			
			
			i++;
			tempb = (Button)tr.findViewById(R.id.button3);
			setUpButton(tempb,++count);			
			
			table_layout.addView(tr);
			
		}
		
		if(jn==2)	{
			
			TableRow tr=(TableRow)LayoutInflater.from(MainActivity.this).inflate(R.layout.main_menu_table_row_col2, null);
			
			tempb = (Button)tr.findViewById(R.id.bt_checkout);
			setUpButton(tempb,++count);			
			
			tempb = (Button)tr.findViewById(R.id.button2);
			setUpButton(tempb,++count);			
			
			table_layout.addView(tr);
		
		}
		else if(jn==1){
			
			TableRow tr=(TableRow)LayoutInflater.from(MainActivity.this).inflate(R.layout.main_menu_table_row_col1, null);

			tempb = (Button)tr.findViewById(R.id.bt_checkout);
			setUpButton(tempb,++count);			
			table_layout.addView(tr);
		
		}
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ir=new InternetConnectionChecker(this);
		
		if(ir.isNetworkAvailable())
			loadData();
		else{
			showNoInternetConnectionDialog();
		}
		

		
	//	tableList.clear();
		
		
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
				MainActivity.this.finish();
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
	
	private void setUpButton(Button tempb, final int table_no) {
		
			tempb.setText("Table "+table_no);
			tempb.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					startBillActivity(table_no);
					
				}
				
			});

	}
	
	private void startBillActivity(int table_no) {
	
	Bundle bundle = new Bundle();
	bundle.putString("table_no", ""+table_no);
	Intent myIntent = new Intent(MainActivity.this, Whoispaying.class);
	myIntent.putExtras(bundle);
	//Toast.makeText(MainActivity.this, "Generating bill: "+billfilename, Toast.LENGTH_SHORT).show();
	startActivity(myIntent);
	
}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
