let Home = {
    render : async () => {
        let view =  /*html*/`
	    <div class='col'>
		<div class="p-2">
		    Welcome to Bookie
		</div>
	    </div>
        `
        return view
    }
    , after_render: async () => {
        document.querySelector('#book-search-main').focus();
    }

}

export default Home;
