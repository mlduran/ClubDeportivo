/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mld.clubdeportivo.utilidades;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.naming.Context;


public class WebDataHTML 
{
        String sUrl;
        String encoding;
        Context cxt;
        
        public WebDataHTML(String url)
        {
                sUrl=url;
                cxt=null;
                encoding=null;
        }
        public void downloadHtml(String name, Context context, String enc) throws MalformedURLException, IOException
        {
                try
                {
                        cxt=context;
                        if (cxt != null)
                        {
                            OutputStream outputStream = new FileOutputStream(name);
                            try (Writer writer = new OutputStreamWriter(outputStream)) {
                                encoding = enc;
                                writer.write(getHtml());
                            }
                        }
                }
                catch(MalformedURLException e){throw e;}
                catch(IOException  e1){throw e1;}
        }
        private String getHtml() throws MalformedURLException, IOException
        {
                var aux="";
                String nextLine;
                URL url;
                URLConnection urlConn;
                InputStreamReader  inStream;
                BufferedReader buff;
                try
                {
                        // Create the URL object that points
                    // at the default file index.html
                    url  = new URL(sUrl);
                    urlConn = url.openConnection();
                    //Set timeouts
                    urlConn.setConnectTimeout(45000);
                    urlConn.setReadTimeout(45000);
                    //TODO: Capturar excepciones de timeout
                    //ConnectException (dentro de SocketException (dentro de IOException))
                    inStream = new InputStreamReader(urlConn.getInputStream(),encoding);
                    buff= new BufferedReader(inStream);
                    // Read and print the lines from index.html
                    while (true)
                    {
                        nextLine =buff.readLine();
                        if (nextLine !=null)
                        {
                                nextLine=cleanEntities(nextLine);
                                aux=aux+nextLine;
                        }
                        else{break;}
                    }
                    return aux;
                }
                catch(MalformedURLException e)
                {
                        // Please check the URL
                    throw e;
                }
                catch(IOException  e1)
                {
                         // Can't read  from the Internet
                     throw e1;
                }               
        }
        private static String cleanEntities(String s)
        {
                // http://www.w3schools.com/tags/ref_entities.asp
                String entities[] = {
                                "&quot;", "&apos;","&lt;", "&gt;","&nbsp;","&iexcl;","&cent;",
                                "&pound;","&curren;","&yen;","&brvbar;","&sect;","&uml;","&copy;",
                                "&ordf;","&laquo;","&not;","&shy;","&reg;","&macr;","&deg;","&plusmn;",
                                "&sup2;","&sup3;","&acute;","&micro;","&para;","&middot;","&cedil;",
                                "&sup1;","&ordm;","&raquo;","&frac14;","&frac12;","&frac34;","&iquest;",
                                "&times;","&divide;","&Agrave;","&Aacute;","&Acirc;","&Atilde;",
                                "&Auml;","&Aring;","&AElig;","&Ccedil;","&Egrave;","&Eacute;",
                                "&Ecirc;","&Euml;","&Igrave;","&Iacute;","&Icirc;","&Iuml;","&ETH;",
                                "&Ntilde;","&Ograve;","&Oacute;","&Ocirc;","&Otilde;","&Ouml;",
                                "&Oslash;","&Ugrave;","&Uacute;","&Ucirc;","&Uuml;","&Yacute;",
                                "&THORN;","&szlig;","&agrave;","&aacute;","&acirc;","&atilde;","&auml;",
                                "&aring;","&aelig;","&ccedil;","&egrave;","&eacute;","&ecirc;",
                                "&euml;","&igrave;","&iacute;","&icirc;","&iuml;","&eth;","&ntilde;",
                                "&ograve;","&oacute;","&ocirc;","&otilde;","&ouml;","&oslash;",
                                "&ugrave;",     "&uacute;","&ucirc;","&uuml;","&yacute;","&thorn;",
                                "&yuml;","&ldquo;","&rdquo;","&#225",
                };
                String characters[] = {
                                "\"","'","<",">"," ","¡","¢","£","¤","¥","¦","§","¨","©","ª","«","¬",
                                " ","®","¯","°","±","²","³","´","µ","¶","·","¸","¹","º","»","¼","½","¾",
                                "¿","×","÷","À","Á","Â","Ã","Ä","Å","Æ","Ç","È","É","Ê","Ë","Ì","Í","Î",
                                "Ï","Ð","Ñ","Ò","Ó","Ô","Õ","Ö","Ø","Ù","Ú","Û","Ü","Ý","Þ","ß","à","á",
                                "â","ã","ä","å","æ","ç","è","é","ê","ë","ì","í","î","ï","ð","ñ","ò","ó",
                                "ô","õ","ö","ø","ù","ú","û","ü","ý","þ","ÿ","","","a"
                };
                int i;
                for(i=0;i<entities.length;i++){
                        s = s.replace(entities[i], characters[i]);
                }
                return s;
        }
}

