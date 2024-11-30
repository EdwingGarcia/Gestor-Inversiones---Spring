     window.onload = function() {
            const today = new Date().toISOString().split('T')[0];
            document.getElementById('fechaInversion').value = today;
        }