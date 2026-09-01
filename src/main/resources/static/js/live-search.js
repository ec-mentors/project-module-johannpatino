(function () {
    const form = document.querySelector('form.filters');
    const results = document.getElementById('results');
    if (!form || !results) return;

    const DEBOUNCE_MS = 250;
    let debounceTimer;

    form.addEventListener('input', event => {
        if (event.target.id === 'ingredient-search') return;
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(refresh, DEBOUNCE_MS);
    });

    form.addEventListener('change', event => {
        if (event.target.id === 'ingredient-search') return;
        clearTimeout(debounceTimer);
        refresh();
    });

    form.addEventListener('submit', event => {
        event.preventDefault();
        refresh();
    });

    document.addEventListener('filters-changed', refresh);

    async function refresh() {
        const query = new URLSearchParams(new FormData(form)).toString();

        const response = await fetch('/recipes/fragment?' + query);
        if (!response.ok) return;

        results.innerHTML = await response.text();
        history.replaceState(null, '', '/recipes' + (query ? '?' + query : ''));
    }
})();