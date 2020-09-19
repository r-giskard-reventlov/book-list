import Utils from "../../services/Utils.js";

const render = async () => {
    return /*html*/`
        <nav class="navbar navbar-dark bg-dark">
            <div class="container-fluid">
                <a class="navbar-brand" href="#">
                  <img src="/images/readingrooms.org.svg" width="60" height="60" alt="" loading="lazy" class="mr-2">
                  ReadingRooms
                </a> 
                <div class="" id="navbarNavDropdown">
                    <ul id='nav' class="navbar-nav mr-auto mb-2 mb-0"></ul>
                    <book-search id="book-search-main"></book-search>
                </div>
            </div>
        </nav>
    `;
};

const after_render = async () => {
    document.getElementById('book-search-main').addEventListener('work.selected', addToList);
};

const addToList = async (ev) => {
    const listId = Utils.parseRequestURL().id;
    await fetch(`/api/justin/lists/${listId}`, {
        method: 'POST',
        body: JSON.stringify({
            id: ev.work
        }),
        headers: {
            'Content-Type': 'application/json'
            // 'Accept': 'application/json'
        }
    }).then(resp => {
        document.location.href=`#/lists/${listId}`;
    });
}

const Header = {
    render: render,
    after_render: after_render
}

export default Header;
