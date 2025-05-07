/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var todos = true;


var tratarVisualizacion = function(cambiar){
    
    if (cambiar){
        if (todos == true){
            todos = false;
            $("#soloMisPartidos").val("Todos los Partidos");
        } 
        else{
            todos = true;
            $("#soloMisPartidos").val("Solo Mis Partidos");
        }
    }
    
    nombreEquipo = $('#nombreEquipo').val();
    
    for (i = 0; i < $('.partido').length; i++){
        td = $('.partido')[i];
        tr = td.parentNode;
        if (nombreEquipo != null && nombreEquipo != ""){
            if (td.innerHTML.indexOf(nombreEquipo) > -1){
                // Es el equipo
                tr.style.backgroundColor = "lightblue";
            }
            else{
                // No es el equipo
                if (!todos)
                    tr.style.display = "none";
                else
                    tr.style.display = "";
                tr.style.backgroundColor = "blanchedalmond";
            }
        } 
    }
    
    
}

$(document).ready(function(){
	
        
        // Relleno alineacion para servidor
        $("#soloMisPartidos").click(function(){
            tratarVisualizacion(true);
        });
        
        tratarVisualizacion(false);
      

});