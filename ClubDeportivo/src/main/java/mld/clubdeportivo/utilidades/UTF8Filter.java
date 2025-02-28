/*
* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
* Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
*/
package mld.clubdeportivo.utilidades;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;

/**
 *
 * @author MiguelLopezDuran
 */
public class UTF8Filter implements Filter {
    
    private String encoding;
            /**
            * Recogemos el tipo de codificación definido en el web.xml
            * Si no se hubiera especificado ninguno se toma "UTF-8" por defecto
     * @param filterConfig
     * @throws jakarta.servlet.ServletException
            */
    @Override
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
     * @param request
     * @param response
     * @param fc
     * @throws java.io.IOException
     * @throws jakarta.servlet.ServletException
    */
    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain fc )
          throws IOException, ServletException {
                request.setCharacterEncoding( encoding );
                fc.doFilter( request, response );
    }
    @Override
    public void destroy() {}
}
    

