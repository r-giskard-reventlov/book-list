import Utils from "../../services/Utils.js";

const fetchBook = async (user, listId) => {
    return fetch(`/api/${user}/lists/${listId}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    });
};

const render = async () => {
    const listId = Utils.parseRequestURL().id;
    const list =
        await fetchBook('justin', listId)
            .then(resp => resp.json())
            .catch(console.log);

    const books = list.books.map((book, ix) => {
        console.log(ix)
        return /*html*/`
            <li class="list-group-item disabled" aria-disabled="true">
                <div id="${book.id}" class="card p-2">
                <div class="row g-0">
                    <div class="col-md-2">
                          <img src=""  alt="${book.title} cover art">
                    </div>
                    <div class="col-md">
                        <div class="card-body">
                            <h5 class="card-title">${book.title}</h5>
                            <p class="card-text">${book.description}</p>
                            <!---<p class="card-text">${book.authors}</p>--->
                            <p class="card-text"><small class="text-muted">${book.published}</small></p>
                        </div>
                    </div>
                  </div>
                </div>
            </li>
        `;
    }).join('');

    return /*html*/`
        <div>
            <div class="p-2">
                <ul class="list-group">
                    <li class="list-group-item disabled" aria-disabled="true">${list.name}</li>
                    ${books}
                </ul>
            </div>
        </div>
    `;
};

const after_render = async () => {
    document.querySelector('#book-search-main').focus();
};

const ListView = {
    render: render,
    after_render: after_render
}

export default ListView;
