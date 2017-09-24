(ns dm-helper.components
  (:require
   [om.next :as om]
   [om.dom :as dom]
   [clojure.string :as str]
   [phrase.alpha :refer [phrase-first]]
   [dm-helper.utils :as utils]))

(defn compact-list
  [text]
  (dom/li #js {:className "no-margin" :key text} text))

(defn- get-column
  [fns coll]
  (map-indexed #(dom/td #js {:key %} %2) (map #(% coll) fns)))

(defn- header-column
  [on-click text]
  (dom/td #js {:className "bold" :onClick on-click} text))

(defn- empty-row
  [opts]
  [(dom/tr
    nil
    (dom/td
     (clj->js opts)
     "No matches"))])

(defn table
  [c coll table-columns on-header-click]
  (let [p (partition 2 table-columns)
        headers (map #(first %) p)
        content-fns (map #(second %) p)
        header-fn (partial header-column on-header-click)
        row-fn (partial dom/tr nil)
        col-fn (partial get-column content-fns)]
    (dom/table
     #js {:className "u-full-width"}
     (dom/thead
      nil
      (apply row-fn (map header-fn headers)))
     (apply dom/tbody
            nil
            (if (empty? coll)
              [(empty-row {:className "center" :colSpan (/ (count table-columns) 2)})]
              (map row-fn (map col-fn coll)))))))

(defn- new-value
  [formatter val]
  (let [f (:onSubmit formatter)
        v (utils/event-value val)]
    (if f
      (f v)
      v)))


(defn input
  [c form-data key opts]
  (let [formatter (:formatter opts)
        on-change #(om/transact! c `[(form/update-val! {:key ~key :val ~(new-value formatter %)})])
        presenter (:presenter formatter)
        value (key form-data)
        field-name (name key)]
    (dom/div
     #js {:className (or (:className opts) "u-full-width")}
     (dom/label #js {:htmlFor field-name} (str/capitalize field-name))
     (dom/input
      #js {:id field-name
           :value (or (if presenter
                        (presenter value)
                        value)
                      "")
      :className "u-full-width"
      :type (or (:type opts) "text")
      :onChange on-change
      :min (:min opts)
      :max (:max opts)})
     (if-let [error (phrase-first {} key (key form-data))]
       (dom/p
        #js {:className "error-text"}
        (str "*" (str/capitalize error)))))))
