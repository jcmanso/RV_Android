package Oraciones;

import java.util.Calendar;

import mansoftware.rv.R;

import android.content.Context;

public class OracionPortugues extends Oracion {

	public OracionPortugues(Context con, Calendar cal) {
		super(con, cal);
	}
	
	public String getURL(){
		StringBuffer sb;
		int dia_semana;
		sb = new StringBuffer();
		dia_semana = fecha.get(Calendar.DAY_OF_WEEK) - 1;

		String url = context.getString(R.string.url);
		
		sb.append("http://dn");
		//Si es sabado o domingo lo pasamos a viernes
		if((dia_semana == 6)||(dia_semana==0)) dia_semana=1;
			sb.append(dia_semana);
		sb.append(url);

		sb.append(obtenerFecha());
		sb.append(".mp3");
		System.out.println("Direccion accedida " + sb.toString());
		return sb.toString();
	}
	
	
	
	

}
