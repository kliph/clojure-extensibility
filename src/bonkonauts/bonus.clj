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
  (hasheq [this]
    (hash-ordered-coll (seq this)))

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
  (meta [this]
    (throw (Exception.)))

  clojure.lang.IObj
  (withMeta [this m]
    (throw (Exception.)))

  clojure.lang.IPersistentCollection
  (cons [this x]
    (throw (Exception.)))

  (empty [this]
    (throw (Exception.)))

  (equiv [this that]
    (throw (Exception.)))

  clojure.lang.IPersistentStack
  (peek [this]
    (throw (Exception.)))

  (pop [this]
    (throw (Exception.)))

  clojure.lang.IPersistentVector
  (assocN [this i x]
    (throw (Exception.)))

  (length [this]
    (throw (Exception.)))

  clojure.lang.Reversible
  (rseq [this]
    (throw (Exception.)))

  clojure.lang.Associative
  (assoc [this k v]
    (throw (Exception.)))

  (containsKey [this k]
    (throw (Exception.)))

  (entryAt [this k]
    (throw (Exception.)))

  clojure.lang.ILookup
  (valAt [this k not-found]
    (throw (Exception.)))

  (valAt [this k]
    (throw (Exception.)))

  clojure.lang.IFn
  (invoke [this k]
    (throw (Exception.)))

  (applyTo [this args]
    (throw (Exception.)))

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
(= (Util/hasheq (cow-vector 1 2 3))
   (Util/hasheq (cow-vector 1 2 3)))

(not= (Util/hasheq (cow-vector 1 2 3))
      (Util/hasheq (cow-vector 1 2 4)))

(identical? (cow-vector 1 2 3)
            (cow-vector 1 2 3))
