(ns js-patch.core
  (:require [cljs.core :as core]))

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

;; https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/lastIndexOf
(defmacro js-array-lastIndexOf
  [ty]
  (let [s  (gensym)
        f  (gensym)
        c  (gensym)
        i  (gensym)]
    `(aset (.-prototype ~ty) "lastIndexOf"
           (fn [~s ~f]
             (core/this-as this#
                           (let [~c  (count this#)
                                 ~f  (or ~f (dec ~c))
                                 ~f  (if (>= ~f 0)
                                       (min ~f (dec ~c))
                                       (+ ~c
                                          ~f))]
                             (cond
                               (>= ~f ~c)   -1
                               :else        (or
                                             (first
                                              (keep (fn [~i]
                                                      (when (= (nth this# ~i) ~s)
                                                        ~i))
                                                    (reverse (range (inc ~f)))))
                                             -1))))))))

;; https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/map
(defmacro js-array-map
  [ty]
  (let [f  (gensym)]
    `(aset (.-prototype ~ty) "map"
           (fn [~f]
             (core/this-as this#
                           (into (empty this#)
                                 (map-indexed #(~f %2 %1 this#))
                                 this#))))))

;; https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/Reduce
(defmacro js-array-reduce
  [ty]
  (let [f    (gensym)
        i    (gensym)
        prev (gensym)
        idx  (gensym)
        val  (gensym)]
    `(aset (.-prototype ~ty) "reduce"
           (fn [~f ~i]
             (core/this-as this#
                           (if (nil? ~i)
                             (reduce (fn [~prev [~idx ~val]]
                                       (if (= 1 ~idx)
                                         (~f (last ~prev) ~val ~idx this#)
                                         (~f ~prev ~val ~idx this#)))
                                     (map-indexed vector this#))
                             (reduce (fn [~prev [~idx ~val]]
                                       (~f ~prev ~val ~idx this#))
                                     ~i
                                     (map-indexed vector this#))))))))


;; https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/ReduceRight
(defmacro js-array-reduceRight
  [ty]
  (let [f    (gensym)
        i    (gensym)
        prev (gensym)
        idx  (gensym)
        val  (gensym)
        c    (gensym)]
    `(aset (.-prototype ~ty) "reduceRight"
           (fn [~f ~i]
             (core/this-as this#
                           (if (nil? ~i)
                             (let [~c  (- (count this#) 2)]
                               (reduce (fn [~prev [~idx ~val]]
                                         (if (= ~c ~idx)
                                           (~f (last ~prev) ~val ~idx this#)
                                           (~f ~prev ~val ~idx this#)))
                                       (reverse (map-indexed vector this#))))
                             (reduce (fn [~prev [~idx ~val]]
                                       (~f ~prev ~val ~idx this#))
                                     ~i
                                     (reverse (map-indexed vector this#)))))))))

;; https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/reverse
(defmacro js-array-reverse
  [ty]
  `(aset (.-prototype ~ty) "reverse"
         (fn []
           (core/this-as this#
                         (into (empty this#)
                               (reverse this#))))))

;; https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/some
(defmacro js-array-some
  [ty]
  `(aset (.-prototype ~ty) "some"
         (fn [f#]
           (core/this-as this#
                         (let [r#  (some (fn [[idx# x#]]
                                           (f# x# idx# this#))
                                         (map-indexed vector this#))]
                           (if (nil? r#)
                             false
                             r#))))))
