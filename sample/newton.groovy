def bx = new LispBuilder()

// Sqrt by newton method

def env = new Env()
env.eval{defun; abs; ${x}
         ${IF; ${lt; x; $0}; ${mul; x; $(-1)}; x}}

assert env.eval{abs; $1} == 1
assert env.eval{abs; $(1.2)} == 1.2
assert env.eval{abs; $(-1)} == 1
assert env.eval{abs; $(-1.2)} == 1.2
assert env.eval{abs; $0} == 0

env.eval{defun; average; ${x; y}
         ${div; ${add; x; y}; $2}}

assert env.eval{average; $1; $2} == 1.5
assert env.eval{average; $2; $4} == 3
assert env.eval{average; $(1.0); $(2.0)} == 1.5
assert env.eval{average; $(2.0); $(4.0)} == 3

env.eval{progn
         ${defun; improve; ${y; x}
           ${average; y; ${div; x; y}}}

         ${defun; good_enoughp; ${y; x}
           ${le; ${abs; ${sub; ${mul; y; y}; x}}; $(0.000001)}}

         ${defun; sqrt_iter; ${y; x}
           ${IF; ${good_enoughp; y; x}
             y
             ${sqrt_iter; ${improve; y; x}
               x}}}

         ${defun; sqrt; ${x}
           ${sqrt_iter; $(1.0); x}}}

assert env.eval{sqrt; $(3.0)} == 1.73205081

