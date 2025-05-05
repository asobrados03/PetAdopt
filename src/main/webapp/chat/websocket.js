var wsUri = 'ws://' + document.location.host 
    + document.location.pathname.substr(0, document.location.pathname.indexOf("/faces")) 
    + '/websocket';
console.log(wsUri);

var websocket; // Declaramos el WebSocket fuera para poder crear uno nuevo cuando el usuario se conecte de nuevo
var textField = document.getElementById("texto");
var users = document.getElementById("users");
var chatlog = document.getElementById("chatlog");
var output = document.getElementById("output");
var username;

function join() {
    username = textField.value;
    
    // Crear un nuevo WebSocket
    websocket = new WebSocket(wsUri);
    
    websocket.onopen = function(evt) {
        writeToScreen("CONNECTED");
        websocket.send(username + " joined");
        document.getElementById("unirse").style.setProperty("visibility", "hidden");
        document.getElementById("unirse").disabled = true;
        document.getElementById("enviar").style.removeProperty("visibility");
        document.getElementById("desconectar").style.removeProperty("visibility");
        textField.value = "";
    };

    websocket.onclose = function(evt) {
        writeToScreen("DISCONNECTED");
    };

    websocket.onmessage = function(evt) {
        writeToScreen("RECEIVED: " + evt.data);

        if (evt.data.indexOf("joined") !== -1) {
            users.innerHTML += evt.data.substring(0, evt.data.indexOf("joined")) + "\n";
        } else {
            chatlog.innerHTML += evt.data + "\n";
        }
    };

    websocket.onerror = function(evt) {
        writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
    };
}

function send_message() {
    const message = textField.value;

    // Validar que el mensaje no esté vacío ni contenga solo espacios
    if (!message || message.trim() === "") {
        alert("No puedes enviar un mensaje vacío.");
        return; // Detener si el mensaje no es válido
    }

    if (websocket && websocket.readyState === WebSocket.OPEN) {
        websocket.send(username + ": " + message.trim()); // Enviar el mensaje limpio
        textField.value = ""; // Limpiar el campo después de enviar
    }
}

function disconnect() {
    // Cierra el WebSocket
    if (websocket && websocket.readyState === WebSocket.OPEN) {
        websocket.close();
    }

    // Ocultar los botones de "enviar" y "desconectar"
    document.getElementById("unirse").style.visibility = "visible";
     document.getElementById("unirse").disabled = false;
    document.getElementById("enviar").style.visibility = "hidden";
    document.getElementById("desconectar").style.visibility = "hidden";

    // Limpiar el campo de texto para que el usuario pueda escribir su nombre de nuevo
    textField.value = username;
}

function writeToScreen(message) {
    var pre = document.createElement("p");
    pre.style.wordWrap = "break-word";
    pre.innerHTML = message;
}
