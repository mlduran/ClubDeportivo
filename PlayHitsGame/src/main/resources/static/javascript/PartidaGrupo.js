/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */


let serverWS = document.getElementById("serverWS").value;
let dirSocket = serverWS + "/websocket";
let userId = document.getElementById("idusuario").value;
let partidaId = document.getElementById("idpartida").value;
let mostrarPlayer = document.getElementById("mostrarPlayer");
let activarPlay = document.getElementById("activarPlay");
let rol = document.getElementById("rol");
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
        txt = txt + "El jugador mas rapido acertando el interprete ha sido " + datosJson["primeroInterprete"] + "\n";
    if (datosJson["primeroAnyo"] !== undefined && datosJson["primeroAnyo"] !== "")
        txt = txt + "El jugador mas rapido acertando el año ha sido " + datosJson["primeroAnyo"];

    return txt;
};

var procesar = function (mensaje) {

    var txt;

    if (mensaje === null)
        return;
    dataJson = JSON.parse(mensaje.body);
    if (dataJson === null || dataJson === "")
        return;
    if (dataJson["op"] === "playerOFF" || dataJson["op"] === "playerON") {
        tratarVisualizarPlayer(dataJson["op"]);
    }
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
            txtHtml = '<p>' + dataJson["usuarios"] + '</p>';            
            mensajesSockets.innerHTML = txtHtml;
        }
    }
};

var botonAnyo = false;
var botonTitulo = false;
var botonInterprete = false;

function txtMensaje(op, valor) {

    let mensaje = "{'op' : '" + op + "',";
    mensaje = mensaje + "'idUsuario' : " + userId + ",";
    mensaje = mensaje + "'idPartida' : " + partidaId;
    mensaje = mensaje + ",'valor' : " + valor;
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

    let avisoErrorWS = true;
    stompClient.onWebSocketError = (error) => {
        console.error('Error websocket ', error);
        if (avisoErrorWS) {
            window.alert("No hay conexion al socket " + dirSocket);
            avisoErrorWS = false;
        }
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
async function incializar() {
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

    if (rol.value === "master") {
        mostrarPlayer.style.display = 'block';
    } else {
        if (activarPlay.value === 'true')
            mostrarPlayer.style.display = 'block';
        else
            mostrarPlayer.style.display = 'none';
    }

}

document.addEventListener("DOMContentLoaded", inicializar);
window.addEventListener('load', incializar);
window.addEventListener('unload', desconectar);

function forzarAcabarRonda() {
    sendmensaje(txtMensaje("acabaronda", null, null));
}

function respuestaTitulo(codCancion) {
    if (botonTitulo === false) {
        sendmensaje(txtMensaje("titulo", codCancion));
        let boton = document.getElementById("titulo_" + codCancion);
        boton.style.backgroundColor = "green";
        botonTitulo = true;
    }
}

function respuestaInterprete(codCancion) {
    if (botonInterprete === false) {
        sendmensaje(txtMensaje("interprete", codCancion));
        let boton = document.getElementById("interprete_" + codCancion);
        boton.style.backgroundColor = "green";
        botonInterprete = true;
    }
}

function respuestaAnyo(anyo) {
    if (botonAnyo === false) {
        sendmensaje(txtMensaje("anyo", anyo));
        let boton = document.getElementById("anyo_" + anyo);
        boton.style.backgroundColor = "green";
        botonAnyo = true;
    }
}

function activarDesactivarPlay() {

    if (reproductor.paused) {
        reproductor.play(); // Reproducir el audio si está pausado
    } else {
        reproductor.pause(); // Pausar el audio si está reproduciéndose
    }

}

function activarPlayer_on_off() {

    var ico = document.getElementById('playerOnOff');

    if (activarPlay.value === 'true') {
        activarPlay.value = false;
        ico.src = ico.src.replace("Abierto", "Cerrado");
        sendmensaje(txtMensaje("playerOFF", null, null));
    } else {
        activarPlay.value = 'true';
        ico.src = ico.src.replace("Cerrado", "Abierto");
        sendmensaje(txtMensaje("playerON", null, null));
    }
}

function tratarVisualizarPlayer(valor) {

    if (rol.value === "invitado") {
        if (valor === "playerOFF") {
            activarPlay.value = false;
            mostrarPlayer.style.display = 'none';
        }
        if (valor === "playerON") {
            activarPlay.value = true;
            mostrarPlayer.style.display = 'block';
        }
    }
}

document.addEventListener("keydown", function (event) {
    // Verificar si la tecla presionada es la barra espaciadora (código 32)
    if (event.code === "Space") {
        event.preventDefault(); // Evitar el scroll al presionar la barra espaciadora
        if (activarPlay.value === 'true') 
            activarDesactivarPlay();
    }
});

let lastTouchTime = 0;
const doubleTapDelay = 1000; // 1 segundo

document.addEventListener("touchstart", function (event) {
    // Al tocar la pantalla inicio el play
    
    if (activarPlay.value === 'false')
        return;
    
    event.preventDefault();
    const currentTime = new Date().getTime(); // Tiempo actual
    const timeSinceLastTouch = currentTime - lastTouchTime; // Diferencia entre el toque actual y el anterior

    // Si el tiempo entre toques es menor a 1 segundo, se considera un doble toque
    if (timeSinceLastTouch < doubleTapDelay && timeSinceLastTouch > 0) {
        activarDesactivarPlay();
    }

    // Actualizar el tiempo del último toque
    lastTouchTime = currentTime;
});