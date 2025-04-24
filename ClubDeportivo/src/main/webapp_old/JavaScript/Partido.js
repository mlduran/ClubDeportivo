/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



var listaAcciones = new Array();
var accionActiva = 0;
var numAcciones = 0;

var accion = function(id, minuto, accion, cuadrante){
    
    this.id = id;
    this.minuto = minuto;
    this.accion = accion;
    this.cuadrante = cuadrante;
   
};   

function actualizarAccion(){
    
    if (numAcciones > 0){
        $("#txtMinuto").val("Minuto : " + listaAcciones[accionActiva].minuto);
        $("#txtJugada").val(listaAcciones[accionActiva].accion);  
        if (listaAcciones[accionActiva].cuadrante == ""){ 
            $("#pelota").css({'visibility': 'hidden'});
        }
        else{
            cuadrante = document.getElementById(listaAcciones[accionActiva].cuadrante);
            if (cuadrante != null){
                pelota = document.getElementById("pelota");
                src = pelota.src;
                padre = pelota.parentNode;
                
                padre.removeChild(pelota);
                icoPelota = document.createElement('img');
                icoPelota.id = "pelota";
                icoPelota.src = src;
                
                cuadrante.appendChild(icoPelota);
                $("#pelota").css({'visibility': 'visible'});
            }
        }
            
    } 
    
}


 function cargarAcciones(url){

        $.getJSON(url, function(data){
            accionActiva = 0;
            for ( var i=0, len= data.length; i< len;i++){
                var obj = data[i];
                if (obj.accion == "") continue;
                acc = new accion(obj.id, obj.minuto, obj.accion, obj.cuadrante);                
                listaAcciones.push(acc);
            }
            numAcciones = listaAcciones.length;   
            actualizarAccion();
        });     
    
    }
    
  function jugadaAnterior(){
      
      accionActiva = accionActiva - 1;
      if (accionActiva < 0) accionActiva = numAcciones - 1;
      actualizarAccion();
      
  }
  
  function jugadaSiguiente(){
      
      accionActiva = accionActiva + 1;
      if (accionActiva > numAcciones - 1) accionActiva = 0;
      actualizarAccion();
      
  }
    
$.extend({
  getUrlVars: function(){
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for(var i = 0; i < hashes.length; i++)
    {
      hash = hashes[i].split('=');
      vars.push(hash[0]);
      vars[hash[0]] = hash[1];
    }
    return vars;
  },
  getUrlVar: function(name){
    return $.getUrlVars()[name];
  }
});
 
 
// Getting URL var by its nam
var idPartido = $.getUrlVar('id');

      

$(document).ready(function(){
	
        // url servicio json
        url = $("#root").val() + "/json/cronicaFutbol8?partido=" + idPartido;
        cargarAcciones(url);
        
        // Acciones
        $("#jugadaAnterior").click(function(){
            jugadaAnterior();
        });
        $("#jugadaSiguiente").click(function(){
            jugadaSiguiente();
	});

});

