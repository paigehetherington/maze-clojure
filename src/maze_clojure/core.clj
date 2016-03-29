(ns maze-clojure.core
  (:gen-class))

(def size 10) ; grid size 10x10

(defn create-rooms []
  (vec
    (for [row (range size)]  ; a vec of vecs, for loop inside of for loop
      (vec
        (for [col (range size)]
          {:row row, :col col, :visited? false, :bottom? true, :right? true})))))
  
  

(defn -main []
  (let [rooms (create-rooms)]
    ;print top walls
    (doseq [_ rooms]
      (print " _")) ; space first for vertical wall
    (println);makes new line after first wall
    ;print grid
    (doseq [row rooms] ;doseq (not lazy) loops over rooms and creates temp variable row
      (print "|")
      (doseq [room row] ; loops over row to create room
        (if (:bottom? room)
          (print "_") ;if true print _
          (print " ")) ;if false print " "
        (if (:right? room)
          (print "|")
          (print " ")))
      (println))))
        
      
