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
            <c:import url="/WEB-INF/jspf/Quiniela/acciones.jspf"/>
        </div>

        <div id="columna_central">
            <div id="divTitulo">
                ${titulo}
            </div>
            
            
            <div id="divContenido">

            <c:choose>

                <c:when test="${requestScope.op eq 'inicio'}">
                    <c:import url="/WEB-INF/jspf/Quiniela/inicio.jspf"/>
                </c:when>

                <c:when test="${requestScope.op eq 'cumplimentar'}">
                    <c:import url="/WEB-INF/jspf/Quiniela/cumplimentar.jspf"/>
                </c:when>

                <c:when test="${requestScope.op eq 'jornada'}">
                    <c:import url="/WEB-INF/jspf/Quiniela/jornada.jspf"/>
                </c:when>

                <c:when test="${requestScope.op eq 'clasificacion'}">
                    <c:import url="/WEB-INF/jspf/Quiniela/clasificacion.jspf"/>
                </c:when>

                <c:when test="${requestScope.op eq 'jornadasDisputadas'}">
                    <c:import url="/WEB-INF/jspf/Quiniela/jornadasDisputadas.jspf"/>
                </c:when>

                <c:when test="${requestScope.op eq 'competiciones'}">
                    <c:import url="/WEB-INF/jspf/Quiniela/competiciones.jspf"/>
                </c:when>
                
                <c:when test="${requestScope.op eq 'cumplimentarAdmin'}">
                    <c:import url="/WEB-INF/jspf/Quiniela/cumplimentarAdmin.jspf"/>
                </c:when>

            </c:choose>
            </div>

        </div>
      

        <div id="columna_derecha">
            <c:import url="/WEB-INF/jspf/Quiniela/panelGrupo.jspf"/>
            <c:import url="/WEB-INF/jspf/generico.jspf"/>
        </div>
          </div>
        </div>
      
    </body>
</html>

