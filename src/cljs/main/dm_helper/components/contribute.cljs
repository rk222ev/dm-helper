(ns dm-helper.components.contribute
  (:require
   [clojure.spec.alpha :as s]
   [clojure.string :as str]
   [om.next :as om]
   [om.dom :as dom]
   [dm-helper.adventure-dataset :as adventures]
   [dm-helper.components :as components]
   [dm-helper.utils :as utils]))

(def ^:private list-formatter
  {:presenter #(str/join ", " %)
   :on-submit #(str/split (str/replace % ", " ",") ",")})

(def ^:private number-formatter
  {:on-submit utils/->int})

(defn- post-adventure
  [c form e]
  (.preventDefault e)
  (let [validation (s/explain-data :dm-helper/adventure form)
        f (into {} (remove (comp nil? second)) (or form adventures/empty-adventure))
        errors (:cljs.spec/problems validation)]
    (if (s/valid? :dm-helper/adventure f)
      (js/console.log "passed validation")
      (om/transact! c `[(form/update! {:val ~f})]))))


(defn contribution-form [c adventure]
  (let [input-for (partial components/input c adventure)
        number {:className "three columns" :type "number" :formatter number-formatter}]
    (utils/log c)
    (dom/form
     #js {:onSubmit (partial post-adventure c adventure)}
     (input-for ::adventures/title)
     (input-for ::adventures/authors {:formatter list-formatter})
     ;; Edition Dropdown
     (input-for ::adventures/enemies {:formatter list-formatter})
     (input-for ::adventures/environment)
     (input-for ::adventures/found-in)
     ;; Yes/No for :handouts
     #_(input-for :items)
     (dom/div #js {:className "row"}
              (input-for ::adventures/min-level number)
              (input-for ::adventures/max-level number))
     (dom/div #js {:className "row"}
              (input-for ::adventures/min-characters number)
              (input-for ::adventures/max-characters number))
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
