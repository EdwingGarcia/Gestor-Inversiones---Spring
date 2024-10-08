document.addEventListener('DOMContentLoaded', function () {
    // Función para actualizar la vista previa de la imagen
    function actualizarVistaPrevia(inputId, previewId) {
        const input = document.getElementById(inputId);
        const imgPreview = document.getElementById(previewId);
        const modal = document.getElementById("myModal");
        const expandedImg = document.getElementById("expandedImg");
        const closeModal = document.getElementById("closeModal");

        // Añadir un evento para actualizar la previsualización cuando cambie el input
        input.addEventListener('input', function () {
            const enlaceImagen = input.value;
            imgPreview.innerHTML = ''; // Limpiar la vista previa anterior

            if (enlaceImagen) {
                const img = document.createElement('img');
                img.src = enlaceImagen;
                img.alt = 'Vista Previa';
                img.onerror = function () {
                    imgPreview.innerHTML = 'No se pudo cargar la imagen. Verifica que la URL sea correcta.';
                };
                imgPreview.appendChild(img);

                // Añadir evento para abrir el modal al hacer clic en la imagen
                img.onclick = function() {
                    expandedImg.src = enlaceImagen; // Establecer la fuente de la imagen expandida
                    modal.style.display = "block"; // Mostrar el modal
                };
            }
        });

        // Cerrar el modal al hacer clic en el botón de cerrar
        closeModal.onclick = function() {
            modal.style.display = "none"; // Ocultar el modal
        };

        // Cerrar el modal al hacer clic fuera de la imagen
        window.onclick = function(event) {
            if (event.target === modal) {
                modal.style.display = "none"; // Ocultar el modal
            }
        };
    }

    // Llamar a la función para cada timeframe
    actualizarVistaPrevia('enlaceImagen4h', 'imgPreview4h');
    actualizarVistaPrevia('enlaceImagen1h', 'imgPreview1h');
    actualizarVistaPrevia('enlaceImagen15m', 'imgPreview15m');
    actualizarVistaPrevia('enlaceImagen5m', 'imgPreview5m');
    actualizarVistaPrevia('enlaceImagen1m', 'imgPreview1m');
});
