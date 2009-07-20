def bx = new LispBuilder2()

// Fibonacci number by Lazy evaluation
def zip(a,b) { new Cons(a.car+b.car, {zip(a.cdr, b.cdr)}); }
fibs = new Cons(0, new Cons(1, {zip(fibs, fibs.cdr)}))
assert fibs[34] == 5702887

// Fibonacci number by recursive call
assert bx.build{$($(progn,
                     $(defun, fib, $(n),
                       $(IF, $(or, $(equal, n, 1), $(equal, n, 2)),
                         1,
                         $(add, $(fib, $(add, n, -1)),
                           $(fib, $(add, n, -2))))),
                     $(fib, 10)))
}.eval() == fibs[10]


