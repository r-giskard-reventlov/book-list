let Header = {
    render: async () => {
        let view =  /*html*/`
            <nav class="navbar navbar-dark bg-dark">
                <div class="container-fluid">
                    <a class="navbar-brand">Bookie</a>
                    <div class="" id="navbarNavDropdown">
                        <ul id='nav' class="navbar-nav mr-auto mb-2 mb-0"></ul>
                        <book-search id="book-search-main"></book-search>
                    </div>
                </div>
            </nav>
        `
        return view
    },
    after_render: async () => {
        // document.querySelector('#patient-search-main').addEventListener('patient.selected', (ev) => {
        //     document.location.href = `#/patient/${ev.patient}`;
        // });
    }
}

export default Header;
