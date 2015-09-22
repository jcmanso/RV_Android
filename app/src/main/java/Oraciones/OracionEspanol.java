package Oraciones;

import java.util.Calendar;

import mansoftware.rv.R;

import android.content.Context;


public class OracionEspanol extends Oracion {

	public OracionEspanol(Context con, Calendar cal) {
		super(con, cal);
	}
	
	public String getURL(){
		StringBuffer sb;
		sb = new StringBuffer();

		String url = context.getString(R.string.url);
		sb.append(url);

		sb.append(obtenerFecha());
		sb.append(".mp3");
		System.out.println("Direccion accedida " + sb.toString());
		return sb.toString();
	}
	
	
	
	

}
