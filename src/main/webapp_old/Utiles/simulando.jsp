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
        <div id="cabecera_login">
            <c:import url="/WEB-INF/jspf/cabecera.jspf"/>
        </div>

        <div id="grupo">
        <div id="columna_izquierda_login">
            <img  style="padding: 0 0 0 0px;" src="${root}/images/deportes1.png" title="Deportes" alt="Deportes"/>
        </div>

        <div id="columna_central_login">

            <p class="titulo1"> SE ESTA SIMULANDO LA JORNADA .... <br/>
            EN BREVES MOMENTOS PODRAS ACCEDER</p> 

           <p class="ayuda">${texto}</p>           
           <img align="middle" src="${root}/images/simulando.png" title="Simulando" alt="Simulando"/>
           <p class="titulo1"><a  href="${root}" >Reintentar</a></p>
           
        </div>

        <div id="columna_derecha_login">
            <img src="${root}/images/deportes2.png" title="Deportes" alt="Deportes"/>
        </div>
        </div>

        </div>

    </body>
  
</html>

