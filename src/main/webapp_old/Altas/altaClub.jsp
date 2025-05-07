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
            <h3>REGISTRO</h3>

        <form method="POST" action="${root}/alta/club">

            <table class="tabla" cellpadding="3">
                <tr align="left">
                    <td>
                        <label for="grupo" accesskey="i">Grupo:</label>
                        <br/>
                        <a href="${root}/alta/grupo" title="Alta Grupo" >Crear Grupo</a>
                    </td>
                    <td>
                        GRUPOS PUBLICOS
                        <SELECT NAME="grupopublico">
                            <c:if test="${newgrupo == null || (newgrupo != null && newgrupo.privado)}">
                                <OPTION VALUE="">
                            </c:if>
                            <c:forEach var="grupo" items="${requestScope.grupospublicos}" step="1">
                                <OPTION VALUE="${grupo.id}"> ${grupo.nombre}
                            </c:forEach>
                        </SELECT>
                        <br/>
                        GRUPOS PRIVADOS
                        <SELECT NAME="grupoprivado">
                            <c:if test="${newgrupo == null || (newgrupo != null && !newgrupo.privado)}">
                                <OPTION VALUE="">
                            </c:if>
                            <c:forEach var="grupo" items="${requestScope.gruposprivados}" step="1">
                                <OPTION VALUE="${grupo.id}"> ${grupo.nombre}
                            </c:forEach>
                        </SELECT>
                        <label for="codigo" accesskey="i">Contraseña:</label>
                        <input type="text" name="codigo" id="codigo" title="Si es un grupo privado introduce la contraseña" maxlength="255" style="width: 60px" value="${codigo}"/>
                        <c:if test="${not empty requestScope.errors.codigo}"><img src="${root}/images/iconError.gif" alt="Error" title="${requestScope.errors.codigo}"/> </c:if>
                    </td>                    
                </tr>
                
                <c:if test="${not empty requestScope.errors.grupo}">
                    <tr>
                        <td>
                            ${requestScope.errors.grupo}
                        </td>
                    </tr>                        
                </c:if>
                
                <tr align="left">
                    <td><label for="nombre" accesskey="i">Nombre Club:</label></td>
                    <td>
                        <input type="text" name="nombre" id="nombre" title="Introduce el nombre para tu Club" maxlength="20" style="width: 200px" value="${param.nombre}"/>
                        <c:if test="${not empty requestScope.errors.nombre}"><img src="${root}/images/iconError.gif" alt="Error" title="${requestScope.errors.nombre}"/> </c:if>
                    </td>
                </tr>

                <tr align="left">
                    <td><label for="usuario" accesskey="i">Usuario:</label></td>
                    <td>
                        <input type="text" name="usuario" id="usuario" title="Introduce el usuario de acceso" maxlength="20" style="width: 60px" value="${param.usuario}"/>
                        <c:if test="${not empty requestScope.errors.usuario}"><img src="${root}/images/iconError.gif" alt="Error" title="${requestScope.errors.usuario}"/> </c:if>
                    </td>
                </tr>

                <tr align="left">
                    <td><label for="password" accesskey="i">Contraseña:</label></td>
                    <td>
                        <input type="password" name="password" id="password" title="Introduce tu contraseña de acceso" maxlength="20" style="width: 100px" value="${param.password}"/>
                        <c:if test="${not empty requestScope.errors.password}"><img src="${root}/images/iconError.gif" alt="Error" title="${requestScope.errors.password}"/> </c:if>
                    </td>
                </tr>

                <tr align="left">
                    <td><label for="password2" accesskey="i">Repite Contraseña:</label></td>
                    <td>
                        <input type="password" name="password2" id="password2" title="Confirma tu contraseña de acceso" maxlength="20" style="width: 100px" value="${param.password2}"/>
                        <c:if test="${not empty requestScope.errors.password2}"><img src="${root}/images/iconError.gif" alt="Error" title="${requestScope.errors.password2}"/> </c:if>
                    </td>
                </tr>

                <tr align="left">
                    <td><label for="mail" accesskey="i">Mail:</label></td>
                    <td>
                        <input type="text" name="mail" id="mail" title="Introduce una dirección de correo" maxlength="255" style="width: 200px" value="${param.mail}"/>
                        <c:if test="${not empty requestScope.errors.mail}"><img src="${root}/images/iconError.gif" alt="Error" title="${requestScope.errors.mail}"/> </c:if>
                    </td>
                </tr>

                
                <tr>
                    <td colspan="2" align="center">
                        <input type="submit" id="bSubmit" name="bSubmit" value="Crear" accesskey="E"/>
                    </td>
                </tr>
            </table>

                    <br/>
                    <c:if test="${not empty requestScope.errors}">
                        Se han producido los siguientes errores:<br/>
                        <c:forEach var="error" items="${requestScope.errors}">
                            <i>${error.value}</i><br/>
                        </c:forEach>
                    </c:if>
                     <c:if test="${not empty requestScope.clubsave}">
                        Felicidades, tu club ha sido dado de alta<br/>
                    </c:if>
        </form>

        <br/>
        <a href="${root}/ayuda?grupo=General&codigo=altaClub" target="_blank"><img src="${root}/images/ayuda.gif"></a> 
        <br/>
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

