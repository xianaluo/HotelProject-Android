package databaseutil;


import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelperLetsGoDutch extends SQLiteOpenHelper {

	   public static final String DATABASE_NAME = "MyDBName.db";
	   public static final String WHO_HAD_THE_LOBSTER_TABLENAME = "WhoHadTheLobster";
	   public static final String LETS_GO_DUTCH_TABLE_NAME = "LetsGoDutch";
	   public static final String ORDER_ID = "order_id";
	   public static final String PERSON_NAME = "person_name";
	   public static final String PAID = "paid";
	   public static final String PAID_AMOUNT = "paid_amount";
	   public static final String PAYMENT_TYPE="payment_type";
	   public static final String CASH_DUE_BACK="cash_due_back";
	   public static final String SIGNATURE="signature";
	   public static final String EMAIL_ID="email_id";


	   public DBHelperLetsGoDutch(Context context)
	   {
	      super(context, DATABASE_NAME , null, 1);
	   }

	   @Override
	   public void onCreate(SQLiteDatabase db) {
	      // TODO Auto-generated method stub
	      db.execSQL(
	      "create table LetsGoDutch " +
	      "(order_id integer, person_name text,paid boolean,paid_amount real,payment_type text,cash_due_back real,signature text,email_id text)"
	      );
	      db.execSQL(
	      "create table WhoHadTheLobster " +
	      "(order_id integer, person_name text,paid boolean,paid_amount real,payment_type text,cash_due_back real,signature text,email_id text)"
	      );
	      
	   }

	   @Override
	   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	      // TODO Auto-generated method stub
		  db.execSQL("DROP TABLE IF EXISTS "+WHO_HAD_THE_LOBSTER_TABLENAME);
	      db.execSQL("DROP TABLE IF EXISTS "+LETS_GO_DUTCH_TABLE_NAME);
	      onCreate(db);
	   }

	   public boolean insertData (int order_id, String person_name, boolean paid, double paid_amount)
	   {
	      SQLiteDatabase db = this.getWritableDatabase();
	      ContentValues contentValues = new ContentValues();

	      contentValues.put(ORDER_ID, order_id);
	      contentValues.put(PERSON_NAME, person_name);
	      contentValues.put(PAID, paid);
	      contentValues.put(PAID_AMOUNT,paid_amount);
	      contentValues.put(PAYMENT_TYPE, "cash");
	      contentValues.put(CASH_DUE_BACK,0);
	      contentValues.put(SIGNATURE,"");
	      contentValues.put(EMAIL_ID,"");

	      db.insert(LETS_GO_DUTCH_TABLE_NAME, null, contentValues);
	      return true;
	   }
	   
	   public boolean insertDataFull (int order_id, String person_name, boolean paid, double paid_amount,String payment_type,double cash_due_back,String signature,String email_id)
	   {
	      SQLiteDatabase db = this.getWritableDatabase();
	      ContentValues contentValues = new ContentValues();

	      contentValues.put(ORDER_ID, order_id);
	      contentValues.put(PERSON_NAME, person_name);
	      contentValues.put(PAID, paid);
	      contentValues.put(PAID_AMOUNT,paid_amount);
	      contentValues.put(PAYMENT_TYPE, payment_type);
	      contentValues.put(CASH_DUE_BACK,cash_due_back);
	      contentValues.put(SIGNATURE,signature);
	      contentValues.put(EMAIL_ID,email_id);

	      db.insert(LETS_GO_DUTCH_TABLE_NAME, null, contentValues);
	      return true;
	   }
	   
	   
	   public Cursor getData(int order_id){
	      SQLiteDatabase db = this.getReadableDatabase();
	      Cursor res =  db.rawQuery( "select * from "+LETS_GO_DUTCH_TABLE_NAME+" where id="+order_id+"", null );
	      return res;
	   }
	   public int numberOfRows(){
	      SQLiteDatabase db = this.getReadableDatabase();
	      int numRows = (int) DatabaseUtils.queryNumEntries(db, LETS_GO_DUTCH_TABLE_NAME);
	      return numRows;
	   }
	   
	   
	   public boolean updateData (Integer order_id, String person_name, boolean paid, double paid_amount,String payment_type,double cash_due_back,String signature,String email_id)
	   {
	      SQLiteDatabase db = this.getWritableDatabase();
	      ContentValues contentValues = new ContentValues();
	      

	      contentValues.put(PAID, paid);
	      contentValues.put(PAID_AMOUNT,paid_amount);
	      contentValues.put(PAYMENT_TYPE, payment_type);
	      contentValues.put(CASH_DUE_BACK, cash_due_back);
	      contentValues.put(SIGNATURE, signature);
	      contentValues.put(EMAIL_ID,email_id);
	      
	      db.update(LETS_GO_DUTCH_TABLE_NAME, contentValues, "order_id = ? AND person_name='"+ person_name +"'", new String[] { Integer.toString(order_id) } );
	      return true;
	   }


	   public Integer deleteData (Integer order_id)
	   {
	      SQLiteDatabase db = this.getWritableDatabase();
	      return db.delete(LETS_GO_DUTCH_TABLE_NAME, 
	      "order_id = ? ", 
	      new String[] { Integer.toString(order_id) });
	   }
	   
	   
	   public boolean isAnyOnePaid(int order_id)
	   {
	      SQLiteDatabase db = this.getReadableDatabase();
	      Cursor res =  db.rawQuery( "select * from "+LETS_GO_DUTCH_TABLE_NAME+" where order_id="+order_id+"", null );
	      res.moveToFirst();
	      boolean anyonepaid=false;
	      while(res.isAfterLast() == false){
	            if(res.getString(res.getColumnIndex(PAID)).trim().equals("1"))
	            {
	            	anyonepaid=true;
	            	break;
	            }
	            res.moveToNext();
	      }
	      
	      return anyonepaid;
	   }
	   
	   public boolean isAllPaid(int order_id){
		   SQLiteDatabase db = this.getReadableDatabase();
		      Cursor res =  db.rawQuery( "select * from "+LETS_GO_DUTCH_TABLE_NAME+" where order_id="+order_id+"", null );
		      res.moveToFirst();
		      boolean allpaid=true;
		      while(res.isAfterLast() == false){
		            if(res.getString(res.getColumnIndex(PAID)).trim().equals("0"))
		            {
		            	allpaid=false;
		            	break;
		            }
		            res.moveToNext();
		      }    
		      return allpaid;
	   }
	   
	   
	   public boolean isThereAnyRecord(int order_id){
		      SQLiteDatabase db = this.getReadableDatabase();
		      Cursor res =  db.rawQuery( "select * from "+LETS_GO_DUTCH_TABLE_NAME+" where order_id="+order_id+"", null );
		      if(res.getCount()==0)
		    	  return false;
		      else 
		    	  return true;
	   }
	   
	   public int getNoPayers(int order_id){
		      SQLiteDatabase db = this.getReadableDatabase();
		      Cursor res =  db.rawQuery( "select order_id from "+LETS_GO_DUTCH_TABLE_NAME+" where order_id="+order_id+"", null );

		    	  return res.getCount();

	   }
	   
	   
	   
	   public ArrayList<PersonRowDS> getAllPersons(int order_id)
	   {
	      ArrayList<PersonRowDS> array_list=new ArrayList<PersonRowDS>();
	      

	      SQLiteDatabase db = this.getReadableDatabase();
	      Cursor res =  db.rawQuery( "select * from "+LETS_GO_DUTCH_TABLE_NAME+" where order_id="+order_id+"", null );
	      res.moveToFirst();
	      while(res.isAfterLast() == false){
	    	  PersonRowDS row=new PersonRowDS();
	    	    row=new PersonRowDS();
	    	  	row.PERSON_NAME=res.getString(res.getColumnIndex(PERSON_NAME));
	    	  	
	    	  	if(res.getString(res.getColumnIndex(PAID)).trim().equals("1"))
	    	  		row.PAID=true;
	    	  	else
	    	  		row.PAID=false;
	    	  	
	    	  	row.PAID_AMOUNT=res.getDouble(res.getColumnIndex(PAID_AMOUNT));
	    	  	row.PAYMENT_TYPE=res.getString(res.getColumnIndex(PAYMENT_TYPE));
	    	  	row.CASH_DUE_BACK=res.getDouble(res.getColumnIndex(CASH_DUE_BACK));
	    	  	row.SIGNATURE=res.getString(res.getColumnIndex(SIGNATURE));
	    	  	row.EMAIL_ID=res.getString(res.getColumnIndex(EMAIL_ID));
	    	  array_list.add(row);
	         res.moveToNext();
	      }
	   return array_list;
	   }
	}