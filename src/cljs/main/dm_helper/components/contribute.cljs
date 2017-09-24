(ns dm-helper.components.contribute
  (:require
   [clojure.spec.alpha :as s]
   [clojure.string :as str]
   [om.next :as om]
   [om.dom :as dom]
   [phrase.alpha :refer [phrase-first]]
   [dm-helper.adventure-dataset :as adventures]
   [dm-helper.utils :as utils]))


(def ^:private list-formatter {:presenter #(str/join "," %)
                     :onSubmit #(str/split % ",")})


(defn- post-adventure
  [c form e]
  (.preventDefault e)
  (let [validation (s/explain-data :dm-helper/adventure form)
        errors (:cljs.spec/problems validation)]
    (if (s/valid? :dm-helper/adventure form)
      (js/console.log "passed validation")
      (om/transact! c `[(form/update! {:val ~adventures/empty-adventure})]))))


(defn- new-value
  [formatter val]
  (let [f (:onSubmit formatter)
        v (utils/event-value val)]
    (if f
      (f v)
      v)))


(defn- input
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


(defn contribution-form [c adventure]
  (let [input-for (partial input c adventure)]
    (dom/form
     #js {:onSubmit (partial post-adventure c adventure)
          :className (when (not (empty? adventure)) "submitted")}
     (input-for ::adventures/title)
     (input-for ::adventures/authors {:formatter list-formatter})
     ;; Edition Dropdown
     (input-for ::adventures/enemies {:formatter list-formatter})
     (input-for ::adventures/environment)
     (input-for ::adventures/found-in)
     ;; Yes/No for :handouts
     #_(input-for :items)
     (dom/div #js {:className "row"}
              (input-for ::adventures/min-level {:className "three columns" :type "number" :min 1 :max 20})
              (input-for ::adventures/max-level {:className "three columns" :type "number" :min 1 :max 20}))
     (dom/div #js {:className "row"}
              (input-for ::adventures/min-characters {:className "three columns" :type "number"})
              (input-for ::adventures/max-characters {:className "three columns" :type "number"}))
     (dom/div #js {:className "row"}
              (input-for ::adventures/format {:className "three columns"}) ;; Dropdown
              (input-for ::adventures/pages {:className "three columns" :type "number"}))
     ;; year selector (input-for :published "three columns")
     (input-for ::adventures/publisher)
     (input-for ::adventures/setting)
     (input-for ::adventures/storyline)
     ;; TextArea for :summary
     ;; Yes/No for :tactical-maps
     #_(input-for :villains)
     (dom/input #js {:type "submit" :className "button-primary"}))))
