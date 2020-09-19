export default class BookSearch extends HTMLElement {

    constructor() {
        super();

        this.attachShadow({ mode: 'open' });

        const bootsrapLink = document.createElement('link');
        bootsrapLink.href = 'https://stackpath.bootstrapcdn.com/bootstrap/5.0.0-alpha1/css/bootstrap.min.css';
        bootsrapLink.rel = 'stylesheet';

        const searchDiv = document.createElement('div');
        searchDiv.style.position = 'relative';

        const searchSpan = document.createElement('span');
        searchSpan.classList.add('input-group-text')
        searchSpan.innerHTML = this.mignifyingGlass();

        const searchField = document.createElement('input');
        searchField.type = 'search';
        searchField.placeholder = 'keywords';
        searchField.classList.add('form-control');
        searchField.classList.add('mr-2');
        searchField.tabIndex = 0;

        const searchInputGroup = document.createElement('div');
        searchInputGroup.classList.add('input-group');
        searchInputGroup.classList.add('mb-3');
        searchInputGroup.appendChild(searchSpan);
        searchInputGroup.appendChild(searchField);

        const resultsDiv = document.createElement("div");
        resultsDiv.style.position = 'absolute';
        resultsDiv.style.top = '50px';

        resultsDiv.style.zIndex = '99';
        resultsDiv.classList.add('d-none');

        searchDiv.appendChild(searchInputGroup);
        searchDiv.appendChild(resultsDiv);

        this.searchField = searchField;
        this.resultsDiv = resultsDiv;

        this.shadowRoot.appendChild(bootsrapLink);
        this.shadowRoot.appendChild(searchDiv);

        resultsDiv.style.width = '800px';
        resultsDiv.style.right = '0';

        this.searchField.addEventListener('keydown', this.searchForWorks);
    }

    mignifyingGlass = () => {
        return '<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" aria-hidden="true" focusable="false" width="1em" height="1em" style="-ms-transform: rotate(360deg); -webkit-transform: rotate(360deg); transform: rotate(360deg);" preserveAspectRatio="xMidYMid meet" viewBox="0 0 8 8"><path d="M3.5 0C1.57 0 0 1.57 0 3.5S1.57 7 3.5 7c.59 0 1.17-.14 1.66-.41a1 1 0 0 0 .13.13l1 1a1.02 1.02 0 1 0 1.44-1.44l-1-1a1 1 0 0 0-.16-.13c.27-.49.44-1.06.44-1.66c0-1.93-1.57-3.5-3.5-3.5zm0 1C4.89 1 6 2.11 6 3.5c0 .66-.24 1.27-.66 1.72l-.03.03a1 1 0 0 0-.13.13c-.44.4-1.04.63-1.69.63c-1.39 0-2.5-1.11-2.5-2.5s1.11-2.5 2.5-2.5z" fill="#626262"/><rect x="0" y="0" width="8" height="8" fill="rgba(0, 0, 0, 0)" /></svg>';
    }

    focus(options) {
        this.searchField.focus();
    }

    search = async (keywords) => {
        return fetch(`/api/works?keywords=${keywords}`, {
            method: 'GET',
            // body: JSON.stringify({
            //     keywords: query.split(/\s{1,}/)
            // }),
            headers: {
                // 'Content-Type': 'application/json'
                'Accept': 'application/json'
            }
        });
    }

    resultRow = (work) => {
        const title = `
            <div class="d-flex w-100 justify-content-between">
                <h5 class="mb-1">${work.title}</h5>
                <span class="text-muted">${work.id}</span>
            </div>`;

        const description = `
            <p class="mb-1">${work.description}</p>
        `;

        const authors = `
            <small class="text-muted">${work.authors.map(a => a.name).join(",")}</small>
        `;

        return `<li class="list-group-item list-group-item-action work" 
                    data-id="${work.id}"
                    data-name="${work.title}">${title}${description}${authors}</li>`;
    };

    searchForWorks = async (ev) => {
        console.log('key', ev);
        if (ev.which != 13) {
            return false;
        }
        ev.preventDefault();
        const keywords = this.searchField.value;
        const splitKeywords = keywords.split(/\s{1,}/);
        const keywordsJoined = splitKeywords.join();
        if(splitKeywords.length === 0) return;

        await this.search.call(this, keywordsJoined)
            .then(response => response.json())
            .then(works => {
                const results = works.map(work => {
                    if(!work) return this.resultRow({
                        title: 'Retrieval issue',
                        key: 123
                    });
                    return this.resultRow(work);
                }).join('');

		this.resultsDiv.classList.remove("d-none");
		this.resultsDiv.innerHTML = `<ul class="list-group">${results}</ul>`;
		this.resultsDiv.style.cursor = 'pointer';

		this.resultsDiv.querySelectorAll('.work').forEach((result, ix, results) => {
		    result.tabIndex = ix;
		    if(ix === 0) {
                result.classList.add('active');
                result.focus();
		    }
		    result.addEventListener('click', this.selectWork);
		    result.addEventListener('keydown', (ev) => {
			const nextIx = ev.currentTarget.tabIndex + 1;
			const previousIx = ev.currentTarget.tabIndex - 1;
			const current = ev.currentTarget;
			if(ev.key === 'ArrowDown' && nextIx < results.length) {
			    current.classList.remove('active');
			    results[nextIx].classList.add('active');
			    results[nextIx].focus();
			} else if(ev.key === 'ArrowUp' && previousIx >= 0) {
			    current.classList.remove('active');
			    results[previousIx].classList.add('active');
			    results[previousIx].focus();
			} else if(ev.key === 'Enter') {
			    result.click();
			}
		    });
		});
	    });
    };

    selectWork = (ev) => {
        const workSelected = new Event('work.selected');
        workSelected.work = ev.currentTarget.dataset.id;
        workSelected.name = ev.currentTarget.dataset.name;

        this.dispatchEvent(workSelected);

        this.resultsDiv.innerHTML = '';
        this.resultsDiv.classList.add('d-none');
    };
}
