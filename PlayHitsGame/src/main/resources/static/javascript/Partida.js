/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */


let serverWS = document.getElementById("serverWS").value;
let dirSocket = "ws:" + serverWS + "/websocket";
let socket = new WebSocket(dirSocket);

socket.onmessage = function (event) {
    if (event.data === '#nueva#')
        location.reload();
    if (event.data === '#acabar#') {
        location.reload(); // ahora hacemos lo mismo, pero se deja por si hubiese otra cosa que hacer
    }
    let mensajesSockets = document.getElementById("mensajesSockets");
    let usuarios = event.data.split(",");
    let txtHtml = '';
    for (let i = 0; i < usuarios.length; i++) {
        txtHtml = txtHtml + '<p>' + usuarios[i] + '</p>';
    }
    mensajesSockets.innerHTML = txtHtml;
};

function txtMensaje(op, idCancion, anyo) {

    let userId = document.getElementById("idusuario").value;
    let partidaId = document.getElementById("idpartida").value;
    let mensaje = "{'op' : '" + op + "',";
    mensaje = mensaje + "'idUsuario' : " + userId + ",";
    mensaje = mensaje + "'idPartida' : " + partidaId;
    if (idCancion !== null) {
        mensaje = mensaje + ",'idCancion' : " + idCancion;
    }
    if (anyo !== null) {
        mensaje = mensaje + ",'anyo' : " + anyo;
    }
    mensaje = mensaje + "}";
    //window.alert(mensaje);
    return mensaje;
}

var botonAnyo = false;
var botonTitulo = false;
var botonInterprete = false;

async function sleep(t){
    await new Promise(resolve => setTimeout(resolve, t));
}                                    

// Hacemos el alta para dar de alta el usuario el el WS
// y hacer la consulta de BD en el momento de carga de 
// la pagina

async function enviarMensaje(txt) {
    let intentos = 3;

    for (var i = 1; i < intentos; i++) {
        try {
            socket.send(txt);
            break;
        } catch (e) {
            window.alert("Temporal para quitar!!!, problema evio a socket: " + e.toString());
            if (i === intentos){
                window.alert("No hay conexion al socket " + dirSocket +
                        " despues de " + i.toString() + " intentos");
            }
            await sleep(1000);// Esperamos 1 seg
        }
    }
}

window.addEventListener('load', function () {
    enviarMensaje(txtMensaje("alta", null, null));
});

function respuestaTitulo(codCancion) {
    if (botonTitulo === false) {
        enviarMensaje(txtMensaje("titulo", codCancion, null));
        let boton = document.getElementById("titulo_" + codCancion);
        boton.style.backgroundColor = "green";
        botonTitulo = true;
    }
}

function respuestaInterprete(codCancion) {
    if (botonInterprete === false) {
        enviarMensaje(txtMensaje("interprete", codCancion, null));
        let boton = document.getElementById("interprete_" + codCancion);
        boton.style.backgroundColor = "green";
        botonInterprete = true;
    }
}

function respuestaAnyo() {
    if (botonAnyo === false) {
        let anyo = document.getElementById("anyo");
        let boton = document.getElementById("botonAnyo");
        if (anyo.value === "" || anyo.value === 0)
            window.alert("Se ha de informar un año");
        else {
            enviarMensaje(txtMensaje("anyo", null, anyo.value));
            boton.style.backgroundColor = "green";
            botonAnyo = true;
        }
    }
}          