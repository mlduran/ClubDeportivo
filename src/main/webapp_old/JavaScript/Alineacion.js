/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


var jugador = function(cuadrante, id, posicion, valoracion, nombre){
    
    this.id = id;
    this.nombre = nombre;
    this.posicion = posicion;
    this.valoracion = valoracion;
    this.cuadrante = cuadrante;
    this.contenedor = null;
    this.tipo = function () { 
        if (this.posicion =="Portero")
            return "portero";
        else
            return "jugador";
    };   
    this.crearImagen = function (src) {
        var icoJug;
        icoJug = document.createElement('img');
        icoJug.id = this.id;
        icoJug.className = this.tipo();
        icoJug.alt = "*";
        icoJug.draggable = true;
        icoJug.src = src; 
        return icoJug;
    }
    
    this.isAliniado = function(){
        return this.cuadrante != "XX";
    }
}



var listaJugadores = new Array();
var imagsDrag = new Array();

 function cargarAlineacion(txtAliniacion){

        if (txtAliniacion == "") return;

        elems = txtAliniacion.split(';');

        for (i = 0; i < elems.length; i++){

            datos = elems[i].split('_');
            cuadr = datos[0];
            id = datos[1];
            pos = datos[2];
            val = datos[3];
            nom = datos[4];
            
            if(pos == null || pos == "") continue;
            
            jug = new jugador(cuadr, id, pos, val, nom);
            listaJugadores[i] = jug;
            
            if (cuadr != "XX"){
                contenedor = $('#' + cuadr);
                jug.contenedor = contenedor[0];
                //jug.contenedor
                etq = contenedor.find('.labelJugador');
                etq.val(jug.nombre + "(" + jug.valoracion + ")");
                etq.css({'visibility': 'visible'});
                
                // al no tener una posicion Absolute tengo
                // que eliminar la imagen y volverla a crear
                // en el sitio que estaba
                imag = $('#' + jug.id)[0];
                src = imag.src;
                padre = imag.parentNode;
                padre.removeChild(imag);
                icoJug = jug.crearImagen(src);               
                contenedor.append(icoJug);  
                imagsDrag.push(icoJug)
                contenedor[0].style.backgroundColor = "#" + getColor(jug);
                // Eliminamos la fila
                document.getElementById("fila" + id).style.display = "none";
                
            }               

        }
    
    }
    function obtenerAlineacion(){
    
        alineacion = "";
 
        for (i = 0; i < listaJugadores.length; i++){
            jug = listaJugadores[i];
            if (jug.cuadrante == "XX") continue;
            if (alineacion != "") alineacion = alineacion + ";";
            alineacion = alineacion + jug.cuadrante + "_" + jug.id;                   
        }

        document.getElementById("alineacion").value = alineacion;

    }
    
    function getJugador(id){
        
        jug = undefined;
        
        for (i = 0; i < listaJugadores.length; i++){
            if (listaJugadores[i].id == id) jug = listaJugadores[i];
        }
        
        return jug;        
        
    }
    
    function getColor(jug){        
        celda = document.getElementById("color" + jug.id);
        color = celda.bgColor;
        return color;
    }
    

$(document).ready(function(){
	
        // Creo todos los jugadores
        cargarAlineacion($('#alineacion').val());
        
        // Relleno alineacion para servidor
        $("#operacionGrabar1").click(function(){
            obtenerAlineacion();
        });
        $("#operacionGrabar2").click(function(){
            obtenerAlineacion();
	});

});

var objClickado, contenedorClickado = null;

$(function() { // Cargamos el DOM
    //
    // Valores iniciales de objetos
    var objArrastrado, objContenedor = null;
    
    var jugadorActivo = null;
    
    var activarArrastrable = function(e) {
        e.addEventListener('dragstart', dragStart, false);
        e.addEventListener('dragend', dragEnd, false);
        e.addEventListener('click', clickJugador, false);
    }
    
    var desactivarArrastrable = function(e) {
        e.removeEventListener('dragstart', dragStart, false);
        e.removeEventListener('dragend', dragEnd, false);
    }
    
    var activarContenedor = function(e) {
        e.addEventListener('dragenter', dragEnter, false);
        e.addEventListener('dragover', dragOver, false);
        e.addEventListener('drop', dragDrop, false);
        e.addEventListener('dragleave', dragLeave, false);
        e.addEventListener('click', clickContenedor, false);
       
    }
    
    var desactivarContenedor = function(e) {
        e.removeEventListener('dragenter', dragEnter, false);
        e.removeEventListener('dragover', dragOver, false);
        e.removeEventListener('drop', dragDrop, false);
        e.removeEventListener('dragleave', dragLeave, false);
        e.removeEventListener('click', clickContenedor, false);
       
    }


    // Eventos para el objeto arrastrable
    var jugadores = $('#tablaJugadores').find('.jugador');
    var porteros = $('#tablaJugadores').find('.portero');    
    // Añade porteros a la lista de jugadores
    Array.prototype.push.apply( jugadores, porteros ); 
    // Añade las imagenes que hemos generado en la carga inicial
    Array.prototype.push.apply( jugadores, imagsDrag);
    
    [].forEach.call(jugadores, activarArrastrable);
 
    // Eventos para el panel origen
    /*
    var origenes = $('#origen');
    [].forEach.call(origenes, function(e) {
    e.addEventListener('dragenter', dragEnter, false);
    e.addEventListener('dragover', dragOver, false);
    e.addEventListener('drop', dragDrop, false);
    e.addEventListener('dragleave', dragLeave, false);
    });*/
 
    // Eventos para el panel destino
    var destinosJugadores = $('.celdaJugador');
    var destinoPortero = $('.celdaPortero');
    Array.prototype.push.apply( destinosJugadores, destinoPortero );   
    
    var contenedores = new Array();
    
    for (i = 0; i < destinosJugadores.length; i++){
        if (destinosJugadores[i].children.length < 3){
            contenedores.push(destinosJugadores[i]);
        }            
    }
    
    [].forEach.call(contenedores, activarContenedor);
    
    function posicionOK(){
        
        ok = true;
        if (objContenedor.id == 'P0' && jugadorActivo.posicion != 'Portero') ok = false;
        if (objContenedor.id != 'P0' && jugadorActivo.posicion == 'Portero') ok = false;
        return ok;
    }
    function posicionClickOK(){
        
        ok = true;
        if (contenedorClickado.id == 'P0' && jugadorActivo.posicion != 'Portero') ok = false;
        if (contenedorClickado.id != 'P0' && jugadorActivo.posicion == 'Portero') ok = false;
        return ok;
    }
     
   
   
        function clickJugador(e) {
            // Guardamos el objeto clickado
            if (objClickado == null){
                objClickado = this;
                jugadorActivo = getJugador(objClickado.id);
                if (jugadorActivo.isAliniado()){
                    // lo quitamos
                    quitarJugador(objClickado);
                    objClickado = null;
                }
                else{
                    // Modificamos su fondo
                    
                    laCelda = objClickado.parentNode.parentNode;
                    laCelda.style.backgroundColor = "red";
                }
            }
            else{
                laCelda = objClickado.parentNode.parentNode;
                laCelda.style.backgroundColor = ""; 
                objClickado = null;
            }
	}
        
        function clickContenedor(e) {
            // Guardamos el objeto clickado
            contenedorClickado = this;     
            if (objClickado == null) return;
            if (!posicionClickOK()) return;            
            
            laCelda = objClickado.parentNode.parentNode;
            laCelda.style.backgroundColor = ""; 
            ponerJugador(contenedorClickado);
            icoJug = jugadorActivo.crearImagen(objClickado.src); 
            activarArrastrable(icoJug);
            contenedorClickado.appendChild(icoJug);   
            objClickado = null;
            contenedorClickado = null;
	}
   
	
	/* El objeto 'comienza' a ser arrastrado */
	function dragStart(e) {

		// Guardamos el objeto que es arrastrado
		objArrastrado = this;                
                               
                jugadorActivo = getJugador(objArrastrado.id);
                             
		// Establecemos la operacion que se va a poder realizar
		e.dataTransfer.effectAllowed = 'move';
		// Establecemos el dato y su tipo
		e.dataTransfer.setData('Data', objArrastrado);

	}

	/* Estamos 'entrando' en el area donde se puede dejar un objeto arrastrable */
	function dragEnter(e) {           

		// Ocultamos el objeto que se queda estatico
		$(objArrastrado).css('visibility', 'hidden');
	}

	/* Situados 'encima' del area donde se puede dejar un objeto arrastrable */
	function dragOver(e) {             

		// Obtenemos el valor objeto del contenedor sobre el que se posa el objeto arrastrable
		objContenedor = this;
                
                if (!posicionOK()) return;

		// El cursor del navegador indica el tipo de operaciÃ³n que se va a realizar
		e.dataTransfer.dropEffect = 'move';

		// Necesario para dejar caer el objeto arrastrado
		if(e.preventDefault)
			e.preventDefault();

	}

	/* Estamos 'soltando' un objeto arrastrable */
	function dragDrop(e) { 

                if (!posicionOK()) return;
                
		// Agregamos el objeto 'arrastrable' en el contenedor actual
		$(this).append(objArrastrado);

		// Necesario para evitar el redireccionamiento de navegadores
		if(e.stopPropagation)
			e.stopPropagation();

	}

	/* Estamos 'saliendo' del area donde se puede dejar un objeto arrastrable */
	function dragLeave(e) {   
      

	}

	/* El objeto 'termina' de ser arrastrado */
	function dragEnd(e) {
                // Mostramos el objeto arrastrado previamente ocultado
		$(objArrastrado).css('visibility', 'visible');
                
                if (e.dataTransfer.dropEffect == 'none'){
                    if (jugadorActivo.contenedor != null){  
                        quitarJugador(objArrastrado);
                    }
                }
                else{
                    if (!posicionOK()) return;
                    if (jugadorActivo.contenedor != null){
                        moverJugador();
                    }   
                    ponerJugador(objContenedor);
                 }   
                 
                 e.dataTransfer.clearData('Data');
	}
        
        
        function moverJugador(){
            elem = $(jugadorActivo.contenedor).find('.labelJugador');
            elem.val("");
            $(elem).css({'visibility': 'hidden'});
            activarContenedor(jugadorActivo.contenedor);
            jugadorActivo.contenedor.style.backgroundColor = "lightgreen";
        }
        
        
        function ponerJugador(contenedor){
            
            fila = document.getElementById("fila" + jugadorActivo.id);
            elem = $(contenedor).find('.labelJugador');
            elem.val(jugadorActivo.nombre + "(" + jugadorActivo.valoracion + ")");
            $(elem).css({'visibility': 'visible'});
            contenedor.style.backgroundColor = "#" + getColor(jugadorActivo);
            jugadorActivo.cuadrante = elem.context.id;                    
            jugadorActivo.contenedor = contenedor;
            desactivarContenedor(contenedor);
            fila.style.display = "none";
        }
        
        
        function quitarJugador(objeto){
            contenedor = jugadorActivo.contenedor;
            activarContenedor(contenedor);
            contenedor.style.backgroundColor = "lightgreen";
            elem = $(contenedor).find('.labelJugador');
            $(elem).css({'visibility': 'hidden'});
            jugadorActivo.cuadrante = "XX";
            jugadorActivo.contenedor = null;
            document.getElementById("fila" + jugadorActivo.id).style.display = "";
            src = objeto.src;
            padre = objeto.parentNode;
            padre.removeChild(objeto);
            
            // Si no tenemos el icono en la fila lo creamos 
            celdaJug = $('#jug' + jugadorActivo.id)
            if (celdaJug[0].childElementCount == 0){                
                icoJug = jugadorActivo.crearImagen(src);                
                celdaJug.append(icoJug);                        
                activarArrastrable(icoJug);        
            }                
                        
        }
        
        

});
