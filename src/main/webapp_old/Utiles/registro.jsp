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

        <table style="width: 800px" class="tabla">
            <tr>
                <td colspan="4">ENTRADAS AL SISTEMA</td>
                <td>
                    <form action="${root}/registro" method="post">
                        <input type="submit" value="Ejecutar"  />
                        <input class="txtNum" type="text" name="numero" value="${numero}" size="3"/>Registros
                    </form>
                </td>                
            <tr>
                <th>Id</th>
                <th>Fecha</th>
                <th>Tipo</th>
                <th>Usuario</th>
                <th>IP</th>
            </tr>
            <c:forEach var="reg" items="${regs}">
                <tr>
                    <td align="center">${reg.id}</td>
                    <td><fmt:formatDate value="${reg.fecha}" type="both"
                                    timeStyle="short" dateStyle="short" />
                    </td>
                    <td>${reg.tipo}</td>
                    <td>${reg.usuario}</td>
                    <td>${reg.ip}</td>
                </tr>
            </c:forEach>

    </table>

    <br>

    <c:if test="${error != null}">
        ${error}
    </c:if>
      
    </body>
</html>

