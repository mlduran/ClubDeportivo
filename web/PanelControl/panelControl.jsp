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
            <c:import url="/WEB-INF/jspf/acciones.jspf"/>
        </div>

        <div id="columna_central">

            <c:choose>

                <c:when test="${requestScope.op eq 'presentacion'}">
                    <c:import url="/WEB-INF/jspf/presentacion.jspf"/>
                </c:when>

                <c:when test="${requestScope.op eq 'fichaClub'}">
                    <c:import url="/WEB-INF/jspf/fichaClub.jspf"/>
                </c:when>

                <c:when test="${requestScope.op eq 'datosUsuario'}">
                    <c:import url="/WEB-INF/jspf/datosUsuario.jspf"/>
                </c:when>
                
                <c:when test="${requestScope.op eq 'faqs'}">
                    <c:import url="/WEB-INF/jspf/faqs.jspf"/>
                </c:when>
                
                 <c:when test="${requestScope.op eq 'ranking'}">
                    <c:import url="/WEB-INF/jspf/ranking.jspf"/>
                </c:when>

            </c:choose>


        </div>

        <div id="columna_derecha">
            <c:import url="/WEB-INF/jspf/panelGrupo.jspf"/>
            <c:import url="/WEB-INF/jspf/generico.jspf"/>
        </div>
        </div>
    </div>


    </body>
</html>

