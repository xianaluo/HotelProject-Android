package multipayerpaymentscreens;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONException;

import payments.PaymentHandler;
import payments.PaymentSettings;

import com.example.hotelproject.R;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.paypal.android.sdk.payments.ShippingAddress;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class PaymentOptionMultiPayer extends ActionBarActivity {

	private static final String TAG = "paymentExample";
    /**
     * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
     * 
     * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test credentials
     * from https://developer.paypal.com
     * 
     * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
     * without communicating to PayPal's servers.
     */
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "credential from developer.paypal.com";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Hotel Payment")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

	
	PaymentHandler paymentHandler;
	Bundle extraBundle;
	ImageView iv_creditdebit,iv_cash,iv_paypal,iv_mobilepay,iv_giftcard,iv_blankpay;
	double totaldue;
	
	private void LoadData(){   
		   extraBundle=getIntent().getBundleExtra("databundle");
		   totaldue=extraBundle.getDouble("totaldue");
		   paymentHandler=new PaymentHandler();
			iv_creditdebit=(ImageView)findViewById(R.id.iv_mp_creditdebit);
			iv_paypal=(ImageView)findViewById(R.id.iv_mp_paypal);
			iv_cash=(ImageView)findViewById(R.id.iv_mp_cash);
			iv_mobilepay=(ImageView)findViewById(R.id.iv_mp_mobilepay);
			iv_giftcard=(ImageView)findViewById(R.id.iv_mp_giftcard);
			iv_blankpay=(ImageView)findViewById(R.id.iv_mp_blankpay);
		   
	     //  Toast.makeText(this,"Payment Option : "+extraBundle.getDouble("totaldue"), Toast.LENGTH_LONG).show();
	   }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mp_payment_option);
		
		LoadData();
		
		iv_creditdebit.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {
	        	
	            	Bundle bundle = new Bundle();
	            	extraBundle.putString("payment_type",PaymentSettings.CREDIT_DEBIT_CARD_PAYMENT);
	        		bundle.putBundle("databundle",extraBundle);
	        		final Intent myIntent = new Intent(PaymentOptionMultiPayer.this,CardSwipeScreenMultiPayer.class);
	        		myIntent.putExtras(bundle);
					startActivity( myIntent);
	        }
	    });
		iv_paypal.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {
	        	onBuyPressed();
//	        	if(paymentHandler.getPaymentByPayPal(totaldue)){
//	            	Bundle bundle = new Bundle();
//	            	extraBundle.putString("payment_type",PaymentSettings.PAYPAL_PAYMENT);
//	        		bundle.putBundle("databundle",extraBundle);
//	        		final Intent myIntent = new Intent(PaymentOptionMultiPayer.this,SignatureScreenMultiPayer.class);
//	        		myIntent.putExtras(bundle);
//					startActivity( myIntent);
//	        	}else{
//                    showAlertDialogBox();
//	        	}
	        }
	    });		
		iv_cash.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {
	            	Bundle bundle = new Bundle();
	            	extraBundle.putString("payment_type",PaymentSettings.CASH_PAYMENT);
	        		bundle.putBundle("databundle",extraBundle);
	        		final Intent myIntent = new Intent(PaymentOptionMultiPayer.this,CashPaymentScreenMultipayer.class);
	        		myIntent.putExtras(bundle);
					startActivity( myIntent);

	        }
	    });
		iv_mobilepay.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {

	        	if(paymentHandler.getPaymentByMobilePay(totaldue)){
	            	Bundle bundle = new Bundle();
	            	extraBundle.putString("payment_type",PaymentSettings.MOBILE_PAY_PAYMENT);
	        		bundle.putBundle("databundle",extraBundle);
	        		final Intent myIntent = new Intent(PaymentOptionMultiPayer.this,SignatureScreenMultiPayer.class);
	        		myIntent.putExtras(bundle);
					startActivity( myIntent);
	        	}else{
                    showAlertDialogBox();
	        	}
	        }
	    });
		iv_giftcard.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {

	        	if(paymentHandler.getPaymentByGiftCard(totaldue)){
	            	Bundle bundle = new Bundle();
	            	extraBundle.putString("payment_type",PaymentSettings.GIFT_CARD_PAYMENT);
	        		bundle.putBundle("databundle",extraBundle);
	        		final Intent myIntent = new Intent(PaymentOptionMultiPayer.this,SignatureScreenMultiPayer.class);
	        		myIntent.putExtras(bundle);
					startActivity( myIntent);
	        	}else{
                    showAlertDialogBox();
	        	}
	        }
	    });
		iv_blankpay.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {
	        	if(paymentHandler.getPaymentByBlank(totaldue)){
	            	Bundle bundle = new Bundle();
	            	extraBundle.putString("payment_type",PaymentSettings.BLANK_PAYMENT);
	        		bundle.putBundle("databundle",extraBundle);
	        		final Intent myIntent = new Intent(PaymentOptionMultiPayer.this,SignatureScreenMultiPayer.class);
	        		myIntent.putExtras(bundle);
					startActivity( myIntent);
	        	}else{
                    showAlertDialogBox();
	        	}
	        }
	    });
		
		
		 Intent intent = new Intent(this, PayPalService.class);
	        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
	        startService(intent);
			
	}

	
	private void showAlertDialogBox(){
		final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PaymentOptionMultiPayer.this);
     alertDialog.setTitle("Payment Failure");
     alertDialog.setMessage("Payment Failed please go for other option or retry...!");
     alertDialog.setIcon(R.drawable.error);
     alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int which) {
         }
     	});
     alertDialog.show();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.payment_option_multi_payer, menu);
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
	
	/////////////////////////////////////////////////////////////////////////////
	
	
	
	 public void onBuyPressed() {
	        /* 
	         * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
	         * Change PAYMENT_INTENT_SALE to 
	         *   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
	         *   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
	         *     later via calls from your server.
	         * 
	         * Also, to include additional payment details and an item list, see getStuffToBuy() below.
	         */
	        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

	        /*
	         * See getStuffToBuy(..) for examples of some available payment options.
	         */

	        Intent intent = new Intent(PaymentOptionMultiPayer.this, PaymentActivity.class);

	        // send the same configuration for restart resiliency
	        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

	        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

	        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
	    }
	    
	    private PayPalPayment getThingToBuy(String paymentIntent) {
	    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>getThingToBuy");
	        return new PayPalPayment(new BigDecimal(""+totaldue), "USD", "Hotel Bill Payment",
	                paymentIntent);
	    }
	    
	    /* 
	     * This method shows use of optional payment details and item list.
	     */
	    private PayPalPayment getStuffToBuy(String paymentIntent) {
	        //--- include an item list, payment amount details
	        PayPalItem[] items =
	            {
	                    new PayPalItem("old jeans with holes", 2, new BigDecimal("87.50"), "USD",
	                            "sku-12345678"),
	                    new PayPalItem("free rainbow patch", 1, new BigDecimal("0.00"),
	                            "USD", "sku-zero-price"),
	                    new PayPalItem("long sleeve plaid shirt (no mustache included)", 6, new BigDecimal("37.99"),
	                            "USD", "sku-33333") 
	            };
	        BigDecimal subtotal = PayPalItem.getItemTotal(items);
	        BigDecimal shipping = new BigDecimal("7.21");
	        BigDecimal tax = new BigDecimal("4.67");
	        PayPalPaymentDetails paymentDetails = new PayPalPaymentDetails(shipping, subtotal, tax);
	        BigDecimal amount = subtotal.add(shipping).add(tax);
	        PayPalPayment payment = new PayPalPayment(amount, "USD", "hipster jeans", paymentIntent);
	        payment.items(items).paymentDetails(paymentDetails);

	        //--- set other optional fields like invoice_number, custom field, and soft_descriptor
	        payment.custom("This is text that will be associated with the payment that the app can use.");

	        return payment;
	    }
	    
	    /*
	     * Add app-provided shipping address to payment
	     */
	    private void addAppProvidedShippingAddress(PayPalPayment paypalPayment) {
	        ShippingAddress shippingAddress =
	                new ShippingAddress().recipientName("Mom Parker").line1("52 North Main St.")
	                        .city("Austin").state("TX").postalCode("78729").countryCode("US");
	        paypalPayment.providedShippingAddress(shippingAddress);
	    }
	    
	    /*
	     * Enable retrieval of shipping addresses from buyer's PayPal account
	     */
	    private void enableShippingAddressRetrieval(PayPalPayment paypalPayment, boolean enable) {
	        paypalPayment.enablePayPalShippingAddressesRetrieval(enable);
	    }

	    public void onFuturePaymentPressed(View pressed) {
	        Intent intent = new Intent(PaymentOptionMultiPayer.this, PayPalFuturePaymentActivity.class);

	        // send the same configuration for restart resiliency
	        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

	        startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
	    }

	    public void onProfileSharingPressed(View pressed) {
	        Intent intent = new Intent(PaymentOptionMultiPayer.this, PayPalProfileSharingActivity.class);

	        // send the same configuration for restart resiliency
	        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

	        intent.putExtra(PayPalProfileSharingActivity.EXTRA_REQUESTED_SCOPES, getOauthScopes());

	        startActivityForResult(intent, REQUEST_CODE_PROFILE_SHARING);
	    }

	    private PayPalOAuthScopes getOauthScopes() {
	        /* create the set of required scopes
	         * Note: see https://developer.paypal.com/docs/integration/direct/identity/attributes/ for mapping between the
	         * attributes you select for this app in the PayPal developer portal and the scopes required here.
	         */
	        Set<String> scopes = new HashSet<String>(
	                Arrays.asList(PayPalOAuthScopes.PAYPAL_SCOPE_EMAIL, PayPalOAuthScopes.PAYPAL_SCOPE_ADDRESS) );
	        return new PayPalOAuthScopes(scopes);
	    }

	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (requestCode == REQUEST_CODE_PAYMENT) {
	            if (resultCode == Activity.RESULT_OK) {
	                PaymentConfirmation confirm =
	                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
	                if (confirm != null) {
	                    try {
	                        Log.i(TAG, confirm.toJSONObject().toString(4));
	                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
	                        /**
	                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
	                         * or consent completion.
	                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
	                         * for more details.
	                         *
	                         * For sample mobile backend interactions, see
	                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
	                         */
	                        Toast.makeText(
	                                getApplicationContext(),
	                                "PaymentConfirmation info received from PayPal", Toast.LENGTH_LONG)
	                                .show();

	                    } catch (JSONException e) {
	                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
	                    }
	                }
	            } else if (resultCode == Activity.RESULT_CANCELED) {
	                Log.i(TAG, "The user canceled.");
	            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
	                Log.i(
	                        TAG,
	                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
	            }
	        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
	            if (resultCode == Activity.RESULT_OK) {
	                PayPalAuthorization auth =
	                        data.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
	                if (auth != null) {
	                    try {
	                        Log.i("FuturePaymentExample", auth.toJSONObject().toString(4));

	                        String authorization_code = auth.getAuthorizationCode();
	                        Log.i("FuturePaymentExample", authorization_code);

	                        sendAuthorizationToServer(auth);
	                        Toast.makeText(
	                                getApplicationContext(),
	                                "Future Payment code received from PayPal", Toast.LENGTH_LONG)
	                                .show();

	                    } catch (JSONException e) {
	                        Log.e("FuturePaymentExample", "an extremely unlikely failure occurred: ", e);
	                    }
	                }
	            } else if (resultCode == Activity.RESULT_CANCELED) {
	                Log.i("FuturePaymentExample", "The user canceled.");
	            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
	                Log.i(
	                        "FuturePaymentExample",
	                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
	            } 
	        } else if (requestCode == REQUEST_CODE_PROFILE_SHARING) {
	            if (resultCode == Activity.RESULT_OK) {
	                PayPalAuthorization auth =
	                        data.getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
	                if (auth != null) {
	                    try {
	                        Log.i("ProfileSharingExample", auth.toJSONObject().toString(4));

	                        String authorization_code = auth.getAuthorizationCode();
	                        Log.i("ProfileSharingExample", authorization_code);

	                        sendAuthorizationToServer(auth);
	                        Toast.makeText(
	                                getApplicationContext(),
	                                "Profile Sharing code received from PayPal", Toast.LENGTH_LONG)
	                                .show();

	                    } catch (JSONException e) {
	                        Log.e("ProfileSharingExample", "an extremely unlikely failure occurred: ", e);
	                    }
	                }
	            } else if (resultCode == Activity.RESULT_CANCELED) {
	                Log.i("ProfileSharingExample", "The user canceled.");
	            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
	                Log.i(
	                        "ProfileSharingExample",
	                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
	            }
	        }
	    }

	    private void sendAuthorizationToServer(PayPalAuthorization authorization) {

	        /**
	         * TODO: Send the authorization response to your server, where it can
	         * exchange the authorization code for OAuth access and refresh tokens.
	         * 
	         * Your server must then store these tokens, so that your server code
	         * can execute payments for this user in the future.
	         * 
	         * A more complete example that includes the required app-server to
	         * PayPal-server integration is available from
	         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
	         */

	    }

	    public void onFuturePaymentPurchasePressed(View pressed) {
	        // Get the Client Metadata ID from the SDK
	        String metadataId = PayPalConfiguration.getClientMetadataId(this);

	        Log.i("FuturePaymentExample", "Client Metadata ID: " + metadataId);

	        // TODO: Send metadataId and transaction details to your server for processing with
	        // PayPal...
	        Toast.makeText(
	                getApplicationContext(), "Client Metadata Id received from SDK", Toast.LENGTH_LONG)
	                .show();
	    }

	    @Override
	    public void onDestroy() {
	        // Stop service when done
	        stopService(new Intent(this, PayPalService.class));
	        super.onDestroy();
	    }
	
	
	
	//////////////////////////////////////////////////////////////////////////
	
}
