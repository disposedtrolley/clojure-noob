(ns clojure-noob.ch3)

;; 1. Use the str, vector, list, hash-map, and hash-set functions.
(str [1 2 3 4])
(str {:name "James"})

(vector 1 2 3 4)
(vector {:name "James"} {:name "Katherine"})

(hash-map :name "James" :age 23)

(hash-set :a :b :a)  ;; not to be confused with a hash map


;; 2. Write a function that takes a number and adds 100 to it.
(defn add-100
  [num]
  (+ num 100))

(def add-100 #(+ % 100))


;; 3. Write a function, dec-maker, that works exactly like the function
;; inc-maker except with subtraction:
(defn dec-maker
  "Create a custom decrementer"
  [dec-by]
  #(- % dec-by))

(def dec9 (dec-maker 9))
(dec9 10)

(defn dec-maker-long
  [dec-by]
  (fn [num]
    (- num dec-by)))

;; 4. Write a function, mapset, that works like map except the return value
;; is a set:
(defn mapset
  [fn args]
  (set (map fn args)))

;; 5. Create a function that’s similar to symmetrize-body-parts except that it
;; has to work with weird space aliens with radial symmetry. Instead of two
;; eyes, arms, legs, and so on, they have five.
(def alien-body-parts
  [{:name "head" :size 3}
   {:name "foot-1" :size 2}
   {:name "arm-1" :size 3}])

(defn matching-parts
  [part]
  (if (re-find #"-1$" (:name part))
    (reduce (fn [all-parts idx]
              (into all-parts [{:name (clojure.string/replace (:name part) #"-1$" (str "-" idx))
                                :size (:size part)}]))
            []
            (range 2 6))
    []))

(defn complete-body-parts
  "Expects a seq of maps that have a :name and :size"
  [body-parts]
  (reduce (fn [final-body-parts part]
            (into final-body-parts (set (flatten [part (matching-parts part)]))))
          []
          body-parts))


;; 6. Create a function that generalizes symmetrize-body-parts and the function
;; you created in Exercise 5. The new function should take a collection of body
;; parts and the number of matching body parts to add.
;;
;; If you’re completely new to Lisp languages and functional program-ming, it
;; probably won’t be obvious how to do this. If you get stuck, just move on
;; to the next chapter and revisit the problem later.
(defn matching-parts
  [part total]
  (if (re-find #"-1$" (:name part))
    (reduce (fn [all-parts idx]
              (into all-parts [{:name (clojure.string/replace (:name part) #"-1$" (str "-" idx))
                                :size (:size part)}]))
            []
            (range 2 total))
    []))

(defn complete-body-parts
  "Expects a seq of maps that have a :name and :size, and the total number of
   body parts to add"
  [body-parts total]
  (reduce (fn [final-body-parts part]
            (into final-body-parts (set (flatten [part (matching-parts part total)]))))
          []))
