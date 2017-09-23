(ns dm-helper.components.adventures
  (:require
   [clojure.string :as str]
   [om.next :as om]
   [om.dom :as dom]
   [dm-helper.adventure-dataset :as a]
   [dm-helper.components :as components]
   [dm-helper.utils :refer [->int]]))


(defn- get-levels [m]
  (let [[min max] (vals (select-keys m [::a/min-level ::a/max-level]))]
    (cond
      (= 0 (+ min max)) ""
      (= 0 min) max
      (= 0 max) min
      (= min max) min
      :else (str min "-" max))))

(defn- get-option
  [index [column-name key]]
  (dom/option
   #js {:value (subs (str key) 1)
        :key index} column-name))


(defn- ->table-format
  [adventure]
  (merge
   {}
   (select-keys adventure [::a/title ::a/storyline ::a/setting ::a/edition ::a/published ::a/publisher])
   {:authors (str/join ", " (sort (::a/authors adventure)))
    ::a/enemies (->> adventure ::a/enemies sort (map str/capitalize) (str/join ", "))
    ::a/levels (get-levels adventure)}))


(def ^:private table-columns
  ["Title" ::a/title
   ;; "Pages" :pages
   "Storyline" ::a/storyline
   "Setting" ::a/setting
   "Lvl" ::a/levels
   "Edition" ::a/edition
   ;; "Published" :published
   "Publisher" ::a/publisher
   ;; "Authors" :authors
   ;; "Enemies" :enemies
   ;; "environment" :environment
   ;; "format" :format
   ])


(defn- update-sort-column [c e]
  (let [v (keyword (str/lower-case (.-textContent (.-target e))))]
    (om/transact! c `[(search/update! {:key :search/sort-column :val ~v})])))

(defn- update-current-filter-column [c e]
  (let [v (keyword (.-value (.-target e)))]
    (om/transact! c `[(search/update! {:key :search/filter-column :val ~v})])))

(defn- clear-search
  [c e]
  (.preventDefault e)
  (om/transact! c `[(search/update! {:key :search/phrase :val ""})]))

(defn- update-search [c e]
  (.preventDefault e)
  (let [v (str/lower-case (.-value (.-target e)))]
    (om/transact! c `[(search/update! {:key :search/phrase :val ~v})])))

(defn- compare-levels
  [a b]
  (let [[amin amax] (str/split a "-")
        [bmin bmax] (str/split b "-")]
    (cond
      (= amin bmin) (compare (->int amax) (->int bmax))
      :default (compare (->int amin) (->int bmin)))))

(defn- custom-compare
  "Makes blank lines appear last"
  [key a b]
  (cond
    (str/blank? a) (.-MAX_SAFE_INTEGER js/Number)
    (str/blank? b) (.-MIN_SAFE_INTEGER js/Number)
    (= key :levels) (compare-levels a b)
    :default (compare a b)))

(defn- custom-filter
  [queried-column search-phrase adventure]
  (let [column (queried-column adventure)]
    (str/includes? (if (string? column)
                     (str/lower-case column)
                     (str column)) ;; Probably a number. Should support a more complex search < > =
                   search-phrase)))

(defn filter-sort
  [coll sort-by-column queried-column search-phrase]
  (->> coll
       (sort-by sort-by-column (partial custom-compare sort-by-column))
       (filter (partial custom-filter queried-column search-phrase))))


(defn- table
  [c coll search-phrase sort-column filter-column]
  (components/table
   c
   (filter-sort coll sort-column filter-column search-phrase)
   table-columns
   (partial update-sort-column c)))


(defn- table-filter
  [c search-phrase]
  (dom/form
   nil
   (dom/div
    #js {:className "row"}
    (dom/div
     #js {:className "twelve columns"}
     (dom/select
      #js {:className "two columns"
           :onChange (partial update-current-filter-column c)}
      (map-indexed get-option (partition 2 table-columns)))
     (dom/input
      #js {:className "eight columns"
           :type "text"
           :value search-phrase
           :onChange (partial update-search c)})
     (dom/button #js {:className "button-secondary two columns fa fa-times"
                     :type "submit"
                     :value "Clear"
                     :onClick (partial clear-search c)})))))


(defn adventure-table
  [c coll search-phrase column-name filter-column]
  (dom/div nil
           (table-filter c search-phrase)
           (table c (map ->table-format coll) search-phrase column-name filter-column)))
