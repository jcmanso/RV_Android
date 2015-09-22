package Oraciones;

import java.util.Calendar;

import android.content.Context;

public abstract class Oracion{

	protected Calendar fecha;
	protected final Context context;
	
	/**
	 * 
	 * @param cal Fecha en la que se desea ver la oracion
	 */
	public Oracion(Context con, Calendar cal){
		context = con;
		fecha = cal;
	}
	
	public abstract String getURL();
	
	/**
	 * 
	 * @return aammdd (si es s�bado pasa al domingo)
	 */
	protected String obtenerFecha(){
		int dia_mes, dia_semana, mes, anno;
		StringBuffer sb, sb_aux;

		//Obtencion de la fecha calibrada
		dia_mes = fecha.get(Calendar.DATE);
		dia_semana = fecha.get(Calendar.DAY_OF_WEEK) - 1;
		mes = fecha.get(Calendar.MONTH) + 1;
		anno = fecha.get(Calendar.YEAR);
		
		//Si es sábado lo pasamos a domingo
		if(dia_semana == 6) dia_mes++;
	
		//Formaci�n de la cadena aammdd
		sb = new StringBuffer();
		//Formacion a�o
		sb_aux = new StringBuffer();
		sb_aux.append(anno);
		sb.append(sb_aux.substring(2, 4));
		//Formacion mes
		sb_aux = new StringBuffer();
		sb_aux.append(mes);
		if(sb_aux.length()==1) sb.append("0");
		sb.append(mes);
		//Formacion dia
		sb_aux = new StringBuffer();
		sb_aux.append(dia_mes);
		if(sb_aux.length()==1) sb.append("0");
		sb.append(dia_mes);
		
		return sb.toString();
		
	}

}
