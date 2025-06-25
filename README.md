# Person Validation Module

This project defines two schemas for representing a `Person` and provides validation functions designed to support form editing and structural validation.

## Overview

While in the process of being edited, form data exists in an incomplete or invalid state. Once the form is submitted the data should be fully valid. This project models these concepts with two schemas:

- `editable-person`: A permissive schema that allows partial or invalid input; suitable for live form editing.
- `valid-person`: A strict schema representing fully validated data that can be safely used throughout the application.

This project uses [Malli](https://github.com/metosin/malli), a data-driven schema library for Clojure/Script.

## Module Structure

### `person.schema`
- `editable-person`: Open, permissive schema for partially filled or invalid form data.
- `valid-person`: Closed, strict schema used after validation.

### `person.core`
- `validate-person`: `editable-person -> Maybe valid-person`. Returns the input if it conforms to `valid-person`, otherwise returns `nil`.
- `explain-person`: `editable-person -> Maybe (Map Field [String])`. Returns a map field->error if validation fails, otherwise returns `nil`.

## Design Philosophy

### Data-Driven Design

"Just use maps." A `Person` and the schemas are just maps. This design avoids unnecessary abstraction and leverages Malli’s ability to treat validation logic as composable, inspectable data.

### Composability

`validate-person` is intended for truthy-style composition, returning either a valid value or `nil`. Errors can be retrieved separately with `explain-person`. This follows idiomatic Clojure practices, favoring simple functions and ease of composition.

### Safety

The `valid-person` schema is closed to enforce a strict contract, ensuring impossible states are unrepresentable.

### Functional

All validation functions are pure. Clojure's core data structures are immutable by default.

## Usage
```
(ns example.app
  (:require
    [person.core :as person]
    [person.schema :as schema]))

;; Example: editable input from a user form
(def form-input
  {:first-name     "Ada"
   :last-name      "Lovelace"
   :ssn            "123-45-6789"
   :marital-status "single"
   :us-phone       "(123) 456-7890"})

;; Validate the form data
(if-let [valid-person (person/validate-person form-input)]
  (println "Valid person:" valid-person)
  (println "Validation failed:" (person/explain-person form-input)))
```
Example invalid input:
```
(def bad-input
  {:first-name ""
   :ssn        "not-a-ssn"})

(person/validate-person bad-input)
;; => nil

(person/explain-person bad-input)
;; => {:first-name ["should be at least 1 character"],
;;     :last-name ["missing required key"],
;;     :ssn ["should match regex"],
;;     :marital-status ["missing required key"],
;;     :us-phone ["missing required key"]}
```
## Notes

This project is incomplete until unit tests are added. Malli schemas are compatible with Clojure's property-based testing framework [test.check](https://github.com/clojure/test.check).
