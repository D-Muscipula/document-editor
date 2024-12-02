document.getElementById('document-form').addEventListener('submit', function(event) {
      event.preventDefault();

      const title = document.getElementById('title').value;

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
              const documentUrl = `${window.location.origin}/api/documents/${encodeURIComponent(data.title)}/${data.id}`;


              const linkContainer = document.getElementById('link-container');
              linkContainer.innerHTML = '';

              const link = document.createElement('a');
              link.href = documentUrl;
              link.textContent = "View Document";
              link.target = '_blank';
              linkContainer.appendChild(link);

              const urlText = document.createElement('p');
              urlText.textContent = documentUrl;
              linkContainer.appendChild(urlText);

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