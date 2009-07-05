def bx = new LispBuilder()

assert bx.build{${quote
                  ${lambda
                    ${a; b}
                    ${eq; a; b}}}
                $1
                $1 }.eval() == true

assert bx.build{${quote
                  ${lambda
                    ${a; b}
                    ${eq; a; b}}}
                $1
                $2 }.eval() == false

assert bx.build {progn
  ${defun; _not; ${x}
    ${IF; x; nil; TRUE}}

                 ${and
                   ${not; ${_not; TRUE}}
                   ${not; ${_not; $1}}
                   ${not; ${_not; ${_not; FALSE}}}
                 }
}.eval() == true

assert bx.build {progn
  ${defun; _neq; ${x; y}
    ${not; ${eq; x; y}}
  }

                  ${and; 
                    ${_neq; $1; $2}
                    ${_neq; $3; nil}
                    ${not; ${_neq; $1; $1}}
                    ${not; ${_neq; nil; nil}}
                  }
}.eval() == true

assert bx.build{progn
  ${defun; _nullp; ${x}
    ${eq; nil; x}
  }
                ${and;
                  ${_nullp; nil}
                  ${not; ${_nullp; FALSE}}
                  ${not; ${_nullp; TRUE}}
                  ${not; ${_nullp; $1}}
                }
}.eval() == true

assert bx.build{progn
  ${defun; _append; ${a; b}
    ${IF;
      ${nullp; a}
      b;
      ${cons; ${car; a}; ${_append; ${cdr; a}; b}}
    }
  }

                ${_append; ${quote; ${$1; $2}}; ${quote; ${$3; $4}}}

}.eval().toString() == "(1 2 3 4)"

assert bx.build{progn
  ${defun; _reverse; ${x}
    ${IF
      ${nullp; x}
      x
      ${append
        ${_reverse
          ${cdr; x}}
        ${cons; ${car; x}; nil}}
    }
  }
                 ${and
                   ${equal
                     ${_reverse; ${quote; ${$1; $2}}}
                     ${quote; ${$2; $1}}}
                   ${equal
                     ${_reverse; ${quote; ${a; ${c; b}; d}}}
                     ${quote; ${d; ${c; b}; a}}
                   }}
}.eval() == true

// Fibonacci number by Lazy evaluation
def zip(a,b) { new Cons(a.car+b.car, {zip(a.cdr, b.cdr)}); }
fibs = new Cons(0, new Cons(1, {zip(fibs, fibs.cdr)}))
assert fibs[34] == 5702887

assert bx.build{progn
  ${defun; fib; ${n}
    ${IF; ${or; ${equal; n; $1}; ${equal; n; $2}}
      $1
      ${add; ${fib; ${add; n; $(-1)}}
        ${fib; ${add; n; $(-2)}}}}}

  ${fib; $10}

}.eval() == fibs[10]


assert bx.build{progn;
                 ${defun
                   dbl
                   ${x}
                   ${add; x; x}}
                 ${dbl; $3}
}.eval() == 6;
