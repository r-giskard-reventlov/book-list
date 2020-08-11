(ns book-list.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [book-list.books :as books]
            [ring.util.response :as r]
            [clojure.data.json :as json]))


(defroutes app-routes
  (GET "/account/:account/lists" [account]
       (let [resp (-> (r/response (json/write-str (books/lists-for-account account)))
                      (r/status 200)
                      (r/header "Content-Type" "application/json"))]
         resp))
  (GET "/" [] (books/home))
  (route/not-found "Not Found"))


(def app
  (wrap-defaults app-routes site-defaults))
