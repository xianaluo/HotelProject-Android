package emailutil;

import android.content.Context;
import android.content.Intent;

public class EmailHandler {
	Context context;
	public boolean emailsent=true;
	public EmailHandler(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
	}

	public void sendEmail(String[] emailAddresses,String subject,String message){
		emailsent=true;
		Intent emailIntent=new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailAddresses);
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,subject);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
		context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
		emailsent=false;
	}
}
