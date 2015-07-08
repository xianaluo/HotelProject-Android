package com.example.hotelproject;

import java.util.ArrayList;

import multipayerpaymentscreens.TipCouponMultipayer;

import com.example.hotelproject.R;

import databaseutil.DBHelperLetsGoDutch;
import databaseutil.PersonRowDS;
import payments.PaymentSettings;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;


public class LetsGoDutchScreen extends ActionBarActivity {
	double perpersonamount,totaldue;
	ArrayList<TableRow> tableRows;
	TableLayout table_layout;
	boolean no_one_paid;
	boolean payers_paid[];
	String[] payers_name;
	double[] payers_payment_due;
	int no_of_payers,no_of_payers_noonepaid;
	int order_id;
	DBHelperLetsGoDutch mydb;
	
	int payer_remove_index;
	//Bundle extraBundle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lets_go_dutch_screen);
		
		//load from prev activity
		   SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences",Context.MODE_PRIVATE);
			//extraBundle=getIntent().getBundleExtra("databundle");
			order_id=sharedPreferences.getInt("order_id",0);                       // extraBundle.getInt("order_id");
			totaldue=Double.parseDouble(sharedPreferences.getString("totaldue", "0")) ;        //extraBundle.getDouble("totaldue");
			no_of_payers=no_of_payers_noonepaid= sharedPreferences.getInt("no_of_payers", 0);             //extraBundle.getInt("no_of_payers");
		
    	// System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> no_of_payers " + no_of_payers_noonepaid);
		loadData();
		prepareScreen();
		//loadPreferences();
		
	}
	@SuppressLint("InflateParams")
	public void prepareScreen(){
		table_layout=(TableLayout)findViewById(R.id.tl_lets_go_dutch_screen);
		table_layout.removeAllViews();
		Button tempb = null;
		ImageView tempiv=null;
		EditText tempet=null;
		tableRows=new ArrayList<TableRow>();
		
		int i;
		for(i=0;i<no_of_payers-1;i++ ) {
			
			TableRow tr=(TableRow)LayoutInflater.from(LetsGoDutchScreen.this).inflate(R.layout.payer_row_with_concatinate, null);
			tempb = (Button)tr.findViewById(R.id.bt_payer_name);
			setUpButton(tempb,payers_name[i]);			
			tempet=(EditText)tr.findViewById(R.id.et_payer_amount);
			tempet.setText(PaymentSettings.CURRENCY_SIGN+payers_payment_due[i]);
			tempiv=(ImageView)tr.findViewById(R.id.iv_concatinate);
			setUpImageViewButton(tempiv);
			if(payers_paid[i]==true){
				tempb.setEnabled(false);
				tempb.setBackgroundResource(R.drawable.button_flat_disable_grey);
				tempiv.setEnabled(false);
			}
			tableRows.add(tr);
			table_layout.addView(tr);
			
		}
		
		TableRow tr=(TableRow)LayoutInflater.from(LetsGoDutchScreen.this).inflate(R.layout.payer_row_with_concatinate, null);
		tempb = (Button)tr.findViewById(R.id.bt_payer_name);
		setUpButton(tempb,payers_name[i]);	
		tempet=(EditText)tr.findViewById(R.id.et_payer_amount);
		tempet.setText(PaymentSettings.CURRENCY_SIGN+payers_payment_due[i]);
		tempiv=(ImageView)tr.findViewById(R.id.iv_concatinate);
		if(payers_paid[i]==true){
			tempb.setEnabled(false);
			tempb.setBackgroundResource(R.drawable.button_flat_disable_grey);
			tempiv.setEnabled(false);
		}
		tempiv.setVisibility(TRIM_MEMORY_UI_HIDDEN);
		tableRows.add(tr);		
		table_layout.addView(tr);
	}
	
	
	public void loadData(){
		   

		   mydb = new DBHelperLetsGoDutch(this);
		   
		   if (mydb.isThereAnyRecord(order_id)) {
			   if(!mydb.isAnyOnePaid(order_id)){
				    mydb.deleteData(order_id);
				    no_one_paid=true;
				   
				    no_of_payers=no_of_payers_noonepaid;
			    	perpersonamount=totaldue/no_of_payers;
			    	perpersonamount=(double) Math.round(perpersonamount * 100) / 100;
		    		payers_paid=new boolean[no_of_payers];
		    		payers_name=new String[no_of_payers];
		    		payers_payment_due=new double[no_of_payers];
	    			for(int i=0;i<no_of_payers;i++){
		    			  payers_paid[i]=false;
		    			  payers_name[i]="Payer "+(i+1);
		    			  payers_payment_due[i]=perpersonamount;
		    		  }
			   }else{
				    no_of_payers=mydb.getNoPayers(order_id);
		    		payers_paid=new boolean[no_of_payers];
		    		payers_name=new String[no_of_payers];
		    		payers_payment_due=new double[no_of_payers];
		    		
		    		ArrayList<PersonRowDS> array_list=mydb.getAllPersons(order_id);
		    		  for(int i=0;i<no_of_payers;i++){
		    			//  System.out.println(array_list.get(i).PAID  + array_list.get(i).PERSON_NAME + array_list.get(i).PAID_AMOUNT);
		    			  payers_paid[i]=array_list.get(i).PAID;
		    			  payers_name[i]=array_list.get(i).PERSON_NAME;
		    			  payers_payment_due[i]=array_list.get(i).PAID_AMOUNT;
		    		  }
			   }
		   }else{
		    	no_one_paid=true;
		    	perpersonamount=totaldue/no_of_payers;
		    	perpersonamount=(double) Math.round(perpersonamount * 100) / 100;
		    	payers_paid=new boolean[no_of_payers];
		    	payers_name=new String[no_of_payers];
		    	payers_payment_due=new double[no_of_payers];
   			    for(int i=0;i<no_of_payers;i++){
	    			  payers_paid[i]=false;
	    			  payers_name[i]="Payer "+(i+1);
	    			  payers_payment_due[i]=perpersonamount;
	    		  }
		   }
		   
		
	}
	
	public void saveData(){
	    int no_of_rows=tableRows.size();
	    no_of_payers=no_of_rows;
	    ArrayList<PersonRowDS> arrayList=mydb.getAllPersons(order_id);
	    mydb.deleteData(order_id);
	    for(int i=0;i<no_of_rows;i++){
	    	Button tempb=(Button)tableRows.get(i).findViewById(R.id.bt_payer_name);
	    	EditText tempet=(EditText)tableRows.get(i).findViewById(R.id.et_payer_amount);
	    	if(tempb.isEnabled())
	    	{ 
	    		mydb.insertData(order_id, tempb.getText().toString(),false, Double.parseDouble(tempet.getText().toString().replace("$","")) );
	    	}else{
	    		for(int j=0;i<arrayList.size();j++){
	    			if(arrayList.get(j).PERSON_NAME.equals(tempb.getText().toString())){
	    				mydb.insertDataFull(order_id, arrayList.get(j).PERSON_NAME, arrayList.get(j).PAID, arrayList.get(j).PAID_AMOUNT, arrayList.get(j).PAYMENT_TYPE, arrayList.get(j).CASH_DUE_BACK,arrayList.get(j).SIGNATURE,arrayList.get(j).EMAIL_ID);
	    				break;
	    			}
	    		}
	    		//mydb.updateData(order_id, person_name, paid, paid_amount, payment_type, cash_due_back, signature)Data(order_id, tempb.getText().toString(),true, Double.parseDouble(tempet.getText().toString().replace("$","")) );
	    	}
	
	    }
	}
	
	
	private void setUpImageViewButton(ImageView tempiv){
		tempiv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				//startBillActivity(table_no);
				for(int i=0;i<tableRows.size();i++){
					ImageView tiv=(ImageView)tableRows.get(i).findViewById(R.id.iv_concatinate);
				
					if(tiv.equals(v)  )
						{
						  ImageView tivNext=(ImageView)tableRows.get(i+1).findViewById(R.id.iv_concatinate);
							if(tivNext.isEnabled()){
								EditText tempet=(EditText)tableRows.get(i).findViewById(R.id.et_payer_amount);
								EditText tempet2=(EditText)tableRows.get(i+1).findViewById(R.id.et_payer_amount);
								Double personamountnext=Double.parseDouble(tempet2.getText().toString().replace("$",""));
								
								Double personamount= Double.parseDouble(tempet.getText().toString().replace("$","")) + personamountnext;
								tempet.setText(PaymentSettings.CURRENCY_SIGN+(personamount));
								
								//System.out.println("Size : " + tableRows.size() + "  Index "+ i);
								if((i+2)==tableRows.size())
								{
									tiv.setVisibility(TRIM_MEMORY_UI_HIDDEN);
								}
								else if(i==0 && tableRows.size()==2){
									tiv.setVisibility(TRIM_MEMORY_UI_HIDDEN);
								}

								table_layout.removeViewAt(i+1);
								tableRows.remove(i+1);
							}
							

						}
				}
				
			}
			
		});
	}
	
	private void setUpButton(final Button tempb,final String payer_name) {
		
		tempb.setText(payer_name);
		tempb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				saveData();

			    EditText tempet=null;
			    for(int i=0;i<tableRows.size();i++){
			    	
			    	Button tempb=(Button)tableRows.get(i).findViewById(R.id.bt_payer_name);
			    	if(tempb==((Button)v))
			    	{
			    	    tempet=(EditText)tableRows.get(i).findViewById(R.id.et_payer_amount);
			    	    break;
			    	}
			
			    }

				Bundle extradataBundle=new Bundle();
				
				extradataBundle.putInt("order_id", order_id);
				extradataBundle.putString("payer_name",((Button)v).getText().toString());
				extradataBundle.putDouble("totaldue", Double.parseDouble(tempet.getText().toString().replace("$","")) );
				extradataBundle.putString("who_is_paying","lets_go_dutch");
				
				Bundle bundle=new Bundle();
				bundle.putBundle("databundle",extradataBundle);
		        Intent sd=new Intent(LetsGoDutchScreen.this,TipCouponMultipayer.class);
		        sd.putExtras(bundle);
		        startActivity(sd);
				
			}
			
		});
		
		tempb.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				ClipData data = ClipData.newPlainText("DRAG", "");
		        View.DragShadowBuilder shadow = new View.DragShadowBuilder(v);
		        v.startDrag(data, shadow, null, 0);
		        
		        for(int i=0;i<tableRows.size();i++){
		        	Button bt_temp=(Button)tableRows.get(i).findViewById(R.id.bt_payer_name);
		        	if(tempb.equals(bt_temp)){
		        		payer_remove_index=i;
		        		break;
		        	}
		        	
		        }
				return false;
			}
		});
		
		
		tempb.setOnDragListener(new View.OnDragListener() {
		      @Override
		      public boolean onDrag(View v, DragEvent event) {
		        // TODO Auto-generated method stub
		      final int action = event.getAction();
		            switch(action) {
		            case DragEvent.ACTION_DRAG_STARTED:
		            	break;
		            case DragEvent.ACTION_DRAG_EXITED:
		            	break;
		            case DragEvent.ACTION_DRAG_ENTERED:
		    	  		break;
		            case DragEvent.ACTION_DROP:{
				    			  
							
			            	for(int i=0;i<tableRows.size();i++){
								Button bt_temp_payer=(Button)tableRows.get(i).findViewById(R.id.bt_payer_name);
							
								if(bt_temp_payer.equals(v)  )
									{
											EditText tempet=(EditText)tableRows.get(i).findViewById(R.id.et_payer_amount);
											EditText tempet2=(EditText)tableRows.get(payer_remove_index).findViewById(R.id.et_payer_amount);
											Double personamountnext=Double.parseDouble(tempet2.getText().toString().replace(PaymentSettings.CURRENCY_SIGN,""));
											
											Double personamount= Double.parseDouble(tempet.getText().toString().replace(PaymentSettings.CURRENCY_SIGN,"")) + personamountnext;
											tempet.setText(PaymentSettings.CURRENCY_SIGN+(personamount));
											
											//System.out.println("Size : " + tableRows.size() + "  Index "+ i);
											if(payer_remove_index+1==tableRows.size())
											{
												
												    ImageView tiv=(ImageView)tableRows.get(payer_remove_index-1).findViewById(R.id.iv_concatinate);
												    tiv.setVisibility(TRIM_MEMORY_UI_HIDDEN);
											}
											table_layout.removeViewAt(payer_remove_index);	
											tableRows.remove(payer_remove_index);
											
											 MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.ding);
							    			  mp.start();
									}
							}
							
				    	 }
		        	//	Toast.makeText(WhoHadTheLobster.this,"Finaly Droped", Toast.LENGTH_LONG).show();           	
		                return(true);

		            case DragEvent.ACTION_DRAG_ENDED:
		            	break;
		            default:
		            	break;
		            }
		      return true;
		      }});

    }
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		loadData();
		prepareScreen();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lets_go_dutch_screen, menu);
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
