var quill = new Quill('#editor-container', {
  modules: {
    toolbar: '#toolbar'
  },
  theme: 'snow'
});

const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/gs-guide-websocket'
});

stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/greetings', (update) => {
        try {
            console.log(update);
            var delta = JSON.parse(update.body);
            console.log('delta: ');
            console.log(delta);
            console.log(delta.content);

            // Сохраняем текущую позицию курсора
            const currentRange = quill.getSelection();

            // Помечаем, что сейчас выполняются программные изменения
            isChangingContentsProgrammatically = true;
            quill.setContents(delta.content);

            // После установки содержимого, сбрасываем флаг обратно
            isChangingContentsProgrammatically = false;

            // Восстанавливаем позицию курсора, если она была изначально
            if (currentRange) {
                quill.setSelection(currentRange.index, currentRange.length);
            }
        } catch (e) {
            console.error("Invalid delta received: ", e);
        }
    });
};

function connect() {
    stompClient.activate();
}

function sendTextUpdate() {
    var content = quill.getContents();
    console.log(content);
    console.log(JSON.stringify(content));
    stompClient.publish({
        destination: "/app/hello",
        body: JSON.stringify({'name': content})
    });
}

// Флаг для отслеживания программных изменений
let isChangingContentsProgrammatically = false;

$(document).ready(function() {
    connect();
    quill.on('text-change', function() {
        // Вызываем sendTextUpdate только если изменения сделаны пользователем
        if (!isChangingContentsProgrammatically) {
            sendTextUpdate();
        }
    });
});