// Función para manejar la acción de pegar (Ctrl + V)
document.addEventListener('paste', function(event) {
    let items = (event.clipboardData || event.originalEvent.clipboardData).items;

    for (let index in items) {
        let item = items[index];
        if (item.kind === 'file') {
            let blob = item.getAsFile();
            let reader = new FileReader();

            reader.onload = function(event) {
                let activeTimeframe = getActiveTimeframe(); // Función para obtener el timeframe activo
                if (activeTimeframe) {
                    let imgElement = document.createElement('img');
                    imgElement.src = event.target.result;
                    imgElement.style.width = '100%'; // Ajusta la imagen al 100% del contenedor
                    imgElement.style.maxHeight = '100%'; // Para ajustar la altura máxima según el tamaño del contenedor
                    imgElement.style.objectFit = 'cover'; // Para que la imagen se ajuste al tamaño del contenedor
                    document.getElementById(activeTimeframe).innerHTML = ''; // Limpia cualquier imagen previa
                    document.getElementById(activeTimeframe).appendChild(imgElement);
                }
            };

            reader.readAsDataURL(blob);
        }
    }
});

// Función para obtener el timeframe activo (determinado por cuál área de texto tiene el foco)
function getActiveTimeframe() {
    if (document.activeElement.id === 'observacion4h') return 'imgPreview4h';
    if (document.activeElement.id === 'observacion1h') return 'imgPreview1h';
    if (document.activeElement.id === 'observacion15m') return 'imgPreview15m';
    if (document.activeElement.id === 'observacion5m') return 'imgPreview5m';
    if (document.activeElement.id === 'observacion1m') return 'imgPreview1m';
    return null;
}

// Agregar eventos para botones de eliminar para cada timeframe
document.getElementById('removeImg4h').addEventListener('click', function() {
    document.getElementById('imgPreview4h').innerHTML = ''; // Elimina la imagen de 4H
});

document.getElementById('removeImg1h').addEventListener('click', function() {
    document.getElementById('imgPreview1h').innerHTML = ''; // Elimina la imagen de 1H
});

document.getElementById('removeImg15m').addEventListener('click', function() {
    document.getElementById('imgPreview15m').innerHTML = ''; // Elimina la imagen de 15M
});

document.getElementById('removeImg5m').addEventListener('click', function() {
    document.getElementById('imgPreview5m').innerHTML = ''; // Elimina la imagen de 5M
});

document.getElementById('removeImg1m').addEventListener('click', function() {
    document.getElementById('imgPreview1m').innerHTML = ''; // Elimina la imagen de 1M
});
