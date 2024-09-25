/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */


var reproductor = document.getElementById("reproductor");
var reproductorActivo = false;
function reproducir(cancion){
    
    reproductor.src = cancion;
    
    if (reproductorActivo) {
        reproductor.pause(); // Pausar el audio si está reproduciéndose
        reproductorActivo = false;
    } else {        
        reproductor.play(); // Reproducir el audio si está pausado
        reproductorActivo = true;
    }
    
}