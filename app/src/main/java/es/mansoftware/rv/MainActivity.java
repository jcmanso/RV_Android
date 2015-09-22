/*   
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.mansoftware.rv;

import es.mansoftware.rv.MusicService.MusicBinder;
import mansoftware.rv.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/** 
 * Main activity: shows media player buttons. This activity shows the media player buttons and
 * lets the user click them. No media handling is done here -- everything is done by passing
 * Intents to our {@link MusicService}.
 * */
public class MainActivity extends Activity implements OnClickListener {
    /**
     * The URL we suggest as default when adding by URL. This is just so that the user doesn't
     * have to find an URL to test this sample.
     */
    final String TAG = "MainActivity";
    public static final String REFRESH_DATA_INTENT = "refresh";
    public static final String RETRIEVING = "Retrieving";
    public static final String STOPPED = "Stopped";
    public static final String PREPARING = "Preparing";
    public static final String PLAYING = "Playing";
    public static final String PAUSED = "Paused";

    
 // indicates the state our service:
    enum State {
        Retrieving, // the MediaRetriever is retrieving music
        Stopped,    // media player is stopped and not prepared to play
        Preparing,  // media player is preparing...
        Playing,    // playback active (media player ready!). (but the media player may actually be
                    // paused in this state if we don't have audio focus. But we stay in this state
                    // so that we know we have to resume playback once we get focus back)
        Paused      // playback paused (media player ready!)
    };
    
    
    Button mPlayButton;
    //Button mPauseButton;
    Button mSkipButton;
    Button mRewindButton;
    Button mStopButton;
    TextView mFechaText;
    
    private DataUpdateReceiver dataUpdateReceiver;
    
    //JCM08082012: Se modifica para enganchar el servicio
    MusicService mService;
    boolean mBound = false;

    /**
     * Called when the activity is first created. Here, we simply set the event listeners and
     * start the background service ({@link MusicService}) that will handle the actual media
     * playback.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mPlayButton = (Button) findViewById(R.id.playbutton);
        //mPauseButton = (Button) findViewById(R.id.pausebutton);
        mSkipButton = (Button) findViewById(R.id.skipbutton);
        mRewindButton = (Button) findViewById(R.id.rewindbutton);
        mStopButton = (Button) findViewById(R.id.stopbutton);
        mFechaText = (TextView) findViewById(R.id.texto_fecha);

        mPlayButton.setOnClickListener(this);
        //mPauseButton.setOnClickListener(this);
        mSkipButton.setOnClickListener(this);
        mRewindButton.setOnClickListener(this);
        mStopButton.setOnClickListener(this);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);  
    }
    
    @Override
    protected void onResume(){
    	super.onResume();
    	if (dataUpdateReceiver == null) dataUpdateReceiver = new DataUpdateReceiver();
    	IntentFilter intentFilter = new IntentFilter(REFRESH_DATA_INTENT);
    	registerReceiver(dataUpdateReceiver, intentFilter);
    
    	//Si el servicio esta enganchado ponemos la fecha
    	if(mBound)
        	mFechaText.setText(mService.mSongTitle);

    }
    
    @Override
    protected void onPause(){
    	super.onPause();
    	if (dataUpdateReceiver != null) unregisterReceiver(dataUpdateReceiver);
    }

    public void onClick(View target) {
        // Send the correct intent to the MusicService, according to the button that was clicked
        if (target == mPlayButton){
            //startService(new Intent(MusicService.ACTION_PLAY));
        	Log.v(TAG, "Boton play pulsado");
            mService.processPlayRequest();
        }
        //else if (target == mPauseButton){
        //    startService(new Intent(MusicService.ACTION_PAUSE));
        //    Log.v(TAG, "Boton pause pulsado");
        //}
        else if (target == mSkipButton){
            //startService(new Intent(MusicService.ACTION_SKIP));
        	Log.v(TAG, "Boton skip pulsado");
            mService.processSkipRequest();
        	
        }
        else if (target == mRewindButton){
            //startService(new Intent(MusicService.ACTION_REWIND));
        	Log.v(TAG, "Boton rewind pulsado");
        	mService.processRewindRequest();
        	
        }
        else if (target == mStopButton){
            //startService(new Intent(MusicService.ACTION_STOP));
        	Log.v(TAG, "Boton stop pulsado");
        	mService.processStopRequest();
        	
        }
    }

    /** 
     * Shows an alert dialog where the user can input a URL. After showing the dialog, if the user
     * confirms, sends the appropriate intent to the {@link MusicService} to cause that URL to be
     * played.
     */
    /*
    void showUrlDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Manual Input");
        alertBuilder.setMessage("Enter a URL (must be http://)");
        final EditText input = new EditText(this);
        alertBuilder.setView(input);

        input.setText(SUGGESTED_URL);

        alertBuilder.setPositiveButton("Play!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int whichButton) {
                // Send an intent with the URL of the song to play. This is expected by
                // MusicService.
                Intent i = new Intent(MusicService.ACTION_URL);
                Uri uri = Uri.parse(input.getText().toString());
                i.setData(uri);
                startService(i);
            }
        });
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int whichButton) {}
        });

        alertBuilder.show();
    }
    */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
            case KeyEvent.KEYCODE_HEADSETHOOK:
                startService(new Intent(MusicService.ACTION_TOGGLE_PLAYBACK));
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        /*
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
        */
    }
    
    private void actualizaPantalla(){
    	String state = mService.getState();
    	mFechaText.setText(mService.mSongTitle);
    	//Comprueba estado del servicio
    	if(state.equals(RETRIEVING) || state.equals(PAUSED) || state.equals(STOPPED))
    		mPlayButton.setBackgroundResource(R.drawable.btn_play);
    	else if(state.equals(PREPARING))
    		mPlayButton.setBackgroundResource(R.drawable.btn_flechas);
    	else if(state.equals(PLAYING))
    		mPlayButton.setBackgroundResource(R.drawable.btn_pause);
    }
    
    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder service) {
			MusicBinder binder = (MusicBinder) service;
			mService = binder.getService();
			mBound = true;
			
		}

		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
    };
    
    private class DataUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(REFRESH_DATA_INTENT)) {
            	actualizaPantalla();
            }
        }
    }
}
