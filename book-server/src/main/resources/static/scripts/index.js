"use strict";

import Home from './views/pages/Home.js';
import Error404 from './views/pages/Error404.js';
import Header from './views/components/Header.js';
import Footer from './views/components/Footer.js';
import Sidebar from './views/components/SideBar.js';
import BookSearch from './views/components/BookSearch.js';
import ListView from "./views/pages/ListView.js";
import Utils from './services/Utils.js';


const routes = {
    '/': Home,
    '/lists/:id' : ListView
};


const router = async () => {
    const header = document.getElementById('header-container');
    const sidebar = document.getElementById('sidebar');
    const content = document.getElementById('page-container');
    const footer = document.getElementById('footer-container');

    header.innerHTML = await Header.render();
    footer.innerHTML = await Footer.render();

    const request = Utils.parseRequestURL()
    const parsedURL = (request.resource ? '/' + request.resource : '/') + (request.id ? '/:id' : '') + (request.verb ? '/' + request.verb : '')
    const page = routes[parsedURL] ? routes[parsedURL] : Error404

    sidebar.innerHTML = await Sidebar.render('justin');
    content.innerHTML = await page.render();

    await Header.after_render();
    await Footer.after_render();
    await Sidebar.after_render('justin');
    await page.after_render();
}

window.addEventListener('hashchange', router);
window.addEventListener('load', router);

customElements.define('book-search', BookSearch);
