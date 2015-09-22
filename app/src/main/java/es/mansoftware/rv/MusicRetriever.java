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

import Oraciones.Oracion;
import Oraciones.OracionEspanol;
import Oraciones.OracionIngles;
import Oraciones.OracionPolaco;
import Oraciones.OracionPortugues;
//import android.content.ContentResolver;
import android.annotation.SuppressLint;
import android.content.Context;
//import android.database.Cursor;
import android.net.Uri;
//import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;

//import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
//import java.util.List;
//import java.util.Random;

import mansoftware.rv.R;

/**
 * Retrieves and organizes media to play. Before being used, you must call {@link #prepare()},
 * which will retrieve all of the music on the user's device (by performing a query on a content
 * resolver). After that, it's ready to retrieve a random song, with its title and URI, upon
 * request.
 */
@SuppressLint("ParserError")
public class MusicRetriever {
    final String TAG = "MusicRetriever";

 // the items (songs) we have queried
    List<Item> mItems = new ArrayList<Item>();
    
    final Calendar fecha;
    final Context context;
    
    private int dia=0;
    Oracion oracion;

    public MusicRetriever(Calendar cal, Context con) {
        fecha = cal;
        context = con;
    }

    /**
     * Loads music data. This method may take long, so be sure to call it asynchronously without
     * blocking the main thread.
     */
    public void prepare() {
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        //Log.i(TAG, "Querying media...");
        //Log.i(TAG, "URI: " + uri.toString());
        

        /*
        // Perform a query on the content resolver. The URI we're passing specifies that we
        // want to query for all audio media on external storage (e.g. SD card)
        Cursor cur = mContentResolver.query(uri, null,
                MediaStore.Audio.Media.IS_MUSIC + " = 1", null, null);
        Log.i(TAG, "Query finished. " + (cur == null ? "Returned NULL." : "Returned a cursor."));

        if (cur == null) {
            // Query failed...
            Log.e(TAG, "Failed to retrieve music: cursor is null :-(");
            return;
        }
        if (!cur.moveToFirst()) {
            // Nothing to query. There is no music on the device. How boring.
            Log.e(TAG, "Failed to move cursor to first row (no query results).");
            return;
        }

        Log.i(TAG, "Listing...");

        // retrieve the indices of the columns where the ID, title, etc. of the song are
        int artistColumn = cur.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        int titleColumn = cur.getColumnIndex(MediaStore.Audio.Media.TITLE);
        int albumColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM);
        int durationColumn = cur.getColumnIndex(MediaStore.Audio.Media.DURATION);
        int idColumn = cur.getColumnIndex(MediaStore.Audio.Media._ID);

        Log.i(TAG, "Title column index: " + String.valueOf(titleColumn));
        Log.i(TAG, "ID column index: " + String.valueOf(titleColumn));

        // add each song to mItems
        do {
            Log.i(TAG, "ID: " + cur.getString(idColumn) + " Title: " + cur.getString(titleColumn));
            mItems.add(new Item(
                    cur.getLong(idColumn),
                    cur.getString(artistColumn),
                    cur.getString(titleColumn),
                    cur.getString(albumColumn),
                    cur.getLong(durationColumn)));
        } while (cur.moveToNext());

		*/
        Log.i(TAG, "Done querying media. MusicRetriever is ready.");
    }

    /*
    public ContentResolver getContentResolver() {
        return mContentResolver;
    }
    */

    
    /** Returns a random Item. If there are no items available, returns null. */
   /*
    public Item getRandomItem() {
        if (mItems.size() <= 0) return null;
        return mItems.get(mRandom.nextInt(mItems.size()));
    }
    */
    
    @SuppressLint("ParserError")
	public Item getItem() {
        oracion = getOracion(fecha);
        DateFormat formato = new DateFormat();
        CharSequence fechaFormateada = formato.format("EEEE, MMMM dd, yyyy", fecha);
        Item item = new Item(oracion.getURL(), fechaFormateada.toString());
        return item;
    }
    
    public Item getNextItem() {
    	//dia = dia + 1;
    	fecha.roll(Calendar.DAY_OF_YEAR, 1);
    	return getItem();
    }
    
    public Item getPreviousItem() {
    	//dia = dia - 1;
    	fecha.roll(Calendar.DAY_OF_YEAR, -1);
    	return getItem();
    }
    
    private Oracion getOracion(Calendar cal){
		System.out.println(dia);
    	Oracion ora;
		String idioma = context.getString(R.string.idioma);
		if (idioma.equals("Portugues"))
			ora= new OracionPortugues(context, cal);
		else if(idioma.equals("Ingles"))
			ora= new OracionIngles(context, cal);
		else if(idioma.equals("Polaco"))
			ora= new OracionPolaco(context, cal);
		else 
			ora= new OracionEspanol(context, cal);
				
		return ora;
	}

    public static class Item {
        String url;
        String title;

        public Item(String u, String t) {
            url = u;
            title = t;
        }
        
        public String getTitle() {
            return title;
        }

        /*
        public long getId() {
            return id;
        }

        public String getArtist() {
            return artist;
        }

        public String getTitle() {
            return title;
        }

        public String getAlbum() {
            return album;
        }

        public long getDuration() {
            return duration;
        }
        */

        public String getURL() {
            return url;
        }
    }
}
