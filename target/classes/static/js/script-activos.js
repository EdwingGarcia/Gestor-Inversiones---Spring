document.addEventListener("DOMContentLoaded", function() {
    // Obtener el selector del tipo y el selector de activos
    const tipoSelect = document.getElementById("tipo");
    const activoSelect = document.getElementById("activo");

    tipoSelect.addEventListener("change", function() {
        // Obtener el tipo seleccionado
        const tipoSeleccionado = tipoSelect.value;

        // Limpiar el campo de activos
        activoSelect.innerHTML = '<option value="" disabled selected>Selecciona un Activo</option>';

        // Si se seleccionó un tipo, hacer la llamada AJAX
        if (tipoSeleccionado) {
            fetch(`/inversiones/activos/${tipoSeleccionado}`)
                .then(response => response.json())
                .then(data => {
                    // Agregar los activos obtenidos al select de activos
                    data.forEach(activo => {
                        const option = document.createElement("option");
                        option.value = activo.simbolo;
                        option.textContent = activo.simbolo;
                        activoSelect.appendChild(option);
                    });
                })
                .catch(error => console.error('Error:', error));
        }
    });
});
