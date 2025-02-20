/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */


// Función para cargar y mostrar el archivo HTML en el div
function loadHTML() {
    var fichAjuda = document.getElementById('fichAjuda').value;
    fetch(fichAjuda)
            .then(response => response.text())
            .then(data => {
                document.getElementById('info').innerHTML = data;
            })
            .catch(error => {
                console.error('Error al cargar el archivo HTML:', error);
            });
}

// Llama a la función para cargar el contenido al cargar la página
window.onload = loadHTML;

function handleCrearPartida(event, usuariosGrupo, href) {
    if (usuariosGrupo === false) {
        // Mostrar una alerta con el mensaje
        alert(document.getElementById('txtSinUsuarios').value);
        event.preventDefault(); // Evitar la navegación al enlace
    } else {
        // Si no hay error, proceder con la navegación
        window.location.href = href;
    }
}