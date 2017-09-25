(ns dm-helper.views
  (:require
   [clojure.string :as str]
   [om.next :as om :refer-macros [defui]]
   [om.dom :as dom]
   [dm-helper.adventure-dataset :as adventures]
   [dm-helper.texts :as texts]
   [dm-helper.components :as components]
   [dm-helper.components.adventures :refer [adventure-table]]
   [dm-helper.components.contribute :refer [contribution-form]]
   [dm-helper.utils :as utils]))

(defn- menu-item [url handler text] (dom/a #js {:href url} text))

(defn- navigation-menu []
  (dom/nav
   nil
   (menu-item "/" :adventures (dom/img #js {:id "logo" :src "images/d20-transp.png"}))
   (menu-item "contribute" :contribute "Contribute")
   (menu-item "about" :about "About")))


(defn view
  [children]
  (dom/div
   #js {:className "container"}
   (navigation-menu)
   children))

(defui ^:once About
  static om/IQuery
  (query [this]
         [:app/title])
  Object
  (render [this]
          (let [{:keys [app/title]} (om/props this)]
            (view
             (dom/main
              nil
              (dom/h3 nil "About")
              (dom/p #js {:dangerouslySetInnerHTML #js {:__html texts/about}})
              (dom/h3 nil "Download dataset as CSV")
              (dom/p #js {:dangerouslySetInnerHTML #js {:__html texts/download-dataset}})
              (dom/form nil (dom/button #js {:onClick utils/open-csv} "Download"))
              (dom/h3 nil "Contribute")
              (dom/p #js {:dangerouslySetInnerHTML #js {:__html texts/contribute}})
              (dom/ul nil
                      (map
                       (comp components/compact-list str/capitalize name)
                       (keys adventures/empty-adventure))))))))


(defui ^:once Adventures
  static om/IQuery
  (query [this]
         [:app/title :search/phrase :search/sort-column :search/filter-column :adventures])
  Object
  (render [this]
          (let [{:keys
                 [app/title adventures search/phrase search/filter-column
                  search/sort-column]} (om/props this)]
            (view
             (dom/main
              nil
              (dom/h3 nil "Adventures")
              (adventure-table this adventures phrase sort-column filter-column))))))


(defui ^:once Contribute
  static om/IQuery
  (query [this]
         [:form/adventure :form/errors])
  Object
  (render
   [this]
   (let [{:keys [form/adventure form/errors]} (om/props this)]
     (view
      (dom/main
       nil
       (dom/h3 nil "Contribute")
       (contribution-form this adventure))))))
