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

(defmacro js-array-filter
  [ty]
  (let [f  (gensym)
        x  (gensym)
        y  (gensym)]
    `(aset (.-prototype ~ty) "filter"
           (fn [~f]
             (core/this-as this#
                           (into (empty this#)
                                 (comp
                                  (map-indexed vector)
                                  (filter (fn [[~x ~y]]
                                            (~f ~y ~x this#)))
                                  (map (fn [[_ ~y]]
                                         ~y)))
                                 this#))))))

(defmacro js-array-forEach
  [ty]
  (let [f  (gensym)]
    `(aset (.-prototype ~ty) "forEach"
           (fn [~f]
             (core/this-as this#
                           (dorun
                            (map-indexed #(~f %2 %1 this#) this#)))))))

(defmacro js-array-indexOf
  [ty]
  (let [s  (gensym)
        f  (gensym)]
    `(aset (.-prototype ~ty) "indexOf"
           (fn [~s ~f]
             (core/this-as this#
                           (let [~f  (or ~f 0)
                                 ~f  (max 0 (if (>= ~f 0)
                                              ~f
                                              (+ (count this#)
                                                 ~f)))]
                             (cond
                               (>= ~f (count this#)) -1
                               :else                 (or
                                                      (first (keep-indexed #(when (= %2 ~s) (+ %1 ~f))
                                                                           (subvec this# ~f)))
                                                      -1))))))))

(defmacro js-array-join
  [ty]
  (let [s  (gensym)]
    `(aset (.-prototype ~ty) "join"
           (fn [~s]
             (core/this-as this#
                           (clojure.string/join (or ~s ",") this#))))))
