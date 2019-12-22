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
