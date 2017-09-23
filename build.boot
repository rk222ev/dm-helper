(set-env!
 :source-paths    #{"src/cljs/main" "src/cljc/main"}
 :resource-paths  #{"resources"}
 :dependencies '[[org.clojure/clojure         "1.9.0-alpha17"]
                 [org.clojure/clojurescript   "1.9.908"]
                 [org.omcljs/om               "1.0.0-beta1"]
                 [compassus                   "1.0.0-alpha3"]
                 [bidi                        "2.1.2"]
                 [kibu/pushy                  "0.3.8"]
                 [phrase                      "0.1-alpha1"]


                 [com.cognitect/transit-clj   "0.8.300"        :scope "test"]
                 [com.cemerick/piggieback     "0.2.2"          :scope "test"]
                 [adzerk/boot-cljs            "2.1.3"          :scope "test"]
                 [adzerk/boot-cljs-repl       "0.3.3"          :scope "test"]
                 [adzerk/boot-reload          "0.5.2"          :scope "test"]
                 [crisptrutski/boot-cljs-test "0.3.4"          :scope "test"]
                 [deraen/boot-less            "0.6.2"          :scope "test"]
                 [org.slf4j/slf4j-nop         "1.8.0-alpha2"   :scope "test"]
                 [org.clojure/tools.nrepl     "0.2.13"         :scope "test"]
                 [pandeiro/boot-http          "0.8.3"          :scope "test"]
                 [weasel                      "0.7.0"          :scope "test"]])


(require
 '[adzerk.boot-cljs            :refer [cljs]]
 '[adzerk.boot-cljs-repl       :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload          :refer [reload]]
 '[crisptrutski.boot-cljs-test :refer [test-cljs]]
 '[deraen.boot-less            :refer [less]]
 '[pandeiro.boot-http          :refer [serve]])

(task-options!
  repl {:middleware '[cemerick.piggieback/wrap-cljs-repl]})

(deftask dev []
  (comp
    (serve)
    (watch)
    (cljs-repl)
    (reload :on-jsload 'dm-helper.core/init!)
    (speak)
    (less)
    (cljs :source-map true
          :optimizations :none
          :compiler-options {:parallel-build true}
          :ids #{"js/main"})
    (sift :move {#"dev.js" "main.js"})
    (target)))

(deftask release []
  (comp
    (less)
    (cljs :optimizations :advanced
      :ids #{"js/main"}
      :compiler-options {:parallel-build true
                         :elide-asserts true
                         :closure-defines {"goog.DEBUG" false}})
    (sift :move {#"dev.js" "main.js"})
    (target)))

(deftask testing []
  (set-env! :source-paths #(conj % "src/cljs/test"))
  identity)

(ns-unmap 'boot.user 'test)

(deftask test
  [e exit?     bool  "Exit after running the tests."]
  (let [exit? (cond-> exit?
                (nil? exit?) not)]
    (comp
      (testing)
      (test-cljs
        :js-env :node
        :namespaces #{'dm-helper.tests}
        :cljs-opts {:parallel-build true}
        :exit? exit?))))

(deftask auto-test []
  (comp
    (watch)
    (speak)
    (test :exit? false)))
