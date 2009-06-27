class LispEvaluator {
  Map globalEnv = [:]

  LispEvaluator() {
    globalEnv.with {
      eq = {arg-> arg[0]==arg[1] }
      not = {arg->
             println arg[0]
             arg[0] ? false : true }
    }
  }

  def eval(LispList exp) {
    def func = exp.first
    def args = exp?.tail
    def entry = globalEnv[func]
    if (entry != null) {
      if (entry instanceof Closure) {
        entry.call(args*.eval())
      }
      else return entry
    }
  }

}  
