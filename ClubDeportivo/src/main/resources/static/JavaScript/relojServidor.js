/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */


var tick, anyo, mes, dia, hor, min, seg;

var ut;
window.onload = iniciarReloj();


function iniciarReloj() {

    stringHora = document.getElementById('fechaServer').value;
    datos = stringHora.split("-");

    dia = datos[0];
    mes = datos[1];
    anyo = datos[2];
    hor = datos[3];
    min = datos[4];
    seg = datos[5];

    ut = new Date(anyo, mes, dia, hor, min, seg);

    relojServidor();

}

var tiempoGrafico = 15;
var tiempoGraficoActual = 15;
var numeroImagenes = 6;
var imagenActual = 1;

var imagenes = function (numero) {

    if (numero === 1)
        return "prepararPartidoFutbol8.png";
    else if (numero === 2)
        return "informePartidoFutbol8.png";
    else if (numero === 3)
        return "visualizarPartidoFutbol8.png";
    else if (numero === 4)
        return "plantilla.png";
    else if (numero === 5)
        return "quiniela.png";
    else if (numero === 6)
        return "grafico.png";

};

var textos = function (numero) {

    if (numero === 1)
        return "En Futbol8 puedes preparar tu partido eligiendo diferentes alineaciones, tacticas y estrategias y realizando realizando simulaciones para ver los diferentes; resultados";
    else if (numero === 2)
        return "En Futbol8 tendras un informe detallado de los acontecido en el partido para poder analizar tu resultado y las posibilidades reales de victoria que tenias";
    else if (numero === 3)
        return "En Futbol8 podras analizar en un grafico las acciones realizadas durante el partido ademas de ver su cronica";
    else if (numero === 4)
        return "En Futbol8 dispones de una plantilla de un maximo de 16 jugadores que tendras que entrenar y gestionar de la mejor manera posible para sacar su máximo rendimiento";
    else if (numero === 5)
        return "En la Quiniela puedes realizar 2 columnas de apuestas para obtener los maximos aciertos y puntos y conseguir ser el mejor pronosticador";
    else if (numero === 6)
        return "Dispones de diferentes graficos que te ayudaran a realizar una mejor gestion de tu Club";

};

function relojServidor() {

    ut.setSeconds(ut.getSeconds() + 1);

    modImagen = ut.getSeconds() + 1;

    var h, m, s;
    var time = " ";
    h = ut.getHours();
    m = ut.getMinutes();
    s = ut.getSeconds();

    if (s <= 9)
        s = "0" + s;
    if (m <= 9)
        m = "0" + m;
    if (h <= 9)
        h = "0" + h;
    time += h + ":" + m + ":" + s;
    document.getElementById('reloj').value = time;

    tiempoGraficoActual = tiempoGraficoActual - 1;
    if (tiempoGraficoActual === 0) {
        // aqui cambio de imagen texto
        imag = document.getElementById('imagenPresentacion').src;
        partes = imag.split("/");
        ruta = "";
        for (i = 0; i < partes.length - 1; i++) {
            ruta = ruta + partes[i] + "/";
        }
        newImag = ruta + imagenes(imagenActual);
        document.getElementById('imagenPresentacion').src = newImag;
        document.getElementById('textoPresentacion').innerHTML = textos(imagenActual);

        imagenActual = imagenActual + 1;
        if (imagenActual > numeroImagenes)
            imagenActual = 1;

        tiempoGraficoActual = tiempoGrafico;
    }

    tick = setTimeout("relojServidor()", 1000);
}
