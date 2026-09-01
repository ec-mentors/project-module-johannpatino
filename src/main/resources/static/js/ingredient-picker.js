(function () {
    const searchInput = document.getElementById('ingredient-search');
    const suggestionList = document.getElementById('ingredient-suggestions');
    const chipContainer = document.getElementById('ingredient-chips');

    if (!searchInput) return;

    const MIN_LENGTH = 2;
    const DEBOUNCE_MS = 300;
    let debounceTimer;

    searchInput.addEventListener('input', () => {
        clearTimeout(debounceTimer);
        const query = searchInput.value.trim();

        if (query.length < MIN_LENGTH) {
            hideSuggestions();
            return;
        }
        debounceTimer = setTimeout(() => fetchSuggestions(query), DEBOUNCE_MS);
    });

    async function fetchSuggestions(query) {
        const response = await fetch(
            '/api/ingredients/autocomplete?q=' + encodeURIComponent(query));

        if (!response.ok) {
            hideSuggestions();
            return;
        }
        showSuggestions(await response.json());
    }

    function showSuggestions(suggestions) {
        suggestionList.innerHTML = '';

        suggestions.forEach(suggestion => {
            const item = document.createElement('li');
            item.textContent = suggestion.matchedName;

            if (suggestion.matchedName !== suggestion.canonicalName) {
                const matched = document.createElement('em');
                matched.textContent = ' — ' + suggestion.matchedName
                    + (suggestion.matchedLanguage ? ' (' + suggestion.matchedLanguage + ')' : '');
                item.appendChild(matched);
            }

            item.addEventListener('click',
                () => addChip(suggestion.id, suggestion.canonicalName));
            suggestionList.appendChild(item);
        });

        suggestionList.hidden = suggestions.length === 0;
    }

    function hideSuggestions() {
        suggestionList.innerHTML = '';
        suggestionList.hidden = true;
    }

    function addChip(id, name) {
        if (chipContainer.querySelector('input[value="' + id + '"]')) {
            hideSuggestions();
            searchInput.value = '';
            return;
        }

        const chip = document.createElement('span');
        chip.className = 'chip';

        const label = document.createElement('span');
        label.textContent = name;

        const hidden = document.createElement('input');
        hidden.type = 'hidden';
        hidden.name = 'ingredients';
        hidden.value = id;

        const remove = document.createElement('button');
        remove.type = 'button';
        remove.className = 'chip-remove';
        remove.textContent = '×';


        chip.append(label, hidden, remove);
        chipContainer.appendChild(chip);

        searchInput.value = '';
        hideSuggestions();
        document.dispatchEvent(new Event('filters-changed'))
    }

    chipContainer.addEventListener('click', event => {
        if (event.target.classList.contains('chip-remove')) {
            event.target.closest('.chip').remove();
            document.dispatchEvent(new Event('filters-changed'));
        }
    });
})();