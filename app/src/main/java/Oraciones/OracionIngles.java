package Oraciones;

import java.util.Calendar;

import android.content.Context;


public class OracionIngles extends Oracion {

	public OracionIngles(Context con, Calendar cal) {
		super(con, cal);
	}
	
	public String getURL(){
		StringBuffer sb;
		sb = new StringBuffer();

		String url = context.getString(mansoftware.rv.R.string.url);
		sb.append(url);
		if(finDeSemana()) sb.append("weekends/mp3/PAYG_20");
		else sb.append("mp3/PAYG_");
		sb.append(obtenerFecha());
		sb.append(".mp3");
		System.out.println("Direccion accedida " + sb.toString());
		return sb.toString();
	}
	
	private boolean finDeSemana(){
		int dia_semana = fecha.get(Calendar.DAY_OF_WEEK) - 1;
		if ((dia_semana==6) || (dia_semana==0)) return true;
		else return false;
	}
	
	
	
	

}
