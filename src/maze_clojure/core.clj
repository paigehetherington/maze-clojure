(ns maze-clojure.core
  (:gen-class))

(def size 10) ; grid size 10x10

(defn create-rooms []
  (vec
    (for [row (range size)]  ; a vec of vecs, for loop inside of for loop
      (vec
        (for [col (range size)]
          {:row row, :col col, :visited? false, :bottom? true, :right? true})))))
  

(defn possible-neighbors [rooms row col] ; contains only valid neighbors
  (vec
    (remove 
      (fn [room] ;anon fxn
        (or(nil? room) (:visited? room)))
      [(get-in rooms [(dec row) col]) ; one below
       (get-in rooms [(inc row) col]) ;one row above
       (get-in rooms [row (dec col)]) ;to left column
       (get-in rooms [row (inc col)])]))) ; to right column

(defn random-neighbor [rooms row col]
  (let [neighbors (possible-neighbors rooms row col)]
    (if (pos? (count neighbors))
      (rand-nth neighbors)
      nil)))

(defn tear-down-wall [rooms old-row old-col new-row new-col]
  (cond
    ;going up
    (< new-row old-row) ;new row less than old row
    (assoc-in rooms [new-row new-col :bottom?] false)
    ; going down
    (> new-row old-row)
    (assoc-in rooms [old-row old-col :bottom?] false)
    ;going left:
    (< new-col old-col)
    (assoc-in rooms [new-row new-col :right?] false)
    ;going right:
    (> new-col old-col)
    (assoc-in rooms [old-row old-col :right?] false)))

(defn set-end-if-necessary [rooms row col]
  (let [filtered-rooms (filter :end? (flatten rooms))]
     (if (pos? (count filtered-rooms))
       rooms
       (assoc-in rooms [row col :end?] true))))

(defn create-maze [rooms row col]
  (let [rooms (assoc-in rooms [row col :visited?] true)
        next-room (random-neighbor rooms row col)]
    (if next-room
        ;call create-maze recursively
        (loop [old-rooms (tear-down-wall rooms row col (:row next-room) (:col next-room))] ;creates local variable
          (let [new-rooms (create-maze old-rooms (:row next-room) (:col next-room))]
            (if (= old-rooms new-rooms)
              old-rooms
              (recur new-rooms))))
            
      (set-end-if-necessary rooms row col))));else if doesn't find room, stops. dead end when next room is nil
        

(defn -main []
  (let [rooms (create-rooms)
        rooms (create-maze rooms 0 0)
        rooms (assoc-in rooms [0 0 :start?] true)]
    ;print top walls
    (doseq [_ rooms]
      (print " _")) ; space first for vertical wall
    (println);makes new line after first wall
    ;print grid
    (doseq [row rooms] ;doseq (not lazy) loops over rooms and creates temp variable row
      (print "|")
      (doseq [room row] ; loops over row to create room
        (cond
          (:start? room)
          (print "O")
          (:end? room)
          (print "X")
          (:bottom? room)
          (print "_") ;if true print _
          :else
          (print " ")) ;if false print " "
        (if (:right? room)
          (print "|")
          (print " ")))
      (println))))
        
      
