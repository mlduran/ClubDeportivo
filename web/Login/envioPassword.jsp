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
            <h3>ENVIO PASSWORD</h3>

        <form method="POST" action="${root}/login">

            <table class="tabla" cellpadding="3">  

                <tr align="left">
                    <td><label for="usuario" accesskey="i">Usuario:</label></td>
                    <td>
                        <input type="text" name="usuarioEnvio" id="usuario" title="Introduce el usuario de acceso" maxlength="20" style="width: 60px" value="${param.usuario}"/>
                        <c:if test="${not empty requestScope.errors.usuario}"><img src="${root}/images/iconError.gif" alt="Error" title="${requestScope.errors.usuario}"/> </c:if>
                    </td>
                </tr>            

                <tr align="left">
                    <td><label for="mail" accesskey="i">Mail:</label></td>
                    <td>
                        <input type="text" name="mailEnvio" id="mail" title="Introduce una dirección de correo" maxlength="255" style="width: 200px" value="${param.mail}"/>
                        <c:if test="${not empty requestScope.errors.mail}"><img src="${root}/images/iconError.gif" alt="Error" title="${requestScope.errors.mail}"/> </c:if>
                    </td>
                </tr>
                
                <tr>
                    <td colspan="2" align="center">
                        <input type="submit" id="bSubmit" name="bSubmit" value="Enviar Nuevo Password" accesskey="E"/>
                    </td>
                </tr>
            </table>
         
        </form>
      
        <br/>
        <p>Cumplimenta el usuario y el correo con el que te diste de alta y te enviaremos un nuevo password</p>
        <br/>
        <c:if test="${resultado != null}">
            <p class="error">${resultado}</p>
        </c:if>
        <br/>
        <a href="${root}/alta/login" title="Login" >Volver</a>


    <br/><br/>
        </div>

         <div id="columna_derecha_login">
             <br/>
            <img src="${root}/images/deportes2.png" title="Deportes" alt="Deportes"/>
        </div>
        
        </div>
        

        </div>

    </body>
</html>

