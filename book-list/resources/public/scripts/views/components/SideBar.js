let Sidebar = {
    render: async () => {
	const iconAddList = '<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" aria-hidden="true" focusable="false" width="1em" height="1em" style="-ms-transform: rotate(360deg); -webkit-transform: rotate(360deg); transform: rotate(360deg);" preserveAspectRatio="xMidYMid meet" viewBox="0 0 20 20"><path d="M19.4 9H16V5.6c0-.6-.4-.6-1-.6s-1 0-1 .6V9h-3.4c-.6 0-.6.4-.6 1s0 1 .6 1H14v3.4c0 .6.4.6 1 .6s1 0 1-.6V11h3.4c.6 0 .6-.4.6-1s0-1-.6-1zm-12 0H.6C0 9 0 9.4 0 10s0 1 .6 1h6.8c.6 0 .6-.4.6-1s0-1-.6-1zm0 5H.6c-.6 0-.6.4-.6 1s0 1 .6 1h6.8c.6 0 .6-.4.6-1s0-1-.6-1zm0-10H.6C0 4 0 4.4 0 5s0 1 .6 1h6.8C8 6 8 5.6 8 5s0-1-.6-1z" fill="#626262"/><rect x="0" y="0" width="20" height="20" fill="rgba(0, 0, 0, 0)" /></svg>';
        const  view =  /*html*/`
            <nav id="sidebarMenu" class="col-3 col-md-2 d-md-block bg-light sidebar collapse">
		<div class="sidebar-sticky pt-3">
		  <h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
		    <span>My Lists</span>
                    <button id="btn-create-list" class="d-flex text-muted btn">${iconAddList}</button>
		  </h6>
		  <ul class="nav flex-column mb-2">
		    <li class="nav-item">
		      <a class="nav-link" href="#">
			<span data-feather="file-text"></span>
			Current month
		      </a>
		    </li>
		    <li class="nav-item">
		      <a class="nav-link" href="#">
			<span data-feather="file-text"></span>
			Last quarter
		      </a>
		    </li>
		    <li class="nav-item">
		      <a class="nav-link" href="#">
			<span data-feather="file-text"></span>
			Social engagement
		      </a>
		    </li>
		    <li class="nav-item">
		      <a class="nav-link" href="#">
			<span data-feather="file-text"></span>
			Year-end sale
		      </a>
		    </li>
		  </ul>
		</div>
	      </nav>
        `
        return view
    },
    after_render: async () => {

    }
}

export default Sidebar;
