(ns book-list.books
  (:require [clostache.parser :as parser]
            [clojure.java.io :as io]
            [clojure.data.json :as json]
            [cheshire.core :refer :all]))


(def accounts {"justin" {"2020 books" [{:title "The way of Kings" :author "Brandon Sanderson"}
                                       {:title "The name of the Wind" :author "Patrick Rothfuss"}]
                         "2019 books" [{:title "Foundation" :author "Isaac Asimov"}]}
               "iain" {"My Favourite books" [{:title "The way of Kings"}]}})

(def works-by-keyword
  (let [works (parse-stream (clojure.java.io/reader "/home/justin/Storage/Development/book-list-project/data-extraction/works-lowercase.ix"))]
    works))

(def works-by-id
  (let [works (parse-stram (clojure.java.io/reader "/home/justin/Storage/Development/book-list-project/data-extraction/works-by-id.ix"))]))

(defn render-page
  "Render a page including any partials"
  [template data partials]
  (parser/render-resource
   (str "templates/" template ".mustache")
   data
   (reduce (fn [accum pt] ;; "pt" is the name (as a keyword) of the partial.
             (assoc accum pt (slurp (io/resource (str "templates/"
                                                      (name pt)
                                                      ".mustache")))))
           {}
           partials)))

(defn home []
  (do (println "here")
      (render-page "home"
                   {:title "Books"
                    :name "Justin Wilson"}
                   [:header :footer])))

(defn lists-for-account [account]
  (let [account-list (get accounts account)]
    (do (println (str "Returning lists for " account " " account-list)))
        account-list))

(defn works-by-word [word]
  (let [works-for-word (get works-by-keyword word)]
    (set works-for-word)))

(defn search [title-words]
  (let [work-ids (map works-by-word title-words)]
    (apply clojure.set/intersection work-ids)))

