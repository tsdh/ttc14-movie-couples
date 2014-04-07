(ns ttc14-movie-couples.queries
  (:require [clojure.string  :as str]
            [clojure.java.io :as io]
            [funnyqt.emf     :as emf]
            [funnyqt.generic :as gen]
            [funnyqt.polyfns :as poly]
            [funnyqt.utils   :as u]))

(defn rating-comparator [a b]
  (compare (emf/eget b :avgRating) (emf/eget a :avgRating)))

(defn common-movies-comparator [a b]
  (compare (.size ^java.util.Collection (emf/eget-raw b :commonMovies))
           (.size ^java.util.Collection (emf/eget-raw a :commonMovies))))

(poly/declare-polyfn actors [group])

(poly/defpolyfn actors movies.Couple [group]
  [(emf/eget group :p1) (emf/eget group :p2)])

(poly/defpolyfn actors movies.Clique [group]
  (emf/eget-raw group :persons))

(defn names-comparator [a b]
  (compare (str/join ";" (map #(emf/eget % :name) (actors a)))
           (str/join ";" (map #(emf/eget % :name) (actors b)))))

(defn comparator-combinator [& comparators]
  (fn [a b]
    (loop [cs comparators]
      (if (seq cs)
        (let [r ((first cs) a b)]
          (if (zero? r)
            (recur (rest cs))
            r))
        (u/errorf "%s and %s are incomparable!" a b)))))

(defn groups-by-avg-rating [groups]
  (sort (comparator-combinator rating-comparator common-movies-comparator names-comparator)
        groups))

(defn groups-by-common-movies [groups]
  (sort (comparator-combinator common-movies-comparator rating-comparator names-comparator)
        groups))

(defn group-str [g]
  (let [[ts ps] (if (gen/has-type? g 'Couple)
                  ["Couple" [(emf/eget g :p1) (emf/eget g :p2)]]
                  (let [ps (emf/eget-raw g :persons)]
                    [(str (count ps) "-Clique") ps]))
        ms ^java.util.Collection (emf/eget-raw g :commonMovies)]
    (format "%s avgRating %6.3f, %3d movies (%s)"
            ts (emf/eget g :avgRating) (.size ms)
            (str/join "; " (map #(emf/eget % :name) ps)))))

(defn print-top-n [^java.io.PrintWriter w ^objects ary n]
  (let [k (min (alength ary) n)]
    (dotimes [i k]
      (.print w (format (str "%" (count (str k)) "d. ") (inc i)))
      (.println w (group-str (aget ary i))))))

(defn spit-query-results [groups n file]
  (with-open [w (java.io.PrintWriter. (io/writer file))]
    (let [^objects ary (into-array groups)]
      (.println w (str "There are " (alength ary) " groups.\n"))
      (.println w (format "Top-%d by Average Rating" n))
      (.println w         "========================\n")
      (groups-by-avg-rating ary)
      (print-top-n w ary n)
      (.println w)
      (.println w (format "Top-%d by Number of Common Movies" n))
      (.println w         "=================================\n")
      (groups-by-common-movies ary)
      (print-top-n w ary n))))

(defn clique-size [cl]
  (.size ^java.util.Collection (emf/eget-raw cl :persons)))

(defn spit-top-n-groups-of-size-x [model n x file]
  (spit-query-results (if (= x 2)
                        (emf/eallobjects model 'Couple)
                        (filter #(== (clique-size %) x)
                                (emf/eallobjects model 'Clique)))
                      n file))
