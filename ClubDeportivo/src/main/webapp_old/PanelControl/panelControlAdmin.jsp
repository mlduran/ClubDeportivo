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
    <c:set var="root" value="${pageContext.servletContext.contextPath}"/>
    <link rel="STYLESHEET" type="text/css" href="${root}/css/general.css">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.tituloapp}</title>
    </head>
    <%--Definición del cuerpo del documento HTML--%>
    <body>


    PAGINA DE ADMINISTRACION <br/> <br/>

 
    <form action="${root}/admin" method="post" id="general">
        
        <p>APLICACION</p>
         <br/>
         <c:if test="${mantenimient == null}">
             <p>hola</p>
         </c:if>
         <c:if test="${mantenimient eq 'false'}">
             <input type="submit" value="Iniciar Mantenimiento"  name="operacion" />
         </c:if>
        <c:if test="${mantenimient eq 'true'}">
             <input type="submit" value="Acabar Mantenimiento"  name="operacion" />
         </c:if>       
        <br/>  
        <input type="submit" value="Hacer Backup BD"  name="operacion" />
        <br/>  
        <br/>  
        <input type="submit" value="Prueba Correo"  name="operacion" />
         <input type="text" name="correoPrueba" value="ml_duran@live.com" />
        <br/>  
        
        
        <p>QUINIELAS</p><br/>
         <c:if test="${compQuini == null}">
             <input type="submit" value="Crear Competicion Quiniela"  name="operacion" />
         </c:if>
        
        <input type="text" name="nombrecompeticion" value="${compQuini.nombre}" />
        
        <br/>
        <c:if test="${compQuini != null}">
            <input type="submit" value="Finalizar Competicion Quiniela"  name="operacion" />  
        </c:if>
        <br/>
        <br/>  
        <input type="submit" value="Resultados Generales"  name="operacion" />
        <p>JORNADA</p><input type="text" name="jornadaresult" value="" size="2"/>
         
        <c:if test="${mostrarjornadaresult != null}">
            <p>Resultados : ${mostrarjornadaresult}</p>
        </c:if>
            
              
        <br/>
        <p>FUTBOL8</p> <br/>
        <table>
            <th>
                <p>Colores</p>
            </th>
            <tr>
                <c:forEach var="obj" items="${jugsF8}">
                    <td bgcolor="${obj.colorValoracion}" width="25px" align="center">${obj.valoracionReal}</td>
                </c:forEach>
            </tr>
        </table> 
        <input type="submit" value="Carga Maestro Entrenadores Futbol8"  name="operacion"/>
        <br/>
        <input type="submit" value="Carga Maestro Equipos Futbol8"  name="operacion"/>
        <br/>
        <input type="submit" value="Crear Competiciones Futbol8" name="operacion"/>
        <br/>
        <br/>        
        <input type="submit" value="Limpiar Tablas" name="operacion"/>
        <br/>
        <br/>
        <p>Grupos</p>
        <SELECT NAME="grupo">
            <c:forEach var="grp" items="${requestScope.grupos}" step="1">
                <OPTION VALUE="${grp.id}"> ${grp.nombre}
            </c:forEach>
        </SELECT>
        <input type="submit" value="Eliminar Grupo" name="operacion"/>
        <br/>
        <br/>
        <p>Clubs</p>
        <SELECT NAME="club">
            <c:forEach var="clb" items="${requestScope.clubs}" step="1">
                <OPTION VALUE="${clb.id}"> ${clb.nombre}
            </c:forEach>
        </SELECT>
        <input type="submit" value="Eliminar Club" name="operacion"/>
        <br/>
        <br/>
        <p>CompeticionesFutbol8</p>
        <SELECT NAME="competicionFutbol8">
            <c:forEach var="comp" items="${requestScope.competiciones}" step="1">
                <OPTION VALUE="${comp.id}"> ${comp.nombre}
            </c:forEach>
        </SELECT>
        <input type="submit" value="Eliminar Competicion Futbol8" name="operacion"/>
        <br/>
        

    </form>

    <br/>
    <a  href="${root}/cargaFichero" target="_blank" >Carga de Fichero</a>
    <br/>
    <br/>
    
    <form action="${root}/admin" method="post" id="comunicados">
    <table class="tabla" >
        <th align="left">COMUNICADOS</th>
        <tr align="left">        
            <td>
                <label>Titulo</label>
                <input type="text" name="tituloComunicado" id="tituloComunicado" title="Introduce titulo para el mail" maxlength="100" style="width: 500px"/>
                <br/>  
                <label>Comunicado</label>
                <textarea name="txtComunicado" rows="2" cols="90" title="Introduce el comunicado (max 255 caracteres)"></textarea>                              
            </td>
        </tr>
        <tr align="left">
            <td>
                <input type="submit" id="envioComunicado" name="operacion" value="Enviar Comunicado" accesskey="E"/>
                <SELECT NAME="tipoComunicado">
                        <OPTION VALUE="General">General
                        <OPTION VALUE="Quiniela">Quiniela
                        <OPTION VALUE="Futbol8">Futbol8
                </SELECT>
            </td>
        </tr>
    </table>
    
    <br/>
    <br/>
    
    <p>PREGUNTAS PENDIENTES</p>
    <table class="tabla" >
        <tr>
            <th>Fecha</th>
            <th>Club</th>
            <th>Pregunta</th>
        </tr>
        <tr>
            <td>${faq.fecha}</td>
            <td>${faq.club}</td>
            <td>${faq.pregunta}</td>
        </tr>
        <tr>
            <td colspan="3">
                <label>Respuesta</label>
                <textarea name="respuesta" rows="2" cols="90" title="Introduce la respuesta (max 255 caracteres)"></textarea>
            </td> 
        </tr>
        <tr>
            <td colspan="3">
                <input type="submit" id="envioRespuesta" name="operacion" value="Responder Faq"/>
                <input type="submit" id="eliminarFaq" name="operacion" value="Eliminar Faq"/>
                <input type="hidden" id="codigoFaq" name="codigoFaq" value="${faq.id}"/>
            </td>
        </tr>
    </table>


    </form>



    <c:if test="${error != null}">
        ERROR: ${error}

    </c:if>
      
    </body>
</html>

