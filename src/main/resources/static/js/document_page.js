var quill = new Quill('#editor-container', {
  modules: {
    toolbar: '#toolbar'
  },
  theme: 'snow'
});

const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/editor-websocket'
});

stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);
    stompClient.subscribe(`/topic/deltas/${title}/${uuid}`, (update) => {
        try {
            var delta = JSON.parse(update.body);
            console.log('delta received by client: ', delta.content);
            // Сохраняем текущую позицию курсора
            const currentRange = quill.getSelection();
            console.log(uuid);
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
    console.log('sending text update to server: ', JSON.stringify(content));
    stompClient.publish({
        destination: `/app/update/${title}/${uuid}`,
        body: JSON.stringify({'delta': content})
    });
}

// Флаг для отслеживания программных изменений
let isChangingContentsProgrammatically = false;

$(document).ready(function() {
    connect();

    quill.on('text-change', function() {
        if (!isChangingContentsProgrammatically) {
            sendTextUpdate();
        }
    });

    // Закрываем WebSocket соединение перед уходом пользователя со страницы
    window.addEventListener('beforeunload', function() {
        if (stompClient.active) {
            stompClient.deactivate();
        }
    });
});