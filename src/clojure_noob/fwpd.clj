(ns clojure-noob.fwpd)

(def filename "resources/suspects.csv")

(def vamp-keys [:name :glitter-index])

(defn str->int
  [str]
  (Integer. str))

(def conversions {:name identity
                  :glitter-index str->int})

(defn convert
  [vamp-key value]
  ((get conversions vamp-key) value))

(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))

(defn mapify
  "Return a seq of maps like {:name \"Edward Cullen\" :glitter-index 10}"
  [rows]
  (map (fn [unmapped-row]
         (reduce (fn [row-map [vamp-key value]]
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row)))
       rows))

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))

(defn names
  "Retuns a seq of :name values from the records"
  [records]
  (map #(:name %)
       records))

(defn validate
  "Validates that a record includes the specified keywords"
  [keywords record]
  (every? #(contains? record %) keywords))

(defn append
  "Appends a new record to the map of records"
  [new-record records]
  (cond
    (validate vamp-keys new-record) (conj records new-record)))

(defn to-csv-pair
  "Converts a record map into a CSV row"
  [record]
  (clojure.string/join "," [(:name record) (:glitter-index record)]))

(defn records-to-csv
  "Writes the records map to outfile as a CSV"
  [records outfile]
  (spit outfile (clojure.string/join "\n" (map to-csv-pair records))))
