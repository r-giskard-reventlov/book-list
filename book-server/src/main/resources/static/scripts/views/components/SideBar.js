const fetchLists = async (user) => {
    return fetch(`/api/${user}/lists`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    });
};

const render = async () => {
    const iconAddList = '<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" aria-hidden="true" focusable="false" width="1em" height="1em" style="-ms-transform: rotate(360deg); -webkit-transform: rotate(360deg); transform: rotate(360deg);" preserveAspectRatio="xMidYMid meet" viewBox="0 0 20 20"><path d="M19.4 9H16V5.6c0-.6-.4-.6-1-.6s-1 0-1 .6V9h-3.4c-.6 0-.6.4-.6 1s0 1 .6 1H14v3.4c0 .6.4.6 1 .6s1 0 1-.6V11h3.4c.6 0 .6-.4.6-1s0-1-.6-1zm-12 0H.6C0 9 0 9.4 0 10s0 1 .6 1h6.8c.6 0 .6-.4.6-1s0-1-.6-1zm0 5H.6c-.6 0-.6.4-.6 1s0 1 .6 1h6.8c.6 0 .6-.4.6-1s0-1-.6-1zm0-10H.6C0 4 0 4.4 0 5s0 1 .6 1h6.8C8 6 8 5.6 8 5s0-1-.6-1z" fill="#626262"/><rect x="0" y="0" width="20" height="20" fill="rgba(0, 0, 0, 0)" /></svg>';
    return /*html*/`
        <nav id="sidebarMenu" class="mr-1">
            <div class="sidebar-sticky pt-0">
                <h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
                    <span>My Lists</span>
                    <button id="btn-create-list" class="d-flex text-muted btn">${iconAddList}</button>
                </h6>
                <ul id="my-lists" class="nav flex-column mb-2 list-group"></ul>
           </div>
       </nav>`;
};

const clickedList = async (ev) => {
    // const listId = ev.currentTarget.children[0].dataset.id;
    // const myLists = document.getElementById('my-lists');
    // const listSelectedEvent = new Event('list-selected');
    // listSelectedEvent.listId = listId;
    // myLists.dispatchEvent(listSelectedEvent);
    const listId = ev.currentTarget.children[0].dataset.id;
    document.location.href=`#/lists/${listId}`;
};

const addList = async (list) => {
    const myLists = document.querySelector('#my-lists');

    const liInput = document.createElement('input');
    liInput.type = 'text';
    liInput.classList.add('form-control');
    liInput.placeholder = 'list name';

    if(list !== undefined) {
        liInput.value = list.name;
        liInput.dataset.id = list.id;
        liInput.classList.add('d-none');
    }

    const liInputLabel = document.createElement('span');
    if(list === undefined) {
        liInputLabel.classList.add('d-none');
    }

    if(list !== undefined) {
        liInputLabel.innerHTML = list.name;
    }

    const li = document.createElement('li');
    li.classList.add('nav-item');
    li.classList.add('list-group-item');
    li.style.cursor = 'pointer';
    li.appendChild(liInput);
    li.appendChild(liInputLabel);

    myLists.appendChild(li);

    liInput.focus();

    liInput.addEventListener('keydown', (ev) => {
        keyDownList(ev.which, liInput, liInputLabel)
    });

    li.addEventListener('click', clickedList)
};

const after_render = async (user) => {
    const lists =
        await fetchLists(user)
            .then(resp => resp.json())
            .catch(console.log);

    ;
    lists.forEach(list => {
        addList(list);
    })

    document.querySelector('#btn-create-list').addEventListener('click', (ev) => {
        addList();
    });
};

const createList = async (name) => {
    return fetch(`/api/justin/lists?name=${name}`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json'
        }
    });
};

const keyDownList = async (key, liInput, liInputLabel) => {
    if (key !== 13) {
        return false;
    }
    const list = await createList(liInput.value)
        .then(resp => resp.json())
        .catch(console.log);

    liInput.classList.add('d-none');
    liInput.dataset.id = list.id;
    liInputLabel.innerHTML = liInput.value;
    liInputLabel.classList.remove('d-none');
};


let Sidebar = {
    render: render,
    after_render: after_render
}

export default Sidebar;
