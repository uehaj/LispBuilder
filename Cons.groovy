class Cons extends LispList {
  private carPart

  def getCar() {
    carPart instanceof Closure ? carPart=carPart.call() : carPart
  }

  void replaceCar_(a) {
    carPart = a
  }

  private cdrPart

  def getCdr() {
    cdrPart instanceof Closure ? cdrPart=cdrPart.call() : cdrPart
  }

  void replaceCdr_(a) {
    cdrPart = a
  }

  Cons(a, b) {
    carPart = a;
    cdrPart = b;
  }

  def getAt(n) {
    assert n >= 0
    n==0 ? car : cdr[n-1]
  }

  String toStringHelper(HashSet hashSet) {
    if (hashSet.contains(this)) {
      return "..."
    }
    hashSet.add(this)
    def result = new StringBuilder("(")
    def list
    for (list=this; list instanceof LispList; list=list.cdr) {
      def elem = list.car
      if (elem == null) {
        result << "<nil>"
      }
      else if (elem instanceof Cons) {
        result << elem.toStringHelper(hashSet)+(list.cdr == null?"":" ")
      }
      else {
        result << elem.toString()+(list.cdr == null?"":" ")
      }
    }
    if (list != null) {
        result << ". " + list.toString();
    }
    result << ")";
  }


  String toString() {
    def set = new HashSet()
    return toStringHelper(set)
  }


  int size() {
	if (this.cdr == null) {
	  1
	}
	else {
	  1 + this.cdr.size()
	}
  }

  LispList plus(LispList a) {
	if (this.size() == 0) { //この実装の場合ありえない
	  a
	}
	else if(this.size() == 1) {
	  [car, a] as Cons
	}
	else {
	  [car, cdr+a] as Cons
	}
  }

  LispList append_(a) {
	if (this.size() == 0) { //この実装の場合ありえない
	  a
	}
	else if(this.size() == 1) {
	  replaceCdr_(new Cons(a, null))
	}
	else {
	  cdr.append_(a)
	}
  }

  boolean equals(LispList a) {
	if (this.cdr == a.cdr) {
	  return true
	}
	if (this.cdr == null && a.cdr != null) {
	  return false
	}
	if (this.cdr != null && a.cdr == null) {
	  return false
	}
	return this.cdr == a.cdr
  }

  Iterator iterator() {
    return new LispListIterator(this)
  }

  def applyLambda(lambda, args, env) {
    def pseudoArgList = lambda.cdr.car
    def body = lambda.cdr.cdr.car
    pseudoArgList.eachWithIndex { it, idx ->
      env[it] = args[idx]
    }
    body.eval(env)
  }

  def apply(func, args, env) {
    def entry = func.eval(env)
    if (entry != null) {
      if (entry instanceof Closure) {
        // 関数本体がGroovyのクロージャの場合。
        if (entry.maximumNumberOfParameters != 3) {
          // 引数を評価する。(SUBR)
          args = args*.eval(env)
          return entry.call(args, env)
        }
        else {
          // 3引数のクロージャは特殊形式とみなして引数を評価しない。(FSUBR)
          return entry.call(args, env, "no_automatic_eval_arg")
        }
      }
      else if (entry instanceof Cons) {
        // 関数本体がリストの場合。つまりLambdaの場合。(EXPR)
        if (entry.car == "lambda" && entry.car.isSymbol == true) {
          return applyLambda(entry, args, env)
        }
      }
      return entry
    }
    else {
      throw new Error("Undefined function: "+func)
    }
    return func
  }

  def eval(env) {
    def func = car
    def args = cdr
    apply(func, args, env)
  }

  def eval() {
    def evaluator = new Evaluator()
    return evaluator.eval(this)
  }

}
