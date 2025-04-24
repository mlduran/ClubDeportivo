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
    <link rel="STYLESHEET" type="text/css" href="${root}/css/general.css">

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.tituloapp}</title>
    </head>
    <%--Definición del cuerpo del documento HTML--%>
    <body>
        
        <table class="tabla">
            <tr>
                <th>
                <c:if test="${error == null}">
                    LA JORNADA SE HA LANZADO CORRECTAMENTE
                </c:if>

                    <c:if test="${error != null}">
                        ERROR AL LANZAR JORNADA      
                    </c:if>
                </th>
            </tr>
            <tr>
                <td>${error}</td>
            </tr>
            
        </table>
      
    </body>
</html>

