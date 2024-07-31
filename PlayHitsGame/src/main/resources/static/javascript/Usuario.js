/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */



function validarPasswd() {
    let p1 = document.getElementById("pws1");
    let p2 = document.getElementById("pws2");
    let f = document.getElementById("form");

    let espacios = false;
    let cont = 0;

// Este bucle recorre la cadena para comprobar
// que no todo son espacios
    while (!espacios && (cont < p1.value.length)) {
        if (p1.value.charAt(cont) === " ")
            espacios = true;
        cont++;
    }

    if (espacios)
        alert("La contraseña no puede contener espacios en blanco");
    else if (p1.value.length === 0 || p2.value.length === 0)
        alert("Los campos de la password no pueden quedar vacios");
    else if (p1.value !== p2.value)
        alert("Las passwords deben de coincidir");
    else
        f.submit();

}


function hacerLogin() {
    
    var form = document.getElementById("login");
    var formData = new FormData(form);
    var data = new URLSearchParams(formData).toString();

    fetch("/login", {
        method: "POST",
        headers: {
            "Authorization": "Bearer 313214gfdgfd4324324",
            "Content-Type": "application/x-www-form-urlencoded"            
        },
        body: data
    })
            .then(response => response.json())
            .then(data => console.log(data))
            .catch(error => console.error('Error:', error));

}
