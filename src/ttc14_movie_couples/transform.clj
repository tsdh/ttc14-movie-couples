(ns ^{:pattern-expansion-context :emf}
  ttc14-movie-couples.transform
  (:require [clojure.java.io            :as io]
            [clojure.set                :as set]
            [funnyqt.emf                :as emf]
            [funnyqt.generic            :as gen]
            [funnyqt.in-place           :as ip]))

(emf/load-ecore-resource (io/resource "movies.ecore"))

;;* Generic helpers

(defn movie-count [p]
  (.size ^java.util.Collection (emf/eget-raw p :movies)))

(defn person-count [m]
  (.size ^java.util.Collection (emf/eget-raw m :persons)))

(defn movie-set [p]
  (into #{} (emf/eget-raw p :movies)))

(defn avg-rating [movies]
  (/ (reduce + (map #(emf/eget % :rating) movies))
     (count movies)))

(defn n-common-movies? [n p & more]
  (loop [common (movie-set p), more more]
    (when (>= (count common) n)
      (if (seq more)
        (recur (set/intersection common (movie-set (first more)))
               (rest more))
        common))))

;;* Plain pattern-matching based solution

(defmacro define-group-rule [n]
  (let [psyms (map #(symbol (str "p" %)) (range n))]
    `(ip/defrule ~(symbol (str "make-groups-of-" n "!"))
       {:forall true :no-result-vec true}
       [~'model ~'c]
       [~'m<Movie> :when (>= (person-count ~'m) ~n)
        ~@(mapcat (fn [i]
                    (let [ps (nth psyms i)]
                      `[~'m -<persons>-> ~ps
                        :when (>= (movie-count ~ps) ~'c)
                        ~@(when-not (zero? i)
                            `[:when (neg? (compare (emf/eget-raw ~(nth psyms (dec i)) :name)
                                                   (emf/eget-raw ~ps :name)))])
                        ~@(when-not (or (zero? i) (= i (dec n)))
                            `[:when (n-common-movies? ~'c ~@(take (inc i) psyms))])]))
                  (range n))
        :when-let [~'cms (n-common-movies? ~'c ~@psyms)]
        :as [~'cms ~@psyms]
        :distinct]
       (emf/ecreate! ~'model ~(if (= n 2) `'Couple `'Clique)
                     ~(if (= n 2)
                        `{:commonMovies ~'cms :avgRating (avg-rating ~'cms)
                          :p1 ~(first psyms) :p2 ~(second psyms)}
                        `{:commonMovies ~'cms :avgRating (avg-rating ~'cms)
                          :persons [~@psyms]})))))

(define-group-rule 2)
(define-group-rule 3)
(define-group-rule 4)
(define-group-rule 5)
