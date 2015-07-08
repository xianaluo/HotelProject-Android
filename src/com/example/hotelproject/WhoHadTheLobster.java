package com.example.hotelproject;

import java.util.ArrayList;

import multipayerpaymentscreens.TipCouponMultipayer;
import payments.PaymentSettings;
import serverutil.HandleServerDataTable;
import serverutil.ItemDescDS;
import databaseutil.DBHelperWhoHadTheLobster;
import databaseutil.PersonRowDS;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class WhoHadTheLobster extends ActionBarActivity {
	
	ListView lv_item_table;
	ArrayList<ItemDescDS> all_items;
	HandleServerDataTable handleServerDataTable;
	DBHelperWhoHadTheLobster dBHelperWhoHadTheLobster;
	Button bt_total_amount;
	TableLayout table_layout;
	ArrayList<TableRow> tableRows;
	
	boolean payers_paid[];
	String[] payers_name;
	boolean payers_hilight[];
	double[] payers_payment_due;
	int tableno;
	int order_id;
	int no_of_payers;
	double perpersonamount,totaldue;
	int total , failure = 0;
	
	double drop_item_amount;
	int drop_item_remove_index;
	
	MyListAdapter adapter ;
	ListView billListView ;
	
	RelativeLayout rl_tempFoodRow;
	Drawable rlBackground;
	
	private void loadInitialData(){
		
		//load from prev activity
		   SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences",Context.MODE_PRIVATE);
			order_id=sharedPreferences.getInt("order_id",0);                       
			totaldue=Double.parseDouble(sharedPreferences.getString("totaldue", "0")) ;
			no_of_payers=sharedPreferences.getInt("no_of_payers", 0);             
			 tableno = sharedPreferences.getInt("table_no", 0);
// 	         System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> no_of_payers " + tableno);

 	         
		     lv_item_table=(ListView)findViewById(R.id.lv_item_table_who_had_the_lobster);
		  //   bt_total_amount=(Button)findViewById(R.id.bt_amount_who_had_the_lobster);
		     
		     dBHelperWhoHadTheLobster=new DBHelperWhoHadTheLobster(this);	
		     handleServerDataTable=new HandleServerDataTable(tableno);
			 handleServerDataTable.fetchDataFromServer();
			 while(handleServerDataTable.parsingComplete);	
			 all_items=handleServerDataTable.getTableItems();
			 
			// bt_total_amount.setText("Total: "+PaymentSettings.CURRENCY_SIGN+totaldue);
			 tableRows=new ArrayList<TableRow>();
	}
	
	MyView myView;
	View tempView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_who_had_the_lobster);
		myView =new MyView(this);
		loadInitialData();
		if(tableno==0){
			Toast.makeText(this,"Something Went Wrong Server is Not Responding or Table Does not Exits",Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
		else if(all_items==null)
		{
			Toast.makeText(this,"Bill For Table No "+tableno+ " has already paid",Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		
		}
	   else if(!dBHelperWhoHadTheLobster.isAnyOnePaid(order_id))
	    {
		    order_id=handleServerDataTable.getOrderId();
		   
		    adapter = new MyListAdapter(WhoHadTheLobster.this, R.layout.food_items_row, all_items);
		    billListView = (ListView)findViewById(R.id.lv_item_table_who_had_the_lobster);
		    billListView.setAdapter(adapter);		    
		    loadData();
		    prepareScreen();
	    }else{
	    	all_items=new ArrayList<ItemDescDS>();
	    	adapter = new MyListAdapter(WhoHadTheLobster.this, R.layout.food_items_row, all_items);
	    	billListView = (ListView)findViewById(R.id.lv_item_table_who_had_the_lobster);
		    billListView.setAdapter(adapter);	
	    	loadData();
		    prepareScreen();
	    }
		
		
	}

	public void loadData(){
		   
		   if (dBHelperWhoHadTheLobster.isThereAnyRecord(order_id)) {
			   if(!dBHelperWhoHadTheLobster.isAnyOnePaid(order_id)){
				   dBHelperWhoHadTheLobster.deleteData(order_id);

			    	
		    		payers_paid=new boolean[no_of_payers];
		    		payers_name=new String[no_of_payers];
		    		payers_payment_due=new double[no_of_payers];
		    		payers_hilight=new boolean[no_of_payers];
	    			for(int i=0;i<no_of_payers;i++){
		    			  payers_paid[i]=false;
		    			  payers_name[i]="Payer "+(i+1);
		    			  payers_payment_due[i]=0;
		    			  payers_hilight[i]=false;
		    			  dBHelperWhoHadTheLobster.insertData(order_id, payers_name[i],false, payers_payment_due[i]);
		    		  }
			   }else{
				    no_of_payers=dBHelperWhoHadTheLobster.getNoPayers(order_id);
		    		payers_paid=new boolean[no_of_payers];
		    		payers_name=new String[no_of_payers];
		    		payers_payment_due=new double[no_of_payers];
		    		payers_hilight=new boolean[no_of_payers];
		    		
		    		ArrayList<PersonRowDS> array_list=dBHelperWhoHadTheLobster.getAllPersons(order_id);
		    		  for(int i=0;i<no_of_payers;i++){
		    			//  System.out.println(">>>>>>>>>>>>>>>>>>"+array_list.get(i).PAID  + array_list.get(i).PERSON_NAME + array_list.get(i).PAID_AMOUNT);
		    			  payers_paid[i]=array_list.get(i).PAID;
		    			  payers_name[i]=array_list.get(i).PERSON_NAME;
		    			  payers_payment_due[i]=array_list.get(i).PAID_AMOUNT;
		    			  payers_hilight[i]=false;
		    		  }
			   }
		   }else{
		    	payers_paid=new boolean[no_of_payers];
		    	payers_name=new String[no_of_payers];
		    	payers_payment_due=new double[no_of_payers];
		    	payers_hilight=new boolean[no_of_payers];
			    for(int i=0;i<no_of_payers;i++){
	    			  payers_paid[i]=false;
	    			  payers_name[i]="Payer "+(i+1);
	    			  payers_payment_due[i]=0;
	    			  payers_hilight[i]=false;
	    			  dBHelperWhoHadTheLobster.insertData(order_id, payers_name[i],false, payers_payment_due[i]);
	    		  }
			    
		   }

	}
	
	@SuppressLint("InflateParams")
	public void prepareScreen(){
		table_layout=(TableLayout)findViewById(R.id.tl_who_had_the_lobster);
		table_layout.removeAllViews();
		Button tempb = null;
		TextView temptv=null;

		for(int i=0;i<no_of_payers;i++ ) {
			
			final TableRow tr=(TableRow)LayoutInflater.from(WhoHadTheLobster.this).inflate(R.layout.payer_row_who_had_the_lobster, null);
			tempb = (Button)tr.findViewById(R.id.bt_payer_name_who_had_the_lobster);
			setUpButton(tempb,payers_name[i]);			
			temptv=(TextView)tr.findViewById(R.id.tv_payer_amount_who_had_the_lobster);
			temptv.setText(PaymentSettings.CURRENCY_SIGN+payers_payment_due[i]);

			if(payers_paid[i]==true){
				tempb.setEnabled(false);
				tempb.setBackgroundResource(R.drawable.button_flat_disable_grey);
			}
			
			tableRows.add(tr);
			
		
			table_layout.addView(tr);
			
		}
		
	}
	
	
private void setUpButton(final Button tempb,final String payer_name) {
		
		tempb.setText(payer_name);
		tempb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(billListView.getAdapter().getCount()==0){
					TextView temptv=null;
				    for(int i=0;i<tableRows.size();i++){
				    	
				    	Button tempb=(Button)tableRows.get(i).findViewById(R.id.bt_payer_name_who_had_the_lobster);
				    	if(tempb==((Button)v))
				    	{
				    		temptv=(TextView)tableRows.get(i).findViewById(R.id.tv_payer_amount_who_had_the_lobster);
				    	    break;
				    	}
				
				    }
				    
				    if(!dBHelperWhoHadTheLobster.isAnyOnePaid(order_id)){
				    	
				    	dBHelperWhoHadTheLobster.deleteData(order_id);
				    	for(int i=0;i<no_of_payers;i++){
				    		dBHelperWhoHadTheLobster.insertData(order_id, payers_name[i],false,payers_payment_due[i]);
				    	}
				    }
				    
					Bundle extradataBundle=new Bundle();
					extradataBundle.putInt("order_id", order_id);
					extradataBundle.putString("payer_name",((Button)v).getText().toString());
					extradataBundle.putDouble("totaldue", Double.parseDouble(temptv.getText().toString().replace("$","")) );
					extradataBundle.putString("who_is_paying","who_had_the_lobster");
					
					Bundle bundle=new Bundle();
					bundle.putBundle("databundle",extradataBundle);
			        Intent sd=new Intent(WhoHadTheLobster.this,TipCouponMultipayer.class);
			        sd.putExtras(bundle);
			        startActivity(sd);
				}else{
					 for(int i=0;i<no_of_payers;i++){
						 if(tempb.getText().toString().equals(payers_name[i])){
							 if(payers_hilight[i]==false){
								 tempb.setBackgroundResource(R.drawable.button_flat_disable_grey);
								 payers_hilight[i]=true;
							 }else{
								 tempb.setBackgroundResource(R.drawable.button_flat_green_purple);
								 payers_hilight[i]=false;
							 }
						 }
					 }
				}
				
			}
			
		});

		tempb.setOnDragListener(new View.OnDragListener() {
		      @SuppressLint("NewApi")
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

			    		for(int i=0;i<no_of_payers;i++){
				    		 if(tempb.getText().toString().equals(payers_name[i]))
				    		    {
//				    			  payers_payment_due[i]+=drop_item_amount;
//				    			  TextView tv_amt=(TextView)tableRows.get(i).findViewById(R.id.tv_payer_amount_who_had_the_lobster);
//				    			  tv_amt.setText(PaymentSettings.CURRENCY_SIGN+payers_payment_due[i]);
//				    			  adapter.remove(adapter.getItem(drop_item_remove_index));
//				    			  adapter.notifyDataSetChanged();
				    			 
				    			  payers_payment_due[i]+=removeItem(drop_item_remove_index);
				    			  payers_payment_due[i]=(double) Math.round(payers_payment_due[i] * 100) / 100;
				    			  TextView tv_amt=(TextView)tableRows.get(i).findViewById(R.id.tv_payer_amount_who_had_the_lobster);
				    			  tv_amt.setText(PaymentSettings.CURRENCY_SIGN+payers_payment_due[i]); 
				    			  
				    			  final Button tempBGButton = (Button) tableRows.get(i).findViewById(R.id.bt_payer_name_who_had_the_lobster);
				    			  final Drawable buttonBackground = tempBGButton.getBackground();
				    			 	tempBGButton.setBackgroundResource(R.drawable.button_flat_red_normal);
				    			  final Handler handler = new Handler(); 
				    			  handler.postDelayed(new Runnable() { 
				    				    @Override 
				    				    public void run() { 
				    				        // Do something after 5s = 5000ms 
				    				    	tempBGButton.setBackgroundDrawable(buttonBackground);
				    				    } 
				    				}, 200); 
				    			  
				    			  
				    			  if(adapter.getCount()==0)
				    				  setAllButtonsBackgroundToDefault();
				    			  
				    			  MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.ding);
				    			  mp.start();
				    			  
				    			  break;
				    		    };
				    	 }
		        	//	Toast.makeText(WhoHadTheLobster.this,"Finaly Droped", Toast.LENGTH_LONG).show();
		            	
		                return(true);
		            	}
		            case DragEvent.ACTION_DRAG_ENDED:
		            	
		            	break;
		            default:
		            	break;
		            }
		      return true;
		      }});
		
		
    }
	
	public double removeItem(int index){
		
		int units=adapter.getItem(index).units;
		double amount=adapter.getItem(index).price_per_unit;
		if(units==1){
			final Handler handler = new Handler(); 
			  handler.postDelayed(new Runnable() { 
				    @Override 
				    public void run() { 
				        // Do something after 5s = 5000ms 
				    	rl_tempFoodRow.setBackgroundColor(new Color().parseColor("#00bcd4"));
				    	adapter.remove(adapter.getItem(drop_item_remove_index));
				    } 
				}, 100); 
			
			
		}else{
			adapter.getItem(index).units-=1;
			adapter.getItem(index).total_price-=adapter.getItem(index).price_per_unit;
		}
				
		final Handler handler = new Handler(); 
		  handler.postDelayed(new Runnable() { 
			    @Override 
			    public void run() { 
			        // Do something after 5s = 5000ms 
			    	rl_tempFoodRow.setBackgroundColor(new Color().parseColor("#00bcd4"));
			    	adapter.notifyDataSetChanged();
			    } 
			}, 100); 
		
		return amount;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.who_had_the_lobster, menu);
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
	
	
	public void setAllButtonsBackgroundToDefault(){
		Button tempb;
		for(int i=0;i<tableRows.size();i++){
			tempb=(Button) tableRows.get(i).findViewById(R.id.bt_payer_name_who_had_the_lobster);
			tempb.setBackgroundResource(R.drawable.button_flat_green_purple);
		}
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
			
			LayoutInflater inflater = getLayoutInflater();
			final View row = inflater.inflate(layoutResourceId, parent, false);
			
			final int tempposition=position;
			row.setOnTouchListener(new View.OnTouchListener() {
				
				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					
		    		rl_tempFoodRow=(RelativeLayout) v.findViewById(R.id.rl_food_items_row);
					rlBackground=rl_tempFoodRow.getBackground();
					
					 drop_item_remove_index=tempposition;
					 boolean dragornot=true;
			    	  for(int i=0;i<no_of_payers;i++){
			    		 if(payers_hilight[i]==true)
			    		 {
			    			 dragornot=false;
			    			 break;
			    		 }
			    	 }
			    	
			    	if(dragornot){
				        tempView=row;
						return myView.onTouchEvent(event);
				        
			    	}else{

						rl_tempFoodRow.setBackgroundColor(Color.GREEN);
						
	    			 	double amountdue=removeItem(tempposition);
 			    		int cnt=0;
			    		for(int i=0;i<no_of_payers;i++){
				    		 if(payers_hilight[i]==true)
				    		    cnt++;
				    	 }
			    		for(int i=0;i<no_of_payers;i++){
			    		 if(payers_hilight[i]==true)
			    		    {
			    			  payers_payment_due[i]+=amountdue/cnt;
			    			  payers_payment_due[i]=(double) Math.round(payers_payment_due[i] * 100) / 100;
			    			  TextView tv_amt=(TextView)tableRows.get(i).findViewById(R.id.tv_payer_amount_who_had_the_lobster);
			    			  tv_amt.setText(PaymentSettings.CURRENCY_SIGN+payers_payment_due[i]);

			    		    }
			    	     }
		    			if(adapter.getCount()==0)
	    				  setAllButtonsBackgroundToDefault();
			    		
			    		

			    		return false;
			    	}
			    	  
					
				}
			});
			
			
			
			TextView tv_qty = (TextView)row.findViewById(R.id.tv_item_qty);
			TextView tv_name = (TextView)row.findViewById(R.id.tv_item_name);

			TextView tv_amount = (TextView)row.findViewById(R.id.tv_item_rate);
			
			tv_qty.setText(""+items.get(position).units);
			tv_name.setText(items.get(position).item_name);
			tv_amount.setText(PaymentSettings.CURRENCY_SIGN+items.get(position).total_price);

			return row;
			
		}

	}

	////////////////////////////////////////////////////////////////////////
	
	
	public class MyView extends View {
		 
		GestureDetector gestureDetector;
		 
		public MyView(Context context) {
		    super(context);
		            // creating new gesture detector 
		    gestureDetector = new GestureDetector(context, new GestureListener());
		} 
		 
		// skipping measure calculation and drawing 
		 
		    @SuppressLint("ClickableViewAccessibility")
		// delegate the event to the gesture detector 
		@Override 
		public boolean onTouchEvent(MotionEvent e) {
		    return gestureDetector.onTouchEvent(e);
		} 
		 
		 
		private class GestureListener extends GestureDetector.SimpleOnGestureListener{
			
			@Override
			public boolean onDown(MotionEvent e) {
				// TODO Auto-generated method stub
				return true;
			}
			@Override
			public boolean onDoubleTap(MotionEvent e) {
				// TODO Auto-generated method stub
				Log.v("My Log" ,"Double Tap");
				rl_tempFoodRow.setBackgroundColor(Color.GREEN);
		       double amountdue=removeItem(drop_item_remove_index)/no_of_payers;
	    		for(int i=0;i<no_of_payers;i++){
	    			  payers_payment_due[i]+=amountdue;
	    			  payers_payment_due[i]=(double) Math.round(payers_payment_due[i] * 100) / 100;
	    			  TextView tv_amt=(TextView)tableRows.get(i).findViewById(R.id.tv_payer_amount_who_had_the_lobster);
	    			  tv_amt.setText(PaymentSettings.CURRENCY_SIGN+payers_payment_due[i]);
	    			  
	    	     }
				
				
				return false;//super.onDoubleTap(e);
			}
			
			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				// TODO Auto-generated method stub
				Log.v("My Log","Single Tap");
				return true;
			}
			
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO Auto-generated method stub
				
				Log.v("My Log>>>>>>>>>>>>>>>>>>>>>>>>","Single Tap up");
				
				return false;
			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
					float distanceY) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub

			}
			
			
			@Override
			public void onShowPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
				ClipData data = ClipData.newPlainText("DRAG", "");
		        View.DragShadowBuilder shadow = new View.DragShadowBuilder(tempView);
		        TextView tv_itemQyantity=(TextView)shadow.getView().findViewById(R.id.tv_item_qty);
		         String itemQTYText= tv_itemQyantity.getText().toString();
	
		         TextView tv_amount=(TextView)shadow.getView().findViewById(R.id.tv_item_rate);
		         String amountText=tv_amount.getText().toString();
		         double amount=Double.parseDouble(amountText.substring(1+amountText.indexOf(PaymentSettings.CURRENCY_SIGN))) / Double.parseDouble(itemQTYText)  ;
		         amount=(double) Math.round(amount * 100) / 100;
		         tv_amount.setText(PaymentSettings.CURRENCY_SIGN+amount);
		         tv_itemQyantity.setText("1");
				 Log.v("My Log","Drag Started here");
				 tempView.startDrag(data, shadow, null, 0);
				 
				 tv_itemQyantity.setText(itemQTYText);
				 tv_amount.setText(amountText);
				
						 
				 
				 
			}
		}
 
	} 

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		System.out.println("Here is result");
	}
	
	/////////////Main Activity Ends
	
	
}
