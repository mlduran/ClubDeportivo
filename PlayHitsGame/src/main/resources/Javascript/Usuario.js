/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */

let p1 = document.getElementById("pws1").value;
let p2 = document.getElementById("pws2").value;

p2.addEventListener("change", (event) => {
  // ${event.target.value}`;
  validarPasswd();
});

 function validarPasswd () {   
  
  let espacios = false;
  let cont = 0;

  // Este bucle recorre la cadena para comprobar
  // que no todo son espacios
    while (!espacios && (cont < p1.length)) {
            if (p1.charAt(cont) === " ")
                    espacios = true;
            cont++;
    }
   
  if (espacios) 
   alert ("La contraseña no puede contener espacios en blanco");
   
  if (p1.length === 0 || p2.length === 0) 
   alert("Los campos de la password no pueden quedar vacios");
   
  if (p1 !== p2) 
   alert("Las passwords deben de coincidir");
 
 }
