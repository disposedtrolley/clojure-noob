(ns clojure-noob.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "I'm a little teapot!"))

(defn train
  []
  (println "Choo choo!"))


(if true
  "By Zeus's hammer!"
  "By Aquaman's trident!")

(if true
  (do (println "success")
      "By Zeus's hammer!")
  (do (println "failure")
      "By Aquaman's trident!"))

(when true
  (println "success")
  "abra cadabra")

;; // ======================
;; Naming Values with def

(def failed-protagonist-names
  "Binds the name `failed-protagonist-names` to a vector of three strings"
  ["Larry Potter" "Doreen the Explorer" "The Incredible Bulk"])

;; Clojure encourages bound values to be immutable.
;; Don't do this:
(def severity :mild)
(def error-message "OH GOD! IT'S A DISASTER! WE'RE ")
(if (= severity :mild)
  (def error-message (str error-message "MILDLY INCONVENIENCED!"))
  (def error-message (str error-message "DOOOOMED!")))

;; Prefer this:
(defn error-message
  [severity]
  (str "OH GOD! IT'S A DISASTER! WE'RE "
       (if (= severity :mild)
         "MILDLY INCONVENIENCED!"
         "DOOOOOOMED!")))
(error-message :mild)

;; // ======================
;; Data Structures
;; All Clojure data structures are immutable. Clojure encourages the use of
;; built-in data structures before making your own types or classes.
;;
;; It is better to have 100 functions operate on one data structure than 10
;; functions on 10 data structures.
;; -- Alan Perlis

;; Maps
{}

{:first-name "Charlie"
 :last-name "McFishwich"}

;; Values can be functions.
{"string-key" +}

;; Supports nesting.
{:name {:first "John"
        :middle "Jacob"
        :last "Jingleheimerschmidt"}}

;; (hash-map) can be used to create a map (alternative to a literal map).
(hash-map :a 1 :b 2)

;; Look up values with (get)
(get {:a 0 :b 1} :b)
(get {:a 0 :b 1} :c "unicorns?")                            ;; default value

;; Look up nested values with (get-in)
(get-in {:a 0 :b {:c "ho hum"}} [:b :c])                    ;; get :c in :b

;; Look up value by treating the literal map as a function!
({:name "The Human Coffeepot"} :name)


;; Keywords
;; Primarily used as keys for maps.

;; Can also be used as a function to look up a value in a map!
(:a {:a 1 :b 2 :c 3})
(:d {:a 1 :b 3 :c 3} "No gnome knows homes like Noah knows")


;; Vectors
;; Similar to an array - zero indexed.
[3 2 1]

;; Get the 0th element.
(get [3 2 1] 0)

;; Vectors can contain elements of any type.
(get ["a" {:name "Pugsley Winterbottom"} "c"] 1)

;; Can be created with (vector).
(vector "creepy" "full" "moon")

;; (conj) can be used to append elements to a vector.
(conj [1 2 3] 4)


;; Lists
;; Similar to vectors, but elements can't be retrieved with (get).
;; Use a list when you need to easily prepend items or if you're writing a
;; macro.
'(1 2 3 4)

;; Can be created with (list), and also supports a mix of types.
(list 1 "two" {3 4})

;; Get the 0th element.
;; NB: (nth) is slower than (get).
(nth '(:a :b :c) 0)

;; (conj) can be used to _prepend_ elements to a list.
(conj '(1 2 3) 4)


;; Sets
;; Collections of unique values. Clojure has two kinds: hash sets and sorted
;; sets.

;; Hash Set
#{"kurt vonnegut" 20 :icicle}
(hash-set 1 1 2 2)

;; Uniqueness of values is preserved when adding to the set.
(conj #{:a :b} :b)

;; Check set membership using (contains?) -- returns a boolean
(contains? #{:a :b} :a)
;; or a keyword -- returns the value or nil
(:a #{:a :b})
;; or with (get) -- returns the value or nil, but checking for nil always returns nil



;; // ======================
;; Functions
;; A `function call` is just another term for an operation where the operator
;; is a function or a `function expression` (an expression which returns a
;; function.
(or + -)                                                    ;; returns +
;; These all return 6.
((or + -) 1 2 3)
((and (= 1 1) (= 2 2) +) 1 2 3)
((first [+ 0]) 1 2 3)

;; These are invalid function calls and produce a ClassCastException:
;;     ClassCastException java.lang.String cannot be cast to clojure.lang.IFn
;; a very common error which typically means you're trying to use something
;; as a function when it's not.
(1 2 3 4)
("test" 1 2 3)

;; Higher-order functions
;; Functions which take functions as arguments.
(map inc [0 1 2 3])
;; Clojure lets you create functions that generalise over _processes_, i.e
;; (map) generalises the process of transforming a collection by applying any
;; function over any collection.


;; Defining Functions
(defn too-enthusiastic
  "Return a cheer that might be a bit too enthusiastic"
  [name]
  (str "OH. MY. GOD! " name " YOU ARE MOST DEFINITELY LIKE THE BEST "
       "MAN SLASH WOMAN EVER I LOVE YOU AND WE SHOULD RUN AWAY SOMEWHERE"))
(too-enthusiastic "Zelda")
;; The docstring for a function can be viewed using (doc), i.e. (doc too-enthusiastic)
(doc too-enthusiastic)

;; Functions can be defined with zero or more parameters.
(defn no-params
  []
  "I take no parameters!")                                  ;; 0-arity
(defn one-param
  [x]
  (str "I take one parameter: " x))                         ;; 1-arity
(defn two-params
  [x y]
  (str "Two parameters! That's nothing! Pah! I will smoosh them "
       "together to spite you! " x y))                      ;; 2-arity

;; Functions can also be variable-arity through `arity overloading`.
(defn multi-arity
  ([first-arg second-arg third-arg]
   (str first-arg second-arg third-arg))
  ([first-arg second-arg]
   (str first-arg second-arg))
  ([first-arg]
   (str first-arg)))
;; A good use case for this is providing default values for arguments.
(defn x-chop
  "Describe the kind of chop you're inflicting on someone"
  ([name chop-type]
   (str "I " chop-type " chop " name "! Take that!"))
  ([name]
   (x-chop name "karate")))
;; Rest parameters can be used to define variable-arity functions too.
(defn codger-communication
  [whippersnapper]
  (str "Get off my lawn, " whippersnapper "!!!"))
(defn codger
  [& whippersnappers]                                       ;; `whippersnappers` becomes a list
  (map codger-communication whippersnappers))
(codger "Billy" "Anne-Marie" "The Incredible Bulk")

(defn favourite-things
  [name & things]
  (str "Hi, " name ", here are my favourite things: "
       (clojure.string/join ", " things)))
(favourite-things "Doreen" "gum" "shoes" "kara-te")


;; Destructuring
(defn my-first
  "Returns the first element of a collection"
  [[first-thing]]                                           ;; within a vector
  first-thing)
;; can name as many elements as you want, and introduce rest parameters.
(defn chooser
  [[first-choice second-choice & unimportant-choices]]
  (println (str "Your first choice is: " first-choice))
  (println (str "Your second choice is: " second-choice))
  (println (str "We're ignoring the rest of your choices. "
                "Here they are in case you need to cry over them: "
                (clojure.string/join ", " unimportant-choices))))
(chooser ["Marmalade" "Handsome Jack" "Pigpen" "Aquaman"])
;; can also destructure maps.
(defn announce-treasure-location
  [{lat :lat lng :lng}]
  (println (str "Treasure lat: " lat))
  (println (str "Treasure lng: " lng)))
;; :keys can be used as a shortcut to destructure map keys
(defn announce-treasure-location
  [{:keys [lat lng]}]
  (println (str "Treasure lat: " lat))
  (println (str "Treasure lng: " lng)))
;; access to the original map argument can be retained using the :as keyword
(defn receive-treasure-location
  [{:keys [lat lng] :as treasure-location}]
  (println (str "Treasure lat: " lat))
  (println (str "Treasure lng: " lng))

  treasure-location)


;; Function Body
;; Can contain forms of any kind. Clojure returns the last form evaluated.
;;
;; Clojure has no "privileged functions", +, -, inc, and map are no better than
;; the functions you define yourself.
(defn illustrative-function
  []
  (+ 1 304)  ;; form
  30         ;; form
  "joe")     ;; form (last evaluated)
(defn number-comment
  [x]
  (if (> x 6)
    "Oh my gosh! What a big number!"
    "That number's OK, I guess"))


;; Anonymous Functions

;; `fn` form
(map (fn [name] (str "Hi, " name))
     ["Darth Vader" "Mr. Magoo"])
((fn [x] (* x 3)) 8)

;; anonymous functions can be associated with a name using (def)
(def my-special-multiplier (fn [x] (* x 3)))

;; compact form (reader macros)
(#(* % 3) 8)
(map #(str "Hi, " %)
     ["Darth Vader" "Mr. Magoo"])
;; % indicates the argument being passed to the function. Multiple arguments
;; can be passed as %1, %2, %3... (% == %1)
(#(str %1 " and " %2) "cornbread" "butter beans")
;; rest parameters can be passed with %&
(#(identity %&) 1 "blarg" :yip)


;; Returning Functions
;; Functions can return other functions, called `closures`. They can access all
;; variables which were in scope when the function was created.
(defn inc-maker
  "Create a custom incrementor"
  [inc-by]
  #(+ % inc-by))

(def inc3 (inc-maker 3))



;; // ======================
;; Let
;; Binds names to values. The value of a let form is the last form in the body
;; that's evaluated.
;; Useful because:
;;   1) they provide clarity through names
;;   2) they allow you to evaluate an expression and reuse the result
;;        - good for expensive operations like network requests
(let [x 3] x)  ;; x is the last form which evaluates to 3.

(def dalmatian-list
  ["Pongo" "Perdita" "Puppy 1" "Puppy 2"])
(let [dalmatians (take 2 dalmatian-list)]
  dalmatians)

;; let also introduces a new scope.
(def x 0)
(let [x 1] x)
;; but you can reference existing bindings too:
(def x 0)
(let [x (inc x)] x)  ;; (inc x) refers to the x declared in (def x 0)
;; and use rest parameters:
(let [[pongo & dalmatians] dalmatian-list]
  [pongo dalmatians])



;; // ======================
;; Loop
(loop [iteration 0]
  (println (str "Iteration " iteration))
  (if (> iteration 3)
    (println "Goodbye!")
    (recur (inc iteration))))
