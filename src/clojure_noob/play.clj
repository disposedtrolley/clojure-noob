(ns clojure-noob.play)

;; (defn spread
;;   ([x] x)
;;   ([x y] (- (max x y) (min x y)))
;;   ([x y z] (- (max x y z) (min x y z))))

(defn spread [& nums]
  (- (apply max nums) (apply min nums)))

(/ (+ 5 4 (- 2 (- 3 (+ 6 (/ 4 5)))))
  (* 3 (- 2 7) (- 6 2)))

(/ 2 4)

(defn a-plus-abs-b [a b]
  ((if (> b 0) + -)
   a
   b))

(if (= (count "four")
       4)
  :predicate-was-true
  :predicate-was-false)

(def x -12)

(cond (> x 0)
      x
      (= x 0)
      0
      (< x 0)
      (- x))

(cond (< 0 x)
      "Negative"
      :else
      "Positive")


(defn sqrt-iter [guess x]
  (if (good-enough? guess x)
    guess
    (sqrt-iter (improve guess x) x)))

(defn sqrt-iter [guess x]
  (new-if (good-enough? guess x)
    guess
    (sqrt-iter (improve guess x) x)))

(defn improve [guess x]
  (average guess (/ x guess)))

(defn average [x y]
  (/ (+ x y) 2))

(defn good-enough? [guess x]
  (< (abs (- (square guess) x)) 0.001))

(defn square [x]
  (* x x))

(defn abs [n]
  (max n (- n)))

(defn sqrt [x]
  (sqrt-iter 1.0 x))

(defn new-if [predicate then-clause else-clause]
  (cond predicate
        then-clause
        :else
        else-clause))

(new-if (= 2 3) 0 5)
