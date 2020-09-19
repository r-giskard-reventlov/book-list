
const render = async () => {
    return /*html*/`
        <div>
            <div class="p-2">
                My Feed
            </div>
        </div>
    `;
};


const after_render = async () => {
    document.querySelector('#book-search-main').focus();
    // document.getElementById('my-lists').addEventListener('list-selected', listSelected);
};

let Home = {
    render: render,
    after_render: after_render
}

export default Home;
