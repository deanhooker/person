(ns person.core
  "Core validation functions for use with schemas `person` schemas."
  (:require
   [malli.core :as m]
   [malli.error :as me]
   [person.schema :refer [valid-person]]))

(defn explain-person
  "Returns nil if valid, or a map of field -> human-readable error
  messages if invalid."
  [person]
  (when-let [problems (m/explain valid-person person)]
    (me/humanize problems)))

(defn validate-person
  "Returns the valid person if it conforms to the valid-person
  schema, otherwise nil."
  [person]
  (when (m/validate valid-person person)
    person))
