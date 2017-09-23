(ns dm-helper.utils
  (:require
   [clojure.spec.alpha :as s]
   [clojure.string :as str]
   [om.next :as om]
   [dm-helper.adventure-dataset :as adventures]))

(defn strip-newline [s] (if (string? s) (str/replace s #"\n" "") s))
(def strip-newlines (partial map strip-newline))
(def join-with-comma (partial str/join ","))
(defn join-collections
  [coll]
  (map #(if (coll? %)
          (if (empty? %) ""
              (join-with-comma %))
          %)
       coll))

(defn- str-citations
  [coll]
  (map #(if (and (string? %) ((complement str/blank?) %))
          (str "\"" % "\"")
          %)
       coll))

(defn open-csv [e]
  (.preventDefault e)
  (let [headers (join-with-comma (map name (keys adventures/empty-adventure)))
        adv (map (comp join-with-comma str-citations join-collections strip-newlines vals) adventures/data)
        csv (js/encodeURI (clj->js (str "data:text/csv;charset=utf-8," (str/join "\n" (conj adv headers)))))]
    (js/window.open csv)))

(defn log
  [c]
  (js/console.log (clj->js (dissoc (om/props c) :adventures))))

(defn ->int
  [s]
  (js/parseInt s))

(defn join-non-nil-strings
  [coll]
  (str/join " " (remove str/blank? coll)))

(defn event-value
  [e]
  (.-value (.-target e)))

;;; Spec

(defn- get-req-un [spec] (.-req_un spec))
(defn- get-req [spec] (.-req spec))

(defn required
  [k]
  (let [spec (s/get-spec k)]
    (concat (get-req-un spec) (get-req spec))))
