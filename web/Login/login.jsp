<%--Sentencias de declaración o directivas de la página--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%--Definición del DOCTYPE o tipo de documento--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">



<%--Definición de la estructura del documento HTML--%>
<html>
    <%--Definición de la cabecera o metados del documento HTML--%>
    <c:set var="root" value="${pageContext.servletContext.contextPath}"/>
    <link rel="STYLESHEET" type="text/css" href="${root}/css/estiloPanelControl.css"/>
    <link rel="STYLESHEET" type="text/css" href="${root}/css/general.css"/>
    <script type="text/javascript" src="${root}/JavaScript/JuegoPenalties.js"></script>
       
    
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.tituloapp}</title>
    </head>
    <%--Definición del cuerpo del documento HTML--%>
    <body> 

        <div id="contenedor">
        <div id="cabecera_login">
            <c:import url="/WEB-INF/jspf/cabecera.jspf"/>
        </div>

        <div id="grupo">
        <div id="columna_izquierda_login">
            <br/>
            <img  style="padding: 0 0 0 0px;" src="${root}/images/deportes1.png" title="Deportes" alt="Deportes"/>
        </div>

        <div id="columna_central_login" >

            <br/>
            <form action="${root}/login" method="post">
                Usuario: <input type="text" name="usuario" value="" />
                Password: <input type="password" name="password" value="" />
                <input type="submit" value="Entrar"  />
            </form>
            <a href="${root}/login?accion=envioPassword"><p class="comentario">No me acuerdo de mi Password</p></a>
                        
                <c:if test="${requestScope.noValidado != null}">
                    <p class="error">Usuario o Password incorrectos</p>
                </c:if>

                <br/>
                <a href="${root}/alta/club" title="Registro" >Registro</a>

                <br/><br/>
                <hr/>
                <c:choose>
                    <c:when test="${requestScope.listaranking != null}">

                        <c:set var="lista"
                               value="${requestScope.listaranking}"/>

                        <c:if test="${fn:length(lista) > 0}">


                            <table style="width: 250px" class="tabla">
                                <tr>
                                    <th>Numero</th>
                                    <th>Club</th>
                                </tr>

                                <c:forEach var="obj" items="${lista}" varStatus="cuenta">
                                    <tr>
                                        <td>${cuenta.count}</td>
                                        <td>
                                            <a href="${root}/panelControl/fichaClub?id=${obj.id}">
                                                ${obj.nombre}
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>

                            </table>

                        </c:if>

                        <c:if test="${fn:length(lista) == 0}">

                        </c:if>

                    </c:when>

                    <c:when test="${requestScope.error != null}">

                        Se ha producido el siguiente error:
                        ${requestScope.error.message}

                    </c:when>

                        <c:otherwise>
                            ERROR NO ESPERADO
                        </c:otherwise>

                </c:choose>

                <br/>
                <input class="error" size="10" type="text" id="reloj" disabled />
                <input type="hidden" id="fechaServer" value="${fechaServer}" />
                <br/>  
                <c:if test="${txtBannerEntrada ne ''}">
                    <br/>  
                    <table align="center" width ="60%">
                        <tr>
                            <td class="banner">
                        <MARQUEE>
                            ${txtBannerEntrada}
                        </MARQUEE>
                            </td>
                        </tr>
                    </table>
                    <br/>  
                </c:if>
                <c:if test="${juego != null && juego ne ''}">
                    <br/>  
                    <div id="juego"></div>
                    <br/>  
                </c:if>
                <hr/>
                <br/>
                <i>Para más información: <a href="mailto:${initParam.mailcontacto}">${initParam.mailcontacto}</a></i>
                <br/><br/>
                <i>Página optimizada para última versión de <a href="http://www.google.es/intl/es/chrome/">chrome</a></i>
                <br/>
                <br/>
                <hr/>
                <br/>
                <table width="100%">
                    <tr>
                        <td width="30%" class="presentacion" bgcolor="black"><p class="presentacion">ClubDeportivo es el juego online y gratuito que te permite convertirte en el manager general de un Club con diferentes secciones y competir diariamente con otros amigos. Actualmente las secciones disponibles son Futbol8 y Peña Quinielista, próximamente se incorporará la de Basket</p></td>
                        <td width="40%" ><img alt="Presentacion" id="imagenPresentacion" src="${root}/images/presentacion/visualizarPartidoFutbol8.png"/></td>
                        <td width="30%" class="presentacion" bgcolor="black"><p class="presentacion" id="textoPresentacion">Futbol8 te permite competir diariamente con otros amigos en liga y copa. Controla el aspecto económico de tu equipo y toma todas las decisiones deportivas que llevarán a lo mas alto. </p></td>
                    </tr>
                </table>                
                <br/>
                <br/>

        </div>

        <div id="columna_derecha_login">
            <br/>
            <img src="${root}/images/deportes2.png" title="Deportes" alt="Deportes"/>
        </div>
        
        </div>

        </div>
        <input type="hidden" id="juegoElegido" value="${juego}"/>

    </body>
    
    <script type="text/javascript">
    
    var tick, anyo, mes, dia, hor, min, seg;
    
    var ut;	
    window.onload = iniciarReloj();

 
 function iniciarReloj(){
     
     stringHora = document.getElementById('fechaServer').value;
     datos = stringHora.split("-");  

     dia = datos[0];
     mes = datos[1];
     anyo = datos[2];
     hor = datos[3];
     min = datos[4];
     seg = datos[5];
     
     ut = new Date (anyo, mes, dia, hor, min, seg);

     relojServidor();

 }
 
 var tiempoGrafico = 15;
 var tiempoGraficoActual = 15;
 var numeroImagenes = 6;
 var imagenActual = 1;
 
 var imagenes = function(numero){
     
     if (numero == 1) return "prepararPartidoFutbol8.png"
     else if (numero == 2) return "informePartidoFutbol8.png"
     else if (numero == 3) return "visualizarPartidoFutbol8.png"
     else if (numero == 4) return "plantilla.png"
     else if (numero == 5) return "quiniela.png"
     else if (numero == 6) return "grafico.png"
     
     
     
 }
 
 var textos = function(numero){
     
     if (numero == 1) return "En Futbol8 puedes preparar tu partido eligiendo diferentes alineaciones, tacticas y estrategias y realizando realizando simulaciones para ver los diferentes resultados"
     else if (numero == 2) return "En Futbol8 tendras un informe detallado de los acontecido en el partido para poder analizar tu resultado y las posibilidades reales de victoria que tenias"
     else if (numero == 3) return "En Futbol8 podras analizar en un grafico las acciones realizadas durante el partido ademas de ver su cronica"
     else if (numero == 4) return "En Futbol8 dispones de una plantilla de un maximo de 16 jugadores que tendras que entrenar y gestionar de la mejor manera posible para sacar su máximo rendimiento"
     else if (numero == 5) return "En la Quiniela puedes realizar 2 columnas de apuestas para obtener los maximos aciertos y puntos y conseguir ser el mejor pronosticador"
     else if (numero == 6) return "Dispones de diferentes graficos que te ayudaran a realizar una mejor gestion de tu Club"
   
     
 }
 
 
 function relojServidor() {
     
     ut.setSeconds(ut.getSeconds() + 1);
     
     modImagen = ut.getSeconds() + 1
     
     var h,m,s;
     var time=" ";
     h=ut.getHours();
     m=ut.getMinutes();
     s=ut.getSeconds();
     
     if(s<=9) s="0"+s;
     if(m<=9) m="0"+m;
     if(h<=9) h="0"+h;
     time+=h+":"+m+":"+s;
     document.getElementById('reloj').value=time;   
     
     tiempoGraficoActual = tiempoGraficoActual - 1;
     if (tiempoGraficoActual == 0){
         // aqui cambio de imagen texto
         imag = document.getElementById('imagenPresentacion').src
         partes = imag.split("/");
         ruta = "";
         for (i = 0; i < partes.length - 1; i++){
             ruta = ruta + partes[i] + "/";
         }
         newImag = ruta + imagenes(imagenActual);
         document.getElementById('imagenPresentacion').src = newImag;
         document.getElementById('textoPresentacion').innerHTML = textos(imagenActual);
         
         imagenActual = imagenActual + 1;
         if (imagenActual > numeroImagenes)
             imagenActual = 1;
         
         tiempoGraficoActual = tiempoGrafico;
     }
     
     tick=setTimeout("relojServidor()",1000); 
 }
 </script>
    
</html>

