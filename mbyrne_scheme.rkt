; x^2
(define (square x)
  (* x x))

; (x2 - x1)^2
(define (subAndSquare x1 x2)
  (square (- x1 x2)))

; sqrt ( (x2 - x1)^2 + (y2 - y1)^2 + (z2 - z1)^2 )
(define (distance p1 p2)
  (sqrt (+ (subAndSquare (car p1) (car p2))
     (subAndSquare (car (cdr p1)) (car (cdr p2)))
     (subAndSquare (car (cdr (cdr p1))) (car (cdr (cdr p2)))))))

; recurse to find closest - if list null return currentClosest, otherwise,
; if head of list closer than currentClosest, recurse with head of list as new currentClosest
; else recurse with same currentClosest
(define (findClosest p listPoints currentClosest)
  (if (null? listPoints) currentClosest
      (if (< (distance p (car listPoints)) (distance p currentClosest))
          (findClosest p (cdr listPoints) (car listPoints))
          (findClosest p (cdr listPoints) currentClosest))))

(define (findSmallest p l)
  (let ((closest (findClosest p (cdr l) (car l))))
  (list (distance p closest) p closest)))

(findSmallest '(12 6 5) '((1 4 3) (7 2 5) (9 3 22) (12 5 9)))