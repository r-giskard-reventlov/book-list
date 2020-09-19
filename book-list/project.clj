(defproject book-list "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [ring/ring "1.8.1"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-json "0.5.0"]
                 [compojure "1.6.1"]
                 ;; [codax "1.3.1"]
                 ;; [cljstache "2.0.6"]
                 [de.ubercode.clostache/clostache "1.4.0"]
                 [org.clojure/data.json "1.0.0"]
                 [cheshire "5.10.0"]]
  :jvm-opts ["-Xmx6g"]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler book-list.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
