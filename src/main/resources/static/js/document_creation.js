document.getElementById('document-form').addEventListener('submit', function(event) {
      event.preventDefault(); // предотвращаем стандартное поведение формы

      const title = document.getElementById('title').value;

      // Формирование URL с title в качестве части пути
      const url = `/api/documents/${encodeURIComponent(title)}`;

      fetch(url, {
          method: 'POST',
          headers: {
              'Content-Type': 'application/json'
          }
      })
      .then(response => response.json())
      .then(data => {
          if (data.id) {
              // Используем window.location.origin для получения базового URL (https://host:port)
              const documentUrl = `${window.location.origin}/api/documents/${encodeURIComponent(data.title)}/${data.id}`;

              // Создаем и отображаем ссылку
              const linkContainer = document.getElementById('link-container');
              linkContainer.innerHTML = ''; // очищаем контейнер

              const link = document.createElement('a');
              link.href = documentUrl;
              link.textContent = "View Document";
              link.target = '_blank'; // для открытия в новой вкладке
              linkContainer.appendChild(link);

              const urlText = document.createElement('p');
              urlText.textContent = documentUrl;
              linkContainer.appendChild(urlText);

              // Добавляем кнопку для копирования ссылки
              const copyButton = document.createElement('button');
              copyButton.textContent = 'Copy Link';
              copyButton.className = 'copy-button';
              linkContainer.appendChild(copyButton);

              copyButton.addEventListener('click', () => {
                  navigator.clipboard.writeText(documentUrl).then(() => {
                      alert('Link copied to clipboard');
                  }, (error) => {
                      console.error('Failed to copy: ', error);
                  });
              });

              document.getElementById('document-form').reset();
          } else {
              alert('Failed to create document. Please try again.');
          }
      })
      .catch(error => {
          console.error('Error:', error);
          alert('An error occurred. Please try again.');
      });
  });