class Evaluator {
  Map globalEnv = [:]
  Map special = [:]

  Evaluator() {
    Functions.registerPredefined(globalEnv, special)
  }

  def eval(exp) {
    def func = exp.car
    def args = exp?.cdr
    def entry = globalEnv[func]
    if (entry != null) {
      if (entry instanceof Closure) {
        if (!special[func]) {
          args = args*.eval()
        }
        return entry.call(args)
      }
      return entry
    }
    return func
  }

}  
