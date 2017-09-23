(ns dm-helper.texts
  (:require
   [clojure.string :as str]
   [dm-helper.adventure-dataset :refer [empty-adventure]]))


(def about
  "Insipired by Matt Colville and his proposal for <a href=\"https://adventurelookup.com\">
  Adventurelookup.com</a>.
  I decided to make this prototype experimenting with the idea, technology and because
  I actually had a need for an easy to use database of adventures.")


(def contribute
  (str
   "At the moment contributing is done by sending an email to info(at)adventurelookup.rpkn.se
 where the information below is entered and then it gets manually checked and entered to the Database.
Depedning on how the idea turns out I may add some kind of automated way of adding adventures.
The current idea I'm thinking about is a cloud hosted Elasticsearch instance or similar that provides
 a stable and open platform to enable many different UI implementations using the same dataset. "))

(def download-dataset
  "As a way to keep the data that has been collected by the community open, all data is available to
download as a file with comma separated values (CSV). The thought is that the community who's collected
the data also should have the option to easily get their hand on it and do other cool stuff.")
