/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */


let serverWS = document.getElementById("serverWS").value;
let dirSocket = "ws:" + serverWS + "/websocket";
let userId = document.getElementById("idusuario").value;
let partidaId = document.getElementById("idpartida").value;
var stompClient = new StompJs.Client({
    brokerURL: dirSocket
});


function sendmensaje(mensaje) {
    stompClient.publish({
        destination: '/app/partida/' + partidaId + '/usuario/' + userId,
        body: mensaje});
}

var txtPrimeros = function (datosJson) {

    var txt = "";
    if (datosJson["primeroCancion"] !== undefined && datosJson["primeroCancion"] !== "")
        txt = txt + "El jugador mas rapido acertando la cancion ha sido " + datosJson["primeroCancion"] + "\n";
    if (datosJson["primeroInterprete"] !== undefined && datosJson["primeroInterprete"] !== "")
        txt = txt + "El jugador mas rapido acertando el interprete ha sido " + datosJson["primeroInterprete"];

    return txt;
};

var procesar = function (mensaje) {

    var txt;

    if (mensaje === null)
        return;
    dataJson = JSON.parse(mensaje.body);
    if (dataJson === null || dataJson === "")
        return;
    if (dataJson["op"] === "nueva") {
        txt = txtPrimeros(dataJson);
        if (txt !== undefined && txt !== "")
            window.alert(txt);
        location.reload();
    }
    if (dataJson["op"] === "acabar") {
        txt = txtPrimeros(dataJson);
        if (txt !== undefined && txt !== "")
            window.alert(txt);
        location.href = '/partidaConsultaGrupo/' + dataJson["idPartida"];
        //location.reload(); // ahora hacemos lo mismo, pero se deja por si hubiese otra cosa que hacer
    }
    if (dataJson["op"] === "respuestas") {
        if (dataJson["usuarios"] !== undefined) {
            let mensajesSockets = document.getElementById("mensajesSockets");
            let usuarios = dataJson["usuarios"].split(",");
            let txtHtml = '';
            for (let i = 0; i < usuarios.length; i++)
                txtHtml = txtHtml + '<p>' + usuarios[i] + '</p>';
            mensajesSockets.innerHTML = txtHtml;
        }
    }
};

var botonAnyo = false;
var botonTitulo = false;
var botonInterprete = false;

function txtMensaje(op, idCancion, anyo) {

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

function inicializar() {
    stompClient.activate();
    //stompClient.deactivate();
    stompClient.onConnect = (frame) => {

        console.log('Connected: ' + frame);
        stompClient.subscribe('/tema/partida/' + partidaId, procesar);
    };

    window.addEventListener('load', function () {
        //sendmensaje(txtMensaje("alta", null, null));
    });

    stompClient.onWebSocketError = (error) => {
        console.error('Error websocket ', error);
    };

    stompClient.onStompError = (frame) => {
        console.error('Broker reported error: ' + frame.headers['ḿessage']);
        console.error('Additional details: ' + frame.body);
    };

}
;

function desconectar() {
    stompClient.deactivate();
}
;

async function sleep(t) {
    await new Promise(resolve => setTimeout(resolve, t));
}

// Hacemos el alta para dar de alta el usuario el el WS
// y hacer la consulta de BD en el momento de carga de 
// la pagina
async function altaWS() {
    // si no hacemos esto el usuario no se registra y por ejemplo
    // el ultimo no se tendria en cuenta
    let intentos = 10;

    for (var i = 1; i < intentos; i++) {
        try {
            sendmensaje(txtMensaje("alta", null, null));
            break;
        } catch (e) {
            if (i === intentos) {
                window.alert("No hay conexion al socket " + dirSocket +
                        " despues de " + i.toString() + " intentos");
            }
            await sleep(1000);// Esperamos 1 seg        
        }
    }
    ;
}

document.addEventListener("DOMContentLoaded", inicializar);
window.addEventListener('load', altaWS);
window.addEventListener('unload', desconectar);

function forzarAcabarRonda() {
    sendmensaje(txtMensaje("acabaronda", null, null));
}

function respuestaTitulo(codCancion) {
    if (botonTitulo === false) {
        sendmensaje(txtMensaje("titulo", codCancion, null));
        let boton = document.getElementById("titulo_" + codCancion);
        boton.style.backgroundColor = "green";
        botonTitulo = true;
    }
}

function respuestaInterprete(codCancion) {
    if (botonInterprete === false) {
        sendmensaje(txtMensaje("interprete", codCancion, null));
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
            sendmensaje(txtMensaje("anyo", null, anyo.value));
            boton.style.backgroundColor = "green";
            botonAnyo = true;
        }
    }
}

