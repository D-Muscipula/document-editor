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

    // Fetch initial document state from your API
    fetch(`/document/${title}/${uuid}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('Received data object:', data);

            let messages = [];
            if (data.contentDelta) {
                try {
                    const parsedContent = JSON.parse(data.contentDelta); // Парсим строку JSON
                    messages = parsedContent.messages || [];
                } catch (error) {
                    console.error('Error parsing contentDelta:', error);
                }
            }

            if (Array.isArray(messages)) {
                messages.forEach(msg => {
                    console.log('Processing message:', msg);
                    if (msg.delta) {
                        quill.updateContents(msg.delta);
                    }
                });
            } else {
                console.error('Messages is not an array:', messages);
            }
        })
        .catch(error => console.error('Failed to load initial document:', error));

    // Subscribe to WebSocket updates
    stompClient.subscribe(`/topic/deltas/${title}/${uuid}`, (update) => {
        try {
            var delta = JSON.parse(update.body);
            console.log('Delta received by client:', delta);
            const currentRange = quill.getSelection();
            isChangingContentsProgrammatically = true;
            quill.updateContents(delta.delta); // Access delta.delta
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
        destination: `/app/update/${title}/${uuid}`,  // Corrected quoting
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