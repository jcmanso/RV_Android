package es.mansoftware.rv;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class RvWidget extends AppWidgetProvider{

    final String TAG = "RvWidget";
    public static final String REFRESH_DATA_INTENT = "refresh";
    public static final String RETRIEVING = "Retrieving";
    public static final String STOPPED = "Stopped";
    public static final String PREPARING = "Preparing";
    public static final String PLAYING = "Playing";
    public static final String PAUSED = "Paused";
        
    @Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    	//Solo se inicia el reproductor cuando dan ok

		Intent presionarPlay = new Intent("es.mansoftware.rvapp.action.TOGGLE_PLAYBACK");
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, presionarPlay, 0);
		RemoteViews vista = new RemoteViews(context.getPackageName(), mansoftware.rv.R.layout.widget_play);
		vista.setOnClickPendingIntent(mansoftware.rv.R.id.boton, pendingIntent);	
		
		appWidgetManager.updateAppWidget(appWidgetIds, vista);
	}
	
	@Override
	public void onDisabled(Context context){
		Intent intent = new Intent(context, MusicService.class);
		context.stopService(intent);
	}
		
}
