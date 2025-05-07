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
            <br/>
            <img  style="padding: 0 0 0 0px;" src="${root}/images/deportes1.png" title="Deportes" alt="Deportes"/>
        </div>

        <div id="columna_central_login">
            <h3>ALTA DE GRUPO</h3>

        <form method="POST" action="${root}/alta/grupo">

            <table class="tabla">
                <tr>
                    <td><label for="nombre" accesskey="i">Codigo Grupo:</label></td>
                    <td>
                        <input type="text" name="nombre" id="nombre" title="Introduce el codigo para el grupo (6-20) caracteres, sin espacios" maxlength="20" style="width: 100px" value="${param.nombre}"/>
                        <c:if test="${not empty requestScope.errors.nombre}"><img src="${root}/images/iconError.gif" alt="Error" title="${errors.nombre}"/> </c:if>
                    </td>
                </tr>

                <tr>
                    <td colspan="2" align="center">
                        <input type="radio" name="privado" value="false" checked="checked"/>Publico
                        <input type="radio" name="privado" value="true"/>Privado
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <input type="submit" id="bSubmit" name="bSubmit" value="Crear" accesskey="E"/>
                    </td>
                </tr>
            </table>
            <br/>
                  <a href="${root}/ayuda?grupo=General&codigo=altaGrupo" target="_blank"><img src="${root}/images/ayuda.gif"></a> 
            <br/>
            
            <c:if test="${newgrupo.codigo != 0}">
                <p>El codigo de grupo debe de contener de 6 a 20 caracteres (letras o numeros sin espacios)</p>
            </c:if>
            <c:if test="${newgrupo.codigo != null && newgrupo.codigo != 0}">
                <p class="error">La contraseña para este grupo privado es: ${newgrupo.codigo}
                para dar de alta tu club y el del resto de tus amigos debeis utilizarla.
                Puedes volver al formulario de registro y darte de alta en este grupo</p>
            </c:if>
            <c:if test="${newgrupo.codigo == 0}">
                Puedes volver al formulario de registro y darte de alta en este grupo
            </c:if>

             <c:if test="${not empty requestScope.errors}">
                <br/>
                <p class="error">
                    Se han producido los siguientes errores:<br/>
                    <c:forEach var="error" items="${requestScope.errors}">
                        <i>${error.value}</i><br/>
                    </c:forEach>
                </p>

            </c:if>
        </form>

        <br/>
        <a href="${root}/alta/club">Volver</a>
        </div>

         <div id="columna_derecha_login">
             <br/>
            <img src="${root}/images/deportes2.png" title="Deportes" alt="Deportes"/>
        </div>
        
        </div>

        </div>
    </body>
</html>
