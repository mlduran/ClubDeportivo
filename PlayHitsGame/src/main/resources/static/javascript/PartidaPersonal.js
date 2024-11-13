/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */

let userId = document.getElementById("idusuario").value;
let partidaId = document.getElementById("idpartida").value;
let formResp = document.getElementById("respuestas");


var botonAnyo = false;
var botonTitulo = false;
var botonInterprete = false;
var respAnyo = document.getElementById("anyo");
var respTitulo = document.getElementById("titulo");
var respInterprete = document.getElementById("interprete");

var verificacion = document.getElementById('verificacion');
verificacion.style.display = 'table-cell';
var respOk = document.getElementById('respuestaOK');
var todoFallo = document.getElementById('todoFallo');
var esRecord = document.getElementById('esRecord');
var touchstart = document.getElementById("touchstart").value;

var reproductor = document.getElementById("reproductor");
var reproductorConsulta = document.getElementById("reproductorConsulta");

async function enviarRespuestas() {

    if (botonAnyo === true && botonTitulo === true && botonInterprete === true) {
        formResp.submit();
    }
}

function respuestaTitulo(codCancion) {
    if (botonTitulo === false) {
        respTitulo.value = codCancion;
        let boton = document.getElementById("titulo_" + codCancion);
        boton.style.backgroundColor = "green";
        botonTitulo = true;
        enviarRespuestas();
    }
}

function respuestaInterprete(codCancion) {
    if (botonInterprete === false) {
        respInterprete.value = codCancion;
        let boton = document.getElementById("interprete_" + codCancion);
        boton.style.backgroundColor = "green";
        botonInterprete = true;
        enviarRespuestas();
    }
}

function respuestaAnyo(anyo) {
    if (botonAnyo === false) {
        respAnyo.value = anyo;
        let boton = document.getElementById("anyo_" + anyo);
        boton.style.backgroundColor = "green";
        botonAnyo = true;
        enviarRespuestas();
    }
}

function esperar(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

async function ocultarResultado() {

    if (respOk.value === 'true')
        await esperar(2000);
    else
        await esperar(5000);
    verificacion.style.display = 'none';
}

window.addEventListener('load', function () {

    if (respOk.value === 'true') {
        verificacion.style.color = "white";
        verificacion.style.backgroundColor = "lightgreen";
    } else if (respOk.value === 'false') {
        verificacion.style.color = "white";
        verificacion.style.backgroundColor = "orange";
    }
    if (todoFallo.value === 'false')
        ocultarResultado();
    if (esRecord.value === 'true')
        window.alert(document.getElementById("txtrecordtema").value);

});

function activarDesactivarPlay() {

    if (reproductor.paused) {
        reproductor.play(); // Reproducir el audio si está pausado
    } else {
        reproductor.pause(); // Pausar el audio si está reproduciéndose
    }

}

var reproductorActivo = false;
function reproducir(cancion) {


    reproductorConsulta.src = cancion;

    if (reproductorActivo) {
        reproductorConsulta.pause(); // Pausar el audio si está reproduciéndose
        reproductorActivo = false;
    } else {
        reproductorConsulta.play(); // Reproducir el audio si está pausado
        reproductorActivo = true;
    }

}

document.addEventListener("keydown", function (event) {
    // Verificar si la tecla presionada es la barra espaciadora (código 32)
    if (event.code === "Space") {
        event.preventDefault(); // Evitar el scroll al presionar la barra espaciadora

        activarDesactivarPlay();
    }
});

let lastTouchTime = 0;
const doubleTapDelay = 500; // medio segundo

if (touchstart === 'true') {
    document.addEventListener("touchstart", function (event) {
        // Al tocar la pantalla inicio el play
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
}


// Obtenemos el elemento HTML donde se mostrará el contador
const secondsCounter = document.getElementById("contador");
let secondsElapsed = secondsCounter.value;

// Creamos el intervalo que incrementa el contador cada segundo (1000 ms)
const intervalId = setInterval(() => {
    // Incrementamos el número de segundos
    if (todoFallo.value === 'false') {
        secondsElapsed++;

        // Actualizamos el contenido del elemento HTML
        if (secondsElapsed > 0)
            secondsCounter.value = secondsElapsed;
        else
            secondsCounter.value = "... " + Math.abs(secondsElapsed);
    }else{
        secondsCounter.value = "";
    }
    
}, 1000);
