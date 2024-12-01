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

    fetch(`/document/${title}/${uuid}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('Received data object:', data);

            if (data.contentDelta) {
                try {
                    const parsedDelta = JSON.parse(data.contentDelta);

                    if (parsedDelta.delta) {
                        console.log('Applying delta:', parsedDelta.delta);
                        quill.updateContents(parsedDelta.delta);

                        // Устанавливаем курсор в конец текста после обновления
                        const length = quill.getLength(); // Получаем длину содержимого
                        quill.setSelection(length, 0);    // Перемещаем курсор в конец
                    } else {
                        console.error('No delta found in parsed contentDelta');
                    }
                } catch (error) {
                    console.error('Error parsing contentDelta:', error);
                }
            } else {
                console.error('No contentDelta found in received data');
            }
        })
        .catch(error => console.error('Failed to load initial document:', error));

    // Subscribe to WebSocket updates
    stompClient.subscribe(`/topic/deltas/${title}/${uuid}`, (update) => {
        try {
            var delta = JSON.parse(update.body);
            console.log('delta received by client: ', delta.content);
            // Сохраняем текущую позицию курсора
            const currentRange = quill.getSelection();
            console.log(uuid);
            isChangingContentsProgrammatically = true;
            quill.setContents(delta.content);
            isChangingContentsProgrammatically = false;
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
    console.log('Sending text update to server:', JSON.stringify(content));
    stompClient.publish({
        destination: `/app/update/${title}/${uuid}`,  // Исправлено использование кавычек
        body: JSON.stringify({'delta': content})
    });
}

let isChangingContentsProgrammatically = false;

$(document).ready(function() {
    connect();

    quill.on('text-change', function() {
        if (!isChangingContentsProgrammatically) {
            sendTextUpdate();
        }
    });

    window.addEventListener('beforeunload', function() {
        if (stompClient.active) {
            stompClient.deactivate();
        }
    });
});