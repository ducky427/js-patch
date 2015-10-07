(ns js-patch.core
  (:require [cljs.core :as core]))

(defmacro js-array-map
  [ty]
  (let [f  (gensym)]
    `(aset (.-prototype ~ty) "map"
           (fn [~f]
             (core/this-as this#
                           (into (empty this#)
                                 (map-indexed #(~f %2 %1 this#) this#)))))))

(defmacro js-array-concat
  [ty]
  (let [args (gensym)]
    `(aset (.-prototype ~ty) "concat"
           (fn [& ~args]
             (core/this-as this#
                           (into (empty this#)
                                 (apply concat
                                        this#
                                        (map #(if (coll? %)
                                                %
                                                [%])
                                             ~args))))))))

(defmacro js-array-every
  [ty]
  (let [f  (gensym)
        x  (gensym)
        y  (gensym)]
    `(aset (.-prototype ~ty) "every"
           (fn [~f]
             (core/this-as this#
                           (every? (fn [[~x ~y]]
                                     (~f ~y ~x this#))
                                   (map-indexed vector this#)))))))
