/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */


function actualizarSeleccion() {
    const listaTemas = document.getElementById("listaTemas");
    const campoTema = document.getElementById("tema");

    // Obtener los temas seleccionados en la lista
    const temasSeleccionados = Array.from(listaTemas.selectedOptions).map(option => option.value);

    // Si no hay ninguna selección, restaurar el valor por defecto (PlayHitsGame)
    if (temasSeleccionados.length === 0) {
        for (let option of listaTemas.options) {
            if (option.value === "PlayHitsGame") {
                option.selected = true;
                break;
            }
        }
    }

    // Actualizar el campo oculto con los temas seleccionados
    campoTema.value = Array.from(listaTemas.selectedOptions)
            .map(option => option.value)
            .join(", ");
}

// Inicializar el campo oculto con el tema predeterminado al cargar la página
document.addEventListener("DOMContentLoaded", () => {
    const listaTemas = document.getElementById("listaTemas");
    const campoTema = document.getElementById("tema");

    // Asegurarse de que PlayHitsGame esté seleccionado por defecto
    for (let option of listaTemas.options) {
        if (option.value === "PlayHitsGame") {
            option.selected = true;
            break;
        }
    }

    // Guardar la selección inicial en el campo oculto
    actualizarSeleccion();
});