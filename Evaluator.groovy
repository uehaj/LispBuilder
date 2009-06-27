class Evaluator {
  Map globalEnv = [:]
  Map special = [:]

  Evaluator() {
    globalEnv.with {
      eq = {arg-> arg[0]==arg[1] }
      not = {arg->
             arg[0] ? false : true }
    }

    special.'if' = true
    special.'define' = true
  }

  def eval(LispList exp) {
    def func = exp.first
    def args = exp?.tail
    def entry = globalEnv[func]
    if (entry != null) {
      if (entry instanceof Closure) {
        if (!special[func]) {
          args = args*.eval()
        }
        entry.call(args)
      }
      else return entry
    }
  }

}  
