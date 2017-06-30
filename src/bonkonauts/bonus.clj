(ns bonkonauts.bonus
  (:import (clojure.lang RT Util APersistentMap APersistentSet
                         IPersistentMap IPersistentSet IPersistentStack
                         Box MapEntry SeqIterator)))

;; Create a basic Copy on Write (https://en.wikipedia.org/wiki/Copy-on-write) vector

;; Functions that will help
; Util/isInteger (checks if value is an integer
; Util/hasheq (checks if two objects hashes are equal)
; hash-ordered-coll (takes an order collection and returns a Clojure hash)

;; Skeleton of Copy On Write vector
;; The vector holds an array of values and a metadata field
(deftype COWVector [arr _meta]
  clojure.lang.IHashEq
  (hasheq [this])

  clojure.lang.Indexed
  (nth [this n]
    (aget arr n))

  (nth [this n not-found]
    (if (and (<= 0 n) (< n (alength arr)))
      (aget arr n)
      not-found))

  clojure.lang.Counted
  (count [this]
    (alength arr))

  clojure.lang.IMeta
  (meta [this])

  clojure.lang.IObj
  (withMeta [this m])

  clojure.lang.IPersistentCollection
  (cons [this x])

  (empty [this])

  (equiv [this that])

  clojure.lang.IPersistentStack
  (peek [this])

  (pop [this])

  clojure.lang.IPersistentVector
  (assocN [this i x])

  (length [this])

  clojure.lang.Reversible
  (rseq [this])

  clojure.lang.Associative
  (assoc [this k v])

  (containsKey [this k])

  (entryAt [this k])

  clojure.lang.ILookup
  (valAt [this k not-found])

  (valAt [this k])

  clojure.lang.IFn
  (invoke [this k])

  (applyTo [this args])

  clojure.lang.Seqable
  (seq [this]
    (when (> (alength arr) 0)
      (let [vector-seq (fn vector-seq [i]
                         (lazy-seq
                          (when (< i (alength arr))
                            (cons (aget arr i) (vector-seq (inc i))))))]
        (vector-seq 0)))))

;; Constructor function for Copy on Write Vector
(defn cow-vector [& more]
  (->COWVector (object-array more) {}))

(= [] (cow-vector))
(= 3 (count (cow-vector 1 2 3)))
(= 1 (nth (cow-vector 1 2 3) 0))
(= "Not found" (nth (cow-vector 1 2 3) 5 "Not found"))
(= [1 2 3] (cow-vector 1 2 3))
