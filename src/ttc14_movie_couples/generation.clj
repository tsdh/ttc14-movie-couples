(ns ttc14-movie-couples.generation
  (:require [funnyqt.emf :as emf]))

(defn ^:private create-positive! [model i]
  (let [m1 (emf/ecreate! model 'Movie :rating (+ 0.0 (* 10 i)))
        m2 (emf/ecreate! model 'Movie :rating (+ 1.0 (* 10 i)))
        m3 (emf/ecreate! model 'Movie :rating (+ 2.0 (* 10 i)))
        m4 (emf/ecreate! model 'Movie :rating (+ 3.0 (* 10 i)))
        m5 (emf/ecreate! model 'Movie :rating (+ 5.0 (* 10 i)))]
    (emf/ecreate! model 'Actor   :name (str "a" (* 10 i))       :movies [m1 m2 m3 m4])
    (emf/ecreate! model 'Actor   :name (str "a" (+ 1 (* 10 i))) :movies [m1 m2 m3 m4])
    (emf/ecreate! model 'Actor   :name (str "a" (+ 2 (* 10 i))) :movies [m2 m3 m4])
    (emf/ecreate! model 'Actress :name (str "a" (+ 3 (* 10 i))) :movies [m2 m3 m4 m5])
    (emf/ecreate! model 'Actress :name (str "a" (+ 4 (* 10 i))) :movies [m2 m3 m4 m5])))

(defn ^:private create-negative! [model i]
  (let [m1 (emf/ecreate! model 'Movie :rating (+ 5.0 (* 10 i)))
        m2 (emf/ecreate! model 'Movie :rating (+ 6.0 (* 10 i)))
        m3 (emf/ecreate! model 'Movie :rating (+ 7.0 (* 10 i)))
        m4 (emf/ecreate! model 'Movie :rating (+ 8.0 (* 10 i)))
        m5 (emf/ecreate! model 'Movie :rating (+ 9.0 (* 10 i)))]
    (emf/ecreate! model 'Actor   :name (str "a" (+ 5 (* 10 i))) :movies [m1 m2])
    (emf/ecreate! model 'Actor   :name (str "a" (+ 6 (* 10 i))) :movies [m1 m2 m3])
    (emf/ecreate! model 'Actress :name (str "a" (+ 7 (* 10 i))) :movies [m2 m3 m4])
    (emf/ecreate! model 'Actress :name (str "a" (+ 8 (* 10 i))) :movies [m3 m4 m5])
    (emf/ecreate! model 'Actress :name (str "a" (+ 9 (* 10 i))) :movies [m4 m5])))

(defn create-example [n]
  (let [model (emf/new-resource)]
    (dotimes [i n]
      (create-positive! model i)
      (create-negative! model i))
    model))
