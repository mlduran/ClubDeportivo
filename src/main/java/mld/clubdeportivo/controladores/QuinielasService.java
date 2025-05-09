package mld.clubdeportivo.controladores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;

//import org.htmlcleaner.CleanerProperties;
//import org.htmlcleaner.HtmlCleaner;
//import org.htmlcleaner.PrettyXmlSerializer;
//import org.htmlcleaner.TagNode;
//import org.xml.sax.SAXException;
//
//import com.kete.quinidroid.model.Jornada;
//import com.kete.quinidroid.net.WebDataHTML;
//import com.kete.quinidroid.sql.JornadasSQL;
//import com.kete.quinidroid.util.CalendarFunctions;
//import com.kete.quinidroid.xml.XmlDescargarJornadas;
import mld.clubdeportivo.controladores.AdminHttpServlet;
import mld.clubdeportivo.utilidades.CalendarFunctions;
import mld.clubdeportivo.utilidades.WebDataHTML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class QuinielasService
{
//	private String urlJornadasFuturas = "http://www.loteriasyapuestas.es/mod.sorteos/mem.sorteos/juego.LAQU/filtro_cf.futuro";
//	private String urlJornadasPasadas = "http://www.loteriasyapuestas.es/mod.sorteos/mem.sorteos/juego.LAQU/filtro_cf.pasado";
//	private String urlJornadasPasadasAntiguas = "http://www.loteriasyapuestas.es/mod.sorteos/mem.sorteos/juego.LAQU/filtro_cf.pasado/pagina.";
//	//Nombres base de los ficheros auxiliares
//	private String sFuturo;
//	private String sPasado;
//        private ServletContext appManager;
//	
//        private static Logger logger = LogManager.LoggerFactory.getLogger(QuinielasService.class);
//
//	protected void ejecutar(HttpServletRequest req, HttpServletResponse resp,
//            ServletContext appMan) 
//	{
//            appManager = appMan;
//             sFuturo = "jornadasFuturo";
//             sPasado = "jornadasPasado";
//            GregorianCalendar actual = new GregorianCalendar();
//            try
//            {
//                descargarJornadas(urlJornadasFuturas,sFuturo);
//                descargarJornadas(urlJornadasPasadas,sPasado);                
//            }		
//		catch (IOException e){logger.error(e.toString()); }
//		catch (SAXException e){logger.error(e.toString()); }
//		catch (ParserConfigurationException e){logger.error(e.toString());}
//		finally
//		{
//			GregorianCalendar finServicio = new GregorianCalendar();
//			logger.warn("Fin servicio. Tiempo: "+String.valueOf(CalendarFunctions.restarSegundos(actual, finServicio))+" s");
//		}
//	}
//        
//	private void descargarJornadas(String url, String auxFileName) throws IOException, SAXException, ParserConfigurationException
//	{
//            
//            String ruta = appManager.getInitParameter("rutaficheroscarga");
//            File fHtml = new File(auxFileName + ".html");
//            File fXml= new File(auxFileName+".xml");
//            // FUTURO
//            WebDataHTML webData=new WebDataHTML(url);
//            webData.downloadHtml(auxFileName+".html", , "UTF-8");
//		//Log.d("quinidroid", "ActualizarJornadasService - Html descargado");
//    	//src, dst
//		CleanerProperties props = new CleanerProperties();		 
//		// set some properties to non-default values
//		props.setTranslateSpecialEntities(true);
//		props.setTransResCharsToNCR(true);
//		props.setOmitComments(true);		 
//		// do parsing
//		TagNode tagNode = new HtmlCleaner(props).clean(fHtml);
//		// serialize to xml file
//		PrettyXmlSerializer pXML=new PrettyXmlSerializer(props);
//		pXML.writeToFile(tagNode, fXml.getAbsolutePath(), "UTF-8");
//		//Log.d("quinidroid","ActualizarJornadasService - Convertido fichero jornadas .html a .xml");
//    	XmlDescargarJornadas xml=new XmlDescargarJornadas();
//    	//Log.d("Quinidroid","ActualizarJornadasService - Parseando XML jornadas");
//    	xml.leerXML(fXml);
//    	//Log.d("Quinidroid","ActualizarJornadasService - XML jornadas  parseado");
//    	ArrayList<Jornada> listaJornadas = xml.getListaJornadas();
//    	if (listaJornadas!=null && listaJornadas.size()>0)
//    	{
//    		JornadasSQL sqlJornadas = new JornadasSQL(getApplicationContext());
//    		//Log.d("Quinidroid","ActualizarJornadasService - A�adiendo/Actualizando jornadas a BD SQL");
//    		sqlJornadas.addOrUpdateJornadas(listaJornadas);
//    	}
//    	if (fHtml.exists()) fHtml.delete();
//    	if (fXml.exists()) fXml.delete();
//	}
//	private boolean comprobarSiFaltanJornadasAntiguas()
//	{
//		try
//		{
//			JornadasSQL sqlJornadas = new JornadasSQL(getApplicationContext());
//			ArrayList<Jornada> listaJornadas = sqlJornadas.getListaJornadas();
//			//Ordenamos
//			Collections.sort(listaJornadas);
//			if (CalendarFunctions.restarFechas(CalendarFunctions.stringToCalendar("19/08/2012"), listaJornadas.get(0).getFecha()) > 0)
//			{				
//				int pag = 2;
//				do
//				{
//					Log.d("quinidroid","comprobarSiFaltanJornadasAntiguas = SI, Fecha �ltima jornada a�adida: "+CalendarFunctions.calendarToString(listaJornadas.get(0).getFecha()));
//					String url = urlJornadasPasadasAntiguas + pag;
//					descargarJornadas(url,sPasado);
//					sqlJornadas = new JornadasSQL(getApplicationContext());
//					listaJornadas.clear();
//					listaJornadas = sqlJornadas.getListaJornadas();
//					Collections.sort(listaJornadas);
//					pag++;
//				}
//				while (CalendarFunctions.restarFechas(CalendarFunctions.stringToCalendar("19/08/2012"), listaJornadas.get(0).getFecha()) > 0);
//				return true;
//			}
//			else return false;
//		}
//		catch (ParseException e){Log.e("quinidroid", "comprobarSiFaltanJornadasAntiguas ParseException "+e.toString());return false;}
//		catch (IOException e){Log.e("quinidroid","comprobarSiFaltanJornadasAntiguas - IOException - Tarea asincrona - "+e.toString());return false;}
//		catch (SAXException e){Log.e("quinidroid","comprobarSiFaltanJornadasAntiguas - SAXException - Tarea asincrona - "+e.toString());return false;}
//		catch (ParserConfigurationException e){Log.e("quinidroid","comprobarSiFaltanJornadasAntiguas - ParserConfigurationException - Tarea asincrona - "+e.toString());return false;}
//	}
	
}