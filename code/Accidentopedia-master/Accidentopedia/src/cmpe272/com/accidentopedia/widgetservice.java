package cmpe272.com.accidentopedia;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Intent;

public class  widgetservice extends IntentService {
	public static String ACTION_WIDGET_TITLE_CLICK = "Action_widget_title_click";  
    public static String ACTION_WIDGET_CONTENT_CLICK = "Action_widget_conent_click";
	public widgetservice(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		
	}

}
