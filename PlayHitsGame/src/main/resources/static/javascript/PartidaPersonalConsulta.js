/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */



window.addEventListener('load', function () {

    if (respOk.value === 'true') {
        verificacion.style.color = "white";
        verificacion.style.backgroundColor = "lightgreen";
    } else if (respOk.value === 'false') {
        verificacion.style.color = "white";
        verificacion.style.backgroundColor = "orange";
    }
});

