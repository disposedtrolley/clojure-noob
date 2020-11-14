(ns clojure-noob.ch4)

;; # Programming to Abstractions
;; 
;; - Clojure's collection and sequence abstractions allow you to apply the same
;;   methods to a variety of data structures.
;;   - The `map` function can be used to apply a transformation over any sequence
;;     in Clojure, whereas in elisp you need to use different functions depending
;;     on the data structure; `mapcar` for lists and `maphash` for hash maps.
;; - A **sequence** (seq) is any data structure which responds to (implements)
;;   the core sequence functions:
;;   - `first`
;;   - `rest`
;;   - `cons`

(defn titleize
  [topic]
  (str topic " for the Brave and True"))

(map titleize ["Hamsters", "Ragnarok"])
(map titleize '("Empathy" "Decorating"))
(map titleize #{"Elbows" "Soap Carving"})
(map #(titleize (second %)) {:uncomfortable-thing "Winking"})

;; ## Abstraction Through Indirection
;;
;; - Whenever Clojure expects a seq (i.e. when calling `map`, `first`, `rest`
;;   or `cons`), it calls the `seq` function on the input to convert it into
;;   a data structure that implements `first`, `rest`, and `cons`.

(seq '(1 2 3))
(seq [1 2 3])
(seq #{1 2 3})
(seq {:name "Bill Compton" :occupation "Dead mopey guy"})

;; - `seq` always returns a value that looks and behaves like a list.
;; - A seq can be converted back into a map using `into`.

(into {} (seq {:name "Bill Compton" :occupation "Dead mopey guy"}))

;; - Clojure values what can be done with a data structure rather than
;;   its implementation.

;; # Seq Function Examples
;; 
;; ## map
(map inc [1 2 3])
(map str ["a" "b" "c"] ["A" "B" "C"])


(def human-consumption [8.1 7.3 6.6 5.0])
(def critter-consumption [0.0 0.2 0.3 1.1])
(defn unify-diet-data
  [human critter]
  {:human human
   :critter critter})

(map unify-diet-data human-consumption critter-consumption)

;; - `map` can also be passed a collection of functions.

(def sum #(reduce + %))
(def avg #(/ (sum %) (count %)))
(defn stats
  [numbers]
  (map #(% numbers) [sum count avg])) ;; % is replaced by each function.

(stats [3 4 10])
(stats [80 1 44 13 6])

;; - `map` is also often used to retrieve each value of a keyword in a
;;   collection of maps.

(def identities
  [{:alias "Batman" :real "Bruce Wayne"}
   {:alias "Spider-Man" :real "Peter Parker"}
   {:alias "Santa" :real "Your mum"}
   {:alias "Easter Bunny" :real "Your dad"}])

(map :real identities)

;; ## reduce
;; 
;; Some non-obvious usages of `reduce`.
;; - Updating values of a map:
(reduce (fn [new-map [key val]]
          (assoc new-map key (inc val)))
        {}
        {:max 30 :min 10})

;; - Filtering out keys from a map:
(reduce (fn [new-map [key val]]
          (if (> val 4)
            (assoc new-map key val)
            new-map))
        {}
        {:human 4.1
         :critter 3.9})

;; - Implementing `map`:
(defn my-map
  [transform coll]
  (reverse
   (reduce (fn [new-coll val]
            (cons (transform val) new-coll))
          []
          coll)))

;; ## take, drop, take-while, drop-while

(take 3 [1 2 3 4 5 6])
(drop 3 [1 2 3 4 5 6])

(def food-journal
  [{:month 1 :day 1 :human 5.3 :critter 2.3}
   {:month 1 :day 2 :human 5.1 :critter 2.0}
   {:month 2 :day 1 :human 4.9 :critter 2.1}
   {:month 2 :day 2 :human 5.0 :critter 2.5}
   {:month 3 :day 1 :human 4.2 :critter 3.3}
   {:month 3 :day 2 :human 4.0 :critter 3.8}
   {:month 4 :day 1 :human 3.7 :critter 3.9}
   {:month 4 :day 2 :human 3.7 :critter 3.6}])
  
(take-while #(< (:month %) 3) food-journal)
(drop-while #(< (:month %) 3) food-journal)

(take-while #(< (:month %) 4)
            (drop-while #(< (:month %) 2) food-journal)) ;; drops Jan and takes until April.

;; ## filter and some
;;
;; - `filter` can be used in place of take-while and drop-while, but is less
;;   efficient for sorted collections since it processes every element.
(filter #(< (:human %) 5) food-journal)
(filter #(and (> (:month %) 1) (< (:month %) 4)) food-journal) ;; same as drop-while and take-while.

(defn filter-journal-by-months
  [journal start-month end-month]
  (filter #(and (>= (:month %) start-month) (<= (:month %) end-month)) journal))

;; - `some` returns the first truthy value of a collection that tests true for a
;;   predicate.
(some #(> (:critter %) 5) food-journal)
(some #(> (:critter %) 3) food-journal)

;; - The return value of `some` is determined by the return value of the predicate
;;   function.
(some #(and (> (:critter %) 3) %) food-journal)

;; ## sort and sort-by
(sort [3 2 1])

(sort-by count ["aaa" "c" "bb"])

;; ## concat
(concat [1 2] [3 4])

;; # Lazy Seqs
;;
;; - Many functions like `map` and `filter` actually return a **lazy seq**.
;;   - These are seqs whose members aren't computed until you try to access
;;     them; called **realising** the seq.
;; - Lazy seqs are realised in chunks and cached.

(def vampire-database
  {0 {:makes-blood-puns? false, :has-pulse? true  :name "McFishwich"}
   1 {:makes-blood-puns? false, :has-pulse? true  :name "McMackson"}
   2 {:makes-blood-puns? true,  :has-pulse? false :name "Damon Salvatore"}
   3 {:makes-blood-puns? true,  :has-pulse? true  :name "Mickey Mouse"}})

(defn vampire-related-details
  [social-security-number]
  ;;(Thread/sleep 1000)
  (get vampire-database social-security-number))

(defn vampire?
  [record]
  (and (:makes-blood-puns? record)
       (not (:has-pulse> record))
       record))

(defn identify-vampire
  [social-security-numbers]
  (first (filter vampire?
                 (map vampire-related-details social-security-numbers))))

(time (vampire-related-details 0))

(time (def mapped-details (map vampire-related-details (range 0 1000000)))) ;; instant because nothing has evaluated.
(time (first mapped-details)) ;; 32 seconds the first time because Clojure realises values in chunks.

(time (identify-vampire (range 0 1000000)))

;; ## Infinite Sequences

(concat (take 8 (repeat "na")) ["Batman!"])

(take 3 (repeatedly (fn [] (rand-int 10))))

;; # Function Functions
;;
;; ## `apply`
;; - Takes a function and collection of values, exploding the collection
;;   so elements are passed as individual arguments to the function.

(apply max [0 1 2]) ;; same as (max 0 1 2)

(defn my-into
  [target additions]
  (apply conj target additions))

(apply println [0 1 2])

;; ## `partial`
;; - Takes a function and unlimited number of arguments, returning a function
;;   that can be called as if it were the original function with original
;;   arguments, plus any new arguments supplied.

(def add10 (partial + 10))
(add10 3)
(add10 5)

(def add-missing-elements
  (partial conj ["water" "earth" "air"]))

(add-missing-elements "unobtainium", "admantium")

(defn my-partial
  [partialised-fn & args]
  (fn [& more-args]
    (apply partialised-fn (into args more-args))))

(def add20 (my-partial + 20))
(add20 3)

;; - Use partials when you need to repeat the same combination of function
;;   and arguments in different contexts, e.g. a specialised logger:

(defn lousy-logger
  [log-level message]
  (condp = log-level
    :warn (clojure.string/lower-case message)
    :emergency (clojure.string/upper-case message)))

(def warn (partial lousy-logger :warn))

(warn "Red light ahead")

;; ## `complement`
;; - Returns a function which mirrors the supplied function (side effects and all),
;;   but returns the inverse of what the original would have.

(def should-be-true (complement false))
(= true should-be-true)

(def not-vampire? (complement vampire?))
(defn identify-humans
  [social-security-numbers]
  (filter not-vampire?
          (map vampire-related-details social-security-numbers)))
