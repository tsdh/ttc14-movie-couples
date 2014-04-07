(ns ttc14-movie-couples.core-test
  (:require [clojure.test                   :refer :all]
            [funnyqt.emf                    :as emf]
            [funnyqt.utils                  :as u]
            [ttc14-movie-couples.transform  :as transform]
            [ttc14-movie-couples.generation :as gen]
            [ttc14-movie-couples.queries    :as query]
            [imdb2emf.serialize             :as bin])
  (:import (org.eclipse.emf.ecore.resource Resource)))

(defn load-model [^String f]
  (if (.endsWith f ".movies")
    (emf/load-resource f)
    (bin/read-movies-model f)))

(deftest ^:synthetic test-synthetic-models
  (println)
  (println "Tests on Synthetic Models")
  (println "=========================")
  (println)
  (doseq [N [1000 2000 3000 4000 5000 10000 50000 100000 200000]]
    (doseq [[func n] [[transform/make-groups-of-2! 2]
                      [transform/make-groups-of-3! 3]
                      [transform/make-groups-of-4! 4]
                      [transform/make-groups-of-5! 5]]]
      (System/gc)
      (println "Synthetic model for N =" N " / Groups of" n)
      (let [m (u/timing "    Generation time: %T" (gen/create-example N))]
        (println "    Model size:" (.size (.getContents ^Resource m)))
        (u/timing "    No. groups (%s): %R\n    Exec. time: %T" (func m 3) n)
        (u/timing "    Query time: %T"
                  (query/spit-top-n-groups-of-size-x
                   m 15 n (format "query-results/synth-%s.query.%s" N n)))))))

(deftest ^:imdb test-imdb-models
  (println)
  (println "Tests on IMDb Models")
  (println "====================")
  (println)
  (doseq [f (sort (map #(.getName ^java.io.File %)
                       (.listFiles (java.io.File. "models"))))
          :when (re-matches #".*\.movies(?:\.bin)?" f)]
    (doseq [[func n] [[transform/make-groups-of-2! 2]
                      [transform/make-groups-of-3! 3]]]
      (System/gc)
      (println "Model" f "/ Groups of" n)
      (let [m (u/timing "    Load time: %T" (load-model (str "models/" f)))
            size (count (emf/eallobjects m))]
        (println "    Model size:" (.size (.getContents ^Resource m)) "elements")
        (u/timing "    No. groups (%s): %R\n    Exec. time: %T" (func m 3) n)
        (u/timing "    Query time: %T"
                  (query/spit-top-n-groups-of-size-x
                   m 15 n (format "query-results/%s.query.%s" f n)))))))

