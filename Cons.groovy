class Cons extends LispList {
  private carPart

  def getCar() {
    carPart instanceof Closure ? carPart=carPart.call() : carPart
  }

  def replaceCar_(a) {
    carPart = a
  }

  private cdrPart

  def getCdr() {
    cdrPart instanceof Closure ? cdrPart=cdrPart.call() : cdrPart
  }

  def replaceCdr_(a) {
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

  String toString() {
    def result = new StringBuilder("(")
    def list
    for (list=this; list instanceof LispList; list=list.cdr) {
      result << list.car.toString()+(list.cdr == null?"":" ");
    }
    if (list != null) {
        result << ". " + list.toString();
    }
    result << ")";
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

  def eval(env) {
    def func = car
    def args = cdr
    def entry = env[func]
    if (entry != null) {
      if (entry instanceof Closure) {
        if (entry.maximumNumberOfParameters != 2) {
          args = args*.eval(env)
          return entry.call(args)
        }
        else {
          // 2引数のクロージャは特殊形式とみなして引数を評価しない
          return entry.call(args, "dummpy_special")
        }
      }
      return entry
    }
    return func
  }

  def eval() {
    def evaluator = new Evaluator()
    return evaluator.eval(this)
  }

}
