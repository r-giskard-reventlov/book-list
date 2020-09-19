(ns book-list.books
  (:require [clostache.parser :as parser]
            [clojure.java.io :as io]
            [clojure.data.json :as json]
            [cheshire.core :refer :all]))


(def works-by-keyword
  (let [works (parse-stream (clojure.java.io/reader "/home/justin/Storage/Development/book-list-project/data-extraction/indexes/works-by-keyword.ix"))]
    works))

(def works-by-id
  (let [works (parse-stream (clojure.java.io/reader "/home/justin/Storage/Development/book-list-project/data-extraction/indexes/works-by-id.ix"))]
    works))

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
                   ;;[:header :footer]
                   []
                   )))

(defn works-obj [works-id]
  (let [works (get works-by-id works-id)]
    works))

(defn works-by-word [word]
  (let [works-for-word (get works-by-keyword word)]
    (set works-for-word)))


;; still need to remove stopwords
(defn search [keywords]
  (let [
        ;; lower-case-keywords (do (println (str "keywords> " keywords)) (map clojure.string/lower-case keywords))
        work-ids (map works-by-word keywords)
        unique-works-ids (apply clojure.set/intersection work-ids)
        works (map works-obj unique-works-ids)]
    (do
      ;; (println lower-case-keywords)
      (println (str "ids > " work-ids))
      (println (str "unique > " unique-works-ids))
      (println (str "works > " works))
      works)))

