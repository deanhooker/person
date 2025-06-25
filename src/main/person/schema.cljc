(ns person.schema
  "Schemas for modeling a person at various stages of validation."
  (:require
   [malli.core :as m]))

(def ^:private non-empty-string
  [:string {:min 1}])

(def editable-person
  (m/schema
   [:map
    [:first-name     {:optional true} [:maybe string?]]
    [:last-name      {:optional true} [:maybe string?]]
    [:ssn            {:optional true} [:maybe string?]]
    [:marital-status {:optional true} [:maybe string?]]
    [:us-phone       {:optional true} [:maybe string?]]]))

(def valid-person
  (m/schema
   [:map
    {:closed true}
    [:first-name     non-empty-string]
    [:last-name      non-empty-string]
    [:ssn            [:and non-empty-string [:re #"^\d{3}-\d{2}-\d{4}$"]]]
    [:marital-status [:enum "single" "married" "divorced" "widowed"]]
    [:us-phone       [:and non-empty-string [:re #"^\(\d{3}\) \d{3}-\d{4}$"]]]]))
