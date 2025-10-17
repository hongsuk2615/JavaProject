function createAuthorElement(email, provider) {
    const container = document.createElement('span');
    container.className = 'author-container';

    const providerIcon = document.createElement('img');
    providerIcon.className = 'provider-icon';
    providerIcon.alt = provider || '';
    providerIcon.title = provider || '';

    if (provider === 'google') {
        providerIcon.src = 'https://www.svgrepo.com/show/475656/google-color.svg';
    } else if (provider === 'github') {
        providerIcon.src = 'https://www.svgrepo.com/show/475654/github-color.svg';
    }

    if (providerIcon.src) {
        providerIcon.style.width = '16px';
        providerIcon.style.height = '16px';
        providerIcon.style.marginRight = '6px';
        providerIcon.style.marginBottom = '4px';
    }

    container.appendChild(providerIcon);
    container.appendChild(document.createTextNode(email));

    return container;
}
