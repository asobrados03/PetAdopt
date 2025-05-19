var wsUri = 'ws://' + document.location.host 
    + document.location.pathname.substr(0, document.location.pathname.indexOf("/faces")) 
    + '/websocket';
console.log(wsUri);

var websocket; // Declaramos el WebSocket fuera para poder crear uno nuevo cuando el usuario se conecte de nuevo
var textField = document.getElementById("texto");
var users = document.getElementById("users");
var chatlog = document.getElementById("chatlog");
var output = document.getElementById("output");
var hidden = document.getElementById("hiddenField");
var username;

function join() {
    username = hidden.value;
    
    // Crear un nuevo WebSocket
    websocket = new WebSocket(wsUri);
    
    websocket.onopen = function(evt) {
        writeToScreen("CONNECTED");
        websocket.send(username + " joined");
        document.getElementById("unirse").style.setProperty("visibility", "hidden");
        document.getElementById("unirse").disabled = true;
        document.getElementById("texto").style.removeProperty("visibility");
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
        return; 
    }

    if (websocket && websocket.readyState === WebSocket.OPEN) {
        websocket.send(username + ": " + message.trim()); 
        textField.value = "";
    }
}

function disconnect() {
    // Cierra el WebSocket
    if (websocket && websocket.readyState === WebSocket.OPEN) {
        websocket.close();
    }

    // Ocultar opciones
    document.getElementById("unirse").style.visibility = "visible";
    document.getElementById("unirse").disabled = false;
    document.getElementById("enviar").style.visibility = "hidden";
    document.getElementById("texto").style.visibility = "hidden";
    document.getElementById("desconectar").style.visibility = "hidden";
    
    textField.value = "";
}

function writeToScreen(message) {
    var pre = document.createElement("p");
    pre.style.wordWrap = "break-word";
    pre.innerHTML = message;
}
