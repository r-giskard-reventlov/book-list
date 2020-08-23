"use strict";

import Home from './views/pages/Home.js';
import Error404 from './views/pages/Error404.js';
import Header from './views/components/Header.js';
import Footer from './views/components/Footer.js';
import Sidebar from './views/components/SideBar.js';
import BookSearch from './views/components/BookSearch.js';
import Utils from './services/Utils.js';

const routes = {
    '/': Home,
    // '/patient/:id' : Patient
};


const router = async () => {
    const header = null || document.getElementById('header-container');
    const sidebar = null || document.getElementById('sidebar');
    const content = null || document.getElementById('page-container');
    const footer = null || document.getElementById('footer-container');

    sidebar.innerHTML = await Sidebar.render();
    await Sidebar.after_render();
    
    header.innerHTML = await Header.render();
    await Header.after_render();

    footer.innerHTML = await Footer.render();
    await Footer.after_render();

    const request = Utils.parseRequestURL()
    const parsedURL = (request.resource ? '/' + request.resource : '/') + (request.id ? '/:id' : '') + (request.verb ? '/' + request.verb : '')
    const page = routes[parsedURL] ? routes[parsedURL] : Error404
    content.innerHTML = await page.render();
    await page.after_render();
}

window.addEventListener('hashchange', router);
window.addEventListener('load', router);

// customElements.define('proxy-permissions', Proxy);
customElements.define('book-search', BookSearch);
