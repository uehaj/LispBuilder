class Evaluator {
  Map globalEnv = [:]

  Evaluator() {
    Functions.registerPredefined(globalEnv)
  }

  def eval(exp) {
    return exp.eval(globalEnv)
  }

}  
