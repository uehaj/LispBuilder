/*
{setq t {quote t}}

{setq null {a -> {eq a nil}}}

{setq append
 {a b -> {if {null a}  b {cons {car a} {append {cdr a} b}}}}}

{setq reverse
  {x -> {if {null x} x
	  {append {reverse {cdr x}} {cons {car x} nil}}}}}

{setq not
  {x -> {if {null x} t nil}}}

{setq or
  {x y -> {if {not {null x}} t {if {not {null y}} t nil}}}}

{setq fib
  {n ->
	{if {or {= n 0} {= n 1}}
	  1
	  {+ {fib {- n 1}}
		{fib {- n 2}}}}}}

*/

def bx = new LispBuilder()
  /*
assert bx.build{$"ABC"}.toString() == "(ABC)"
assert bx.build{$1}.toString() == "(1)"
assert bx.build{ a; b; c; d; ${e; f; g; h} }.toString() ==
  "(a b c d (e f g h))"
assert bx.build{ a; b; c; d; ${e; f; g; h}; i; j; }.toString() ==
  "(a b c d (e f g h) i j)"
assert bx.build{ a; b; c; d; ${e; f; g; h}; i; j; ${ k }; l }.toString() ==
  "(a b c d (e f g h) i j (k) l)"

assert bx.build{${a}}.toString() ==
  "((a))"
assert bx.build{quote; $1;}.toString() ==
  "(quote 1)"

assert bx.build{
  ${ a; ${ b; ${ c; $1; $2; $3 }; d }; e } }.toString() ==
  '((a (b (c 1 2 3) d) e))'

assert bx.build{TRUE}.eval() == true
assert bx.build{FALSE}.eval() == false
assert bx.build{not; FALSE}.eval() == true
assert bx.build{not; TRUE}.eval() == false
assert bx.build{eq; $1; $1}.eval() == true
assert bx.build{eq; $1; $2}.eval() == false
assert bx.build{not; ${eq; $1; $1}}.eval() == false
assert bx.build{not; ${eq; $1; $2}}.eval() == true

assert bx.build{ $if; TRUE; $"it's true" }.eval() == "it's true"
assert bx.build{ $if; FALSE; $"it's true" }.eval() == false
assert bx.build{ $if; ${not; FALSE; }; $"it's true" }.eval() == "it's true"
assert bx.build{ $if; ${not; TRUE; }; $"it's true" }.eval() == false
assert bx.build{ $if; TRUE; $1; $2 }.eval() == 1
assert bx.build{ $if; FALSE; $1; $2 }.eval() == 2

assert bx.build{ setq; nullp; ${a}; ${eq; a; nil}}.toString() ==
  '(setq nullp (a) (eq a nil))'

assert bx.build{quote; a}.eval().isSymbol == true
assert bx.build{quote; $"a"}.eval().isSymbol == false
assert bx.build{car; ${quote; ${x; y}}}.eval() == 'x'
assert bx.build{cdr; ${quote; ${x; y}}}.eval().toString() == '(y)'
assert bx.build{cons; $1; $2}.eval().toString() == '(1 . 2)'
assert bx.build{cons; $1; ${cons; $2; $3}}.eval().toString() == '(1 2 . 3)'

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

assert bx.build{progn; $1; $2; $3}.eval() == 3
assert bx.build{${progn; ${setq; a; $77;}; a }}.eval() == 77

assert bx.build {progn
  ${setq; neq
    ${quote
      ${lambda
        ${x; y}
        ${not; ${eq; x; y}}
      }
    }}
  ${neq; $1; $2}
}.eval() == true

assert bx.build {progn
  ${setq; neq
    ${quote
      ${lambda
        ${x; y}
        ${not; ${eq; x; y}}
      }
    }}
  ${neq; $1; $1}
}.eval() == false

*/
  /*
println bx.build{progn
  ${setq; nullp;
    ${quote
      ${lambda
        ${x}
        ${eq; ${}; x}
      }
    }}
  ${nullp; $1}
}.eval()
  */

  //println bx.build{${quote; ${cons; nil; $1}}}.eval().toString()
println bx.build{cdr ${cons; a; b}}.eval().toString()


