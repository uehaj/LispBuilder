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

class LispBuilder {

  def read(Closure c) {
    def listReader = new ListReader()
    c.delegate = listReader
    c.call()
    return listReader.readResult()
  }
}

def bx = new LispBuilder()
  //assert b.eval{[1+3, 2, [5]]}.toString() == "(4 2 [5] ) "
  /*
println bx.read{a
                {b{d}}
                $0}

println bx.read{$1
                {$"a b c"}
                $0}

println bx.read{$0
                {$"a b c"}
                $1
                $2
                $"abc"
                $"def"
                }
  */
  /*
println bx.read{->$0
                println "[0]" 
                {->  $1; println "[1]"   }
                ${-> println "[2]"; a; println "[3]"  }
                ${-> println "[4]"; b; println "[5]"   }
                println "[6]"
                }
  */

assert bx.read{
  ${ a; ${ b; ${ c; $1; $2; $3 }; d }; e; } }.toString() ==
  '((a (b (c 1 2 3) d) e))'

assert bx.read {setq; nullp; ${a}; ${eq; a; nil}}.toString() ==
  '(setq nullp (a) (eq a nil))'

assert bx.read{eq; $1; $1}.eval() == true
assert bx.read{eq; $1; $2}.eval() == false
assert bx.read{not; ${eq; $1; $2}}.eval() == true
assert bx.read{$1}.eval() == 1
assert bx.read{$"ABC"}.eval() == "ABC"
assert bx.read{TRUE}.eval() == true
assert bx.read{FALSE}.eval() == false
assert bx.read{ IF; TRUE; $"it's true" }.eval() == "it's true"

println bx.read{ IF; FALSE; $"it's true" }.eval()
assert bx.read{ IF; ${eq; $1; $2}; $"it's true" }.eval() == false
assert bx.read{ IF; ${eq; $1; $2}; $"it's true"; $"it's false" }.eval() ==
  "it's false"



