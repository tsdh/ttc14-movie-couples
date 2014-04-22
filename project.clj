(defproject ttc14-movie-couples "0.1.0-SNAPSHOT"
  :description "The FunnyQT solution to the TTC Movie Couples case."
  :url "https://github.com/tsdh/ttc14-movie-couples"
  :license {:name "GNU General Public License, Version 3 (or later)"
            :url "http://www.gnu.org/licenses/gpl.html"
            :distribution :repo}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [funnyqt "0.20.1"]]
  :profiles {:dev
             {:dependencies
              [[criterium "0.4.3"]
               [org.clojure/tools.namespace "0.2.4"]]}}
  :test-selectors {:imdb      :imdb
                   :synthetic :synthetic
                   :cliques   :cliques}
  :jvm-opts ^:replace ["-Xms4G" "-Xmx4G"]
  :source-paths ["src/" "../imdb2emf/src/"]
  :global-vars {*warn-on-reflection* true
                *unchecked-math*     true})
