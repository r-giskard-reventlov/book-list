fetch('http://localhost:3000/account/justin/lists')
  .then(response => response.json())
  .then(lists => {
      const listContainer = document.querySelector('.container');
      const listsHtml = Object.entries(lists).map((list) => {
	  const items = list[1].map((item) => {
	      return `
                  <div class='row'>
                      <div class='card'>
                          ${item.title}<br/>${item.author}
                      </div>
                  </div>
              `;
	  }).join("");
	  
	  return `
              <div class='row'>
                  <div class='col-sm-12'>
                      <div class='card fluid'>
                          <div class='section'>
                              ${list[0]}
                          </div>
                          <div class='section'>
                              ${items}
                          </div>
                      </div>
                  </div>
              </div>
          `;
      });
      console.log("html", listsHtml);
      listContainer.innerHTML = listsHtml.join("");
  });
