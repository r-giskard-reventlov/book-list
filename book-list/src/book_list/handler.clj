(ns book-list.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            ;; [compojure.route :as route]
            ;; [ring.middleware.defaults :refer [wrap-defaults site-defaults api-defaults]]
            [book-list.books :as books]
            [ring.util.response :refer :all]
            [ring.middleware.json :refer :all]
            ;; [clojure.data.json :as json]
            ))


(defroutes app-routes
  (GET "/" [] (books/home))
  (POST "/api/works" {body :body} (response (books/search (get body "keywords"))))
  (route/resources "/"))

(def app
  (-> (wrap-json-response app-routes)
      (wrap-json-body)
      (wrap-json-response)))
