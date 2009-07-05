def bx = new LispBuilder()

// Sqrt by newton method

assert bx.build{${add; $(1.3); $(3.2)} }.eval() == 1.3+3.2
assert bx.build{${add; $(1.3); $(3.2); $(4.2)} }.eval() == 1.3+3.2+4.2
assert bx.build{${sub; $(1.3); $(3.2)} }.eval() == 1.3-3.2
assert bx.build{${sub; $(1.3); $(3.2); $(4.2)} }.eval() == 1.3-3.2-4.2
assert bx.build{${mul; $(1.3); $(3.2);} }.eval() == 1.3*3.2
assert bx.build{${mul; $(1.3); $(3.2); $(4.2)} }.eval() == 1.3*3.2*4.2
assert bx.build{${div; $(1.3); $(3.2)} }.eval() == 1.3/3.2
assert bx.build{${div; $(1.3); $(3.2); $(4.2)} }.eval() == 1.3/3.2/4.2

assert bx.build{lt; $(1.1); $(1.1)}.eval() == (1.1 < 1.1)
assert bx.build{lt; $(1.1); $(2.2)}.eval() == (1.1 < 2.2)
assert bx.build{lt; $(2.2); $(1.1)}.eval() == (2.2 < 1.1)
assert bx.build{le; $(1.1); $(1.1)}.eval() == (1.1 <= 1.1)
assert bx.build{le; $(1.1); $(2.2)}.eval() == (1.1 <= 2.2)
assert bx.build{le; $(2.2); $(1.1)}.eval() == (2.2 <= 1.1)
assert bx.build{gt; $(1.1); $(1.1)}.eval() == (1.1 > 1.1)
assert bx.build{gt; $(1.1); $(2.2)}.eval() == (1.1 > 2.2)
assert bx.build{gt; $(2.2); $(1.1)}.eval() == (2.2 > 1.1)
assert bx.build{ge; $(1.1); $(1.1)}.eval() == (1.1 >= 1.1)
assert bx.build{ge; $(1.1); $(2.2)}.eval() == (1.1 >= 2.2)
assert bx.build{ge; $(2.2); $(1.1)}.eval() == (2.2 >= 1.1)

def env1 = new Env()
bx.build{setq; a; $1}.eval(env1)
assert bx.build{a}.eval(env1) == 1
assert bx.eval(env1){a} == 1

def env = new Env()
bx.eval(env){defun; abs; ${x}
             ${IF; ${lt; x; $0}; ${mul; x; $(-1)}; x}}

assert bx.eval(env){abs; $1} == 1
assert bx.eval(env){abs; $(1.2)} == 1.2
assert bx.eval(env){abs; $(-1)} == 1
assert bx.eval(env){abs; $(-1.2)} == 1.2
assert bx.eval(env){abs; $0} == 0

bx.eval(env){defun; average; ${x; y}
             ${div; ${add; x; y}; $2}}

assert bx.eval(env){average; $1; $2} == 1.5
assert bx.eval(env){average; $2; $4} == 3
assert bx.eval(env){average; $(1.0); $(2.0)} == 1.5
assert bx.eval(env){average; $(2.0); $(4.0)} == 3


bx.eval(env){defun; improve; ${y; x}
             ${average; y; ${div; x; y}}}

assert bx.eval(env){improve; $(2.7); $(4.3)} == ((2.7+(4.3/2.7))/2)


bx.eval(env){defun; good_enoughp; ${y; x}
  ${le; ${abs; ${sub; ${mul; y; y}; x}}; $(0.000001)}}

bx.eval(env){defun; sqrt_iter; ${y; x}
  ${IF; ${good_enoughp; y; x}
    y
    ${sqrt_iter; ${improve; y; x}
      x}}}

bx.eval(env){defun; sqrt; ${x}
  ${sqrt_iter; $(1.0); x}}

assert bx.eval(env){sqrt; $(3.0)} == 1.73205081

