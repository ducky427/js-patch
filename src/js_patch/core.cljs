(ns js-patch.core
  (:require-macros [js-patch.core :refer [js-array-map js-array-concat js-array-every
                                          js-array-filter js-array-forEach
                                          js-array-indexOf js-array-join
                                          js-array-lastIndexOf js-array-reduce
                                          js-array-reduceRight js-array-reverse
                                          js-array-some js-array-throw-missing]])
  (:require [clojure.string]))

(def ^:private missing-functions ["pop" "push" "shift" "slice" "sort" "splice" "unshift"])

(defn initialize!
  []
  (js-array-map PersistentVector)
  (js-array-concat PersistentVector)
  (js-array-every PersistentVector)
  (js-array-filter PersistentVector)
  (js-array-forEach PersistentVector)
  (js-array-indexOf PersistentVector)
  (js-array-join PersistentVector)
  (js-array-lastIndexOf PersistentVector)
  (js-array-reduce PersistentVector)
  (js-array-reduceRight PersistentVector)
  (js-array-reverse PersistentVector)
  (js-array-some PersistentVector)
  (doseq [n  missing-functions]
    (js-array-throw-missing PersistentVector n)))
