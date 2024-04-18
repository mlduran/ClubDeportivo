/*
* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
* Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
*/
package mld.clubdeportivo.utilidades;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *
 * @author MiguelLopezDuran
 */
public class UTF8Filter implements Filter {
    
    private String encoding;
            /**
            * Recogemos el tipo de codificación definido en el web.xml
            * Si no se hubiera especificado ninguno se toma "UTF-8" por defecto
            */
    public void init( FilterConfig filterConfig ) throws ServletException {
            encoding = filterConfig.getInitParameter( "requestEncoding" );
            // Forazamos el encodig a ISO o UTF-8 en lugar de 
             if( encoding == null ) {
                //encoding = "ISO-8859-1";
                encoding = "UTF-8";            
             }
    }
    /**
    * Metemos en la request el formato de codificacion UTF-8
    */
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain fc )
          throws IOException, ServletException {
                request.setCharacterEncoding( encoding );
                fc.doFilter( request, response );
    }
    public void destroy() {}
}
    

