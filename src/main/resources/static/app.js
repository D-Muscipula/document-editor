const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/gs-guide-websocket'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/greetings', (greeting) => {
        showGreeting(JSON.parse(greeting.body).content);
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#editor").prop("disabled", !connected);
    if (connected) {
        $("#editor").show();
    } else {
        $("#editor").hide();
    }
}

function connect() {
    stompClient.activate();
}

function sendTextUpdate(text) {
    stompClient.publish({
        destination: "/app/hello",
        body: JSON.stringify({'name': text})
    });
}

function showGreeting(content) {
    $("#editor").val(content);
}

$(document).ready(function() {
    connect();

    // Отправлять обновления при каждом изменении текста
    $("#editor").on('input', function() {
        sendTextUpdate($(this).val());
    });
});