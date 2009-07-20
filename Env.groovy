class Env {
  Map localEnv = [:]
  def parentEnv

  Env(env = null) {
    if (env == null) {
      parentEnv = [:]
      Functions.registerFunctions(parentEnv)
    }
    else {
      this.parentEnv = env
    }
  }


  def getProperty(String key) {
    if (localEnv.containsKey(key)) {
      return localEnv[key]
    }
    if (parentEnv != null) {
      return parentEnv[key]
    }
    null
  }

  void setProperty(String key, value) {
    localEnv[key] = value
  }

  def containsKey(key) {
    if (localEnv.containsKey(key)) {
      return true
    }
    if (parentEnv.containsKey(key)) {
      return true
    }
    return false
  }

  String toString() {
    "local:" + localEnv.toString() + "parent:" + parentEnv.toString()
  }

  def eval(Closure c) {
    def bx = new LispBuilder()
    return bx.build(c).eval(this)
  }

  def eval2(Closure c) {
    def bx = new LispBuilder2()
    return bx.build(c).eval(this)
  }

  def eval3(Closure c) {
    def bx = new LispBuilder3()
    return bx.build(c).eval(this)
  }
}
