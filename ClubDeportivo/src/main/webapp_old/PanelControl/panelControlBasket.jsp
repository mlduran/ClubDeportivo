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
     <link rel="STYLESHEET" type="text/css" href="${root}/css/estiloPanelControl.css">
     <link rel="STYLESHEET" type="text/css" href="${root}/css/general.css">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.tituloapp}</title>
    </head>
    
    <%--Definición del cuerpo del documento HTML--%>
    <body>

         <div id="contenedor">
        <div id="cabecera">
            <c:import url="/WEB-INF/jspf/cabecera.jspf"/>
        </div>
        
        <div id="grupo">

        <div id="columna_izquierda">
            <c:import url="/WEB-INF/jspf/Futbol8/acciones.jspf"/>
        </div>

        <div id="columna_central">
            <div id="divTitulo">
                ${titulo}
            </div>
            
            
            <div id="divContenido">
            <c:choose>

                <c:when test="${requestScope.op eq 'inicio'}">
                    <c:import url="/WEB-INF/jspf/Futbol8/inicio.jspf"/>
                </c:when>

                <c:when test="${requestScope.op eq 'fichaEquipo'}">
                    <c:import url="/WEB-INF/jspf/Futbol8/fichaEquipo.jspf"/>
                </c:when>

                <c:when test="${requestScope.op eq 'gestion'}">
                    <c:import url="/WEB-INF/jspf/Futbol8/gestion.jspf"/>
                </c:when>

                <c:when test="${requestScope.op eq 'prepararPartido'}">
                    <c:import url="/WEB-INF/jspf/Futbol8/prepararPartido.jspf"/>
                </c:when>
                
                <c:when test="${requestScope.op eq 'entrenamiento'}">
                    <c:import url="/WEB-INF/jspf/Futbol8/entrenamiento.jspf"/>
                </c:when>

                <c:when test="${requestScope.op eq 'entrenador'}">
                    <c:import url="/WEB-INF/jspf/Futbol8/entrenador.jspf"/>
                </c:when>
                
                <c:when test="${requestScope.op eq 'cursoEntrenador'}">
                    <c:import url="/WEB-INF/jspf/Futbol8/cursoEntrenador.jspf"/>
                </c:when>

                <c:when test="${requestScope.op eq 'calendario'}">
                    <c:import url="/WEB-INF/jspf/Futbol8/calendario.jspf"/>
                </c:when>
                
                <c:when test="${requestScope.op eq 'partido'}">
                    <c:import url="/WEB-INF/jspf/Futbol8/partido.jspf"/>
                </c:when>

                <c:when test="${requestScope.op eq 'clasificacion'}">
                    <c:import url="/WEB-INF/jspf/Futbol8/clasificacion.jspf"/>
                </c:when>

                <c:when test="${requestScope.op eq 'movimientos'}">
                    <c:import url="/WEB-INF/jspf/Futbol8/movimientos.jspf"/>
                </c:when>
                
                <c:when test="${requestScope.op eq 'mercado'}">
                    <c:import url="/WEB-INF/jspf/Futbol8/mercado.jspf"/>
                </c:when>
                
                <c:when test="${requestScope.op eq 'plantilla'}">
                    <c:import url="/WEB-INF/jspf/Futbol8/plantilla.jspf"/>
                </c:when>
                
                <c:when test="${requestScope.op eq 'jugador'}">
                    <c:import url="/WEB-INF/jspf/Futbol8/jugador.jspf"/>
                </c:when>
                
                <c:when test="${requestScope.op eq 'goleadores'}">
                    <c:import url="/WEB-INF/jspf/Futbol8/goleadores.jspf"/>
                </c:when>
                
                <c:when test="${requestScope.op eq 'porteros'}">
                    <c:import url="/WEB-INF/jspf/Futbol8/porteros.jspf"/>
                </c:when>
                
                <c:when test="${requestScope.op eq 'competiciones'}">
                    <c:import url="/WEB-INF/jspf/Futbol8/competiciones.jspf"/>
                </c:when>
                
                <c:when test="${requestScope.op eq 'noticias'}">
                    <c:import url="/WEB-INF/jspf/noticias.jspf"/>
                </c:when>
                
                <c:when test="${requestScope.op eq 'comentarios'}">
                    <c:import url="/WEB-INF/jspf/comentarios.jspf"/>
                </c:when>
                
                <c:when test="${requestScope.op eq 'auditoria'}">
                    <c:import url="/WEB-INF/jspf/auditoria.jspf"/>
                </c:when>
                
                 <c:when test="${requestScope.op eq 'estadistica'}">
                    <c:import url="/WEB-INF/jspf/Futbol8/estadistica.jspf"/>
                </c:when>
                
                 <c:when test="${requestScope.op eq 'administracion'}">
                    <c:import url="/WEB-INF/jspf/Futbol8/administracion.jspf"/>
                </c:when>

            </c:choose>
            </div>
        </div>

        <div id="columna_derecha">
            <c:import url="/WEB-INF/jspf/Futbol8/panelGrupo.jspf"/>
            <c:import url="/WEB-INF/jspf/generico.jspf"/>
        </div>
        
        </div>
         </div>
  
      
    </body>
</html>

