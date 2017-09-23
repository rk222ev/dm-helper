(ns dm-helper.components
  (:require
   [om.dom :as dom]))

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
