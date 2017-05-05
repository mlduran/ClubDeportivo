package mld.clubdeportivo.utilidades;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Conjunto de funciones para realizar operaciones con fechas
 * 
 * @author Ivan Morales
 *
 */

public class CalendarFunctions 
{
	/**
	 * Transforma un string en un objeto GregorianCalendar
	 * 
	 * @param date String con la fecha en formato dd/MM/yyyy
	 * @return GregorianCalendar con la fecha del string
	 * @throws ParseException Si hay error en el parseo
	 */
    public static GregorianCalendar stringToCalendar(String date) throws ParseException
    {
    	SimpleDateFormat format = null;

    	format = new SimpleDateFormat("dd/MM/yyyy");
    	Date d = (Date)format.parse(date);

    	GregorianCalendar calendar = new GregorianCalendar();
    	calendar.setTime(d);
    	return calendar;
    }
    /**
     * Transforma un string (que contiene fecha y hora) en un objeto GregorianCalendar
     * 
     * @param dateTime String con la fecha en formato yyyy-MM-dd HH:mm
     * @return GregorianCalendar con la fecha del string
     * @throws ParseException Si hay un error en el parseo
     */
    public static GregorianCalendar stringTimeToCalendar(String dateTime) throws ParseException
    {
    	SimpleDateFormat format = null;

    	format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    	Date d = (Date)format.parse(dateTime);

    	GregorianCalendar calendar = new GregorianCalendar();
    	calendar.setTime(d);
    	return calendar;    	
    }
    /**
     * Transforma un objeto GregorianCalendar a un string
     * 
     * @param cal Fecha
     * @return String con la fecha en formato dd/MM/yyyy
     */
    public static String calendarToString(GregorianCalendar cal)
    {
        StringBuilder ret = new StringBuilder();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        if (day < 10) ret.append("0");
        ret.append(day);
        ret.append("/");
        if (month < 10) ret.append("0");
        ret.append(month);	    
        ret.append("/");			
        ret.append(year);
        return ret.toString();
    }
    /**
     * Transforma un objeto GregorianCalendar a un string (fecha + hora)
     * Utilizado para el nombre por defecto al exportar/compartir una quiniela
     * 
     * @param cal Fecha
     * @return String con la fecha en formato: yyyyMMdd_hhmm
     */
    public static String calendarToTimestamp(GregorianCalendar cal)
    {
        StringBuilder ret = new StringBuilder();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        ret.append(year);
        if (month < 10) ret.append("0");
        ret.append(month);
        if (day < 10) ret.append("0");
        ret.append(day);
        ret.append("_");
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        if (hour < 10) ret.append("0");
        ret.append(hour);
        if (min < 10) ret.append("0");
        ret.append(min);
        return ret.toString();
    }
    /**
     * Transforma un objeto GregorianCalendar a un string (fecha + hora)
     * 
     * @param cal Fecha
     * @return String con la fecha en formato yyyy-MM-dd hh:mm
     */
    public static String calendarToStringTime(GregorianCalendar cal)
    {
        StringBuilder ret = new StringBuilder();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        ret.append(year);
        ret.append("-");
        if (month < 10) ret.append("0");
        ret.append(month);
        ret.append("-");
        if (day < 10) ret.append("0");
        ret.append(day);
        ret.append(" ");
        if (hour < 10) ret.append("0");
        ret.append(hour);
        ret.append(":");
        if (min < 10) ret.append("0");
        ret.append(min);
        return ret.toString();
    }
    /**
     * Transforma un string en un objeto Hora
     * 
     * @param s String con la hora en formato hh:mm
     * @return Objeto Hora con la hora contenida en el string
     */
 
    /**
     * Devuelve un entero con la diferencia de d�as (fecha nueva - fecha antigua)
     * 
     * @param fechaAntigua Fecha antigua
     * @param fechaNueva Fecha nueva
     * @return Diferencia de d�as (Fecha nueva - fecha antigua)
     */
    public static int restarFechas(GregorianCalendar fechaAntigua,GregorianCalendar fechaNueva)
    {
        if (fechaAntigua.get(Calendar.YEAR) == fechaNueva.get(Calendar.YEAR)) {
            return fechaNueva.get(Calendar.DAY_OF_YEAR) - fechaAntigua.get(Calendar.DAY_OF_YEAR);
        } 
        else 
        {
            /* SI ESTAMOS EN DISTINTO ANYO COMPROBAMOS QUE EL A�O DEL DATEINI NO SEA BISIESTO
             * SI ES BISIESTO SON 366 DIAS EL A�O
             * SINO SON 365
             */
            int diasAnyo = fechaAntigua.isLeapYear(fechaAntigua.get(Calendar.YEAR)) ? 366 : 365;

            /* CALCULAMOS EL RANGO DE ANYOS */
            int rangoAnyos = fechaNueva.get(Calendar.YEAR) - fechaAntigua.get(Calendar.YEAR);

            /* CALCULAMOS EL RANGO DE DIAS QUE HAY */
            int rango = (rangoAnyos * diasAnyo) + (fechaNueva.get(Calendar.DAY_OF_YEAR) - fechaAntigua.get(Calendar.DAY_OF_YEAR));
            //Log.d("quinidroid","restarFechas - Diferencia de dias: " + rango);
            return rango;
        }
    }
    /**
     * Calcula la diferencia en minutos entre dos fechas (para el liveScore)
     * 
     * @param antiguo Fecha antigua
     * @param nuevo Fecha nueva
     * @return Diferencia en minutos entre las dos fechas o -1 si la fecha antigua es mayor que la nueva
     */
    public static int restarMinutos(GregorianCalendar antiguo, GregorianCalendar nuevo)
    {
    	int restarFechasResult = restarFechas(antiguo,nuevo);
   		int horaAntiguo = antiguo.get(Calendar.HOUR_OF_DAY);
   		int minAntiguo = antiguo.get(Calendar.MINUTE);
   		int horaNuevo = nuevo.get(Calendar.HOUR_OF_DAY);
   		int minNuevo = nuevo.get(Calendar.MINUTE);
   		
   		int minTotalAntiguo = horaAntiguo * 60 + minAntiguo;
   		int minTotalNuevo = horaNuevo * 60 + minNuevo;
    	
   		if (restarFechasResult >= 0)
   		{
   			minTotalNuevo = minTotalNuevo + (restarFechasResult*24*60); //Si restarFechas > 0 le sumo los minutos por dia    		
   			return minTotalNuevo - minTotalAntiguo;
   		}
   		else return -1;   	
    }
    /**
     * Calcula la diferencia en MiliSegundos entre dos fechas
     * 
     * @param antiguo Fecha antigua
     * @param nuevo Fecha nueva
     * @return Diferencia en milisegundos entre las dos fechas
     */
    public static long restarMiliSegundos(GregorianCalendar antiguo, GregorianCalendar nuevo)
    {
    	long timeMillis = nuevo.getTime().getTime() - antiguo.getTime().getTime();
    	return timeMillis;
    }
    /**
     * Calcula la diferencia en Segundos entre dos fechas
     * @param antiguo Fecha antigua
     * @param nuevo Fecha nueva
     * @return Diferencia en segundos entre las dos fechas
     */
    public static long restarSegundos(GregorianCalendar antiguo, GregorianCalendar nuevo)
    {
    	long timeMillis = nuevo.getTime().getTime() - antiguo.getTime().getTime();
    	return timeMillis / 1000;
    }
    
    
    public static GregorianCalendar stringTimestampToCalendar(int date) throws ParseException
    {

        Timestamp timeStamp = new Timestamp(date);
        Date d = new Date(timeStamp.getTime());
    
    	GregorianCalendar calendar = new GregorianCalendar();
    	calendar.setTime(d);
    	return calendar;
    }
}