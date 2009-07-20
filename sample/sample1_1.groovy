public class sample1_1 extends GroovyTestCase {

  def bx

  void setUp() {
    bx = new LispBuilder()
  }

  def foo(t) {
    def result = { ${$1; $2} }
    result.delegate = t;
    result
  }

  void testParseAtom() {
    // parse atom
    assert bx.build{$"ABC"}.toString() == "(ABC)"
    assert bx.build{$1}.toString() == "(1)"
    assert bx.build{$(-1)}.toString() == "(-1)"
    assert bx.build{$(1.3)}.toString() == "(1.3)"
  }

  void testParseSExp() {
    // parse S-expressions
    assert bx.build{ a; b; c; d; ${e; f; g; h} }.toString() ==
      "(a b c d (e f g h))"
    assert bx.build{ a; b; c; d; ${e; f; g; h}; i; j; }.toString() ==
      "(a b c d (e f g h) i j)"
    assert bx.build{ a; b; c; d; ${e; f; g; h}; i; j; ${ k }; l }.toString() ==
      "(a b c d (e f g h) i j (k) l)"

    assert bx.build{${a}}.toString() ==
      "((a))"
    assert bx.build{quote; $1;}.toString() ==
      "(quote 1)"
  }

  void testCallOtherMethodFromBuilder() {
    // call other method from builder

    assert bx.build{ a; foo(delegate).call(); b}.toString() ==
      "(a (1 2) b)"

    assert bx.build{
      ${ a; ${ b; ${ c; $1; $2; $3 }; d }; e } }.toString() ==
      '((a (b (c 1 2 3) d) e))'
  }

  void testBooleanConstantsAndOperators() {
    // Boolean constants and operators

    assert bx.build{TRUE}.eval() == true
    assert bx.build{FALSE}.eval() == false
    assert bx.build{not; FALSE}.eval() == true
    assert bx.build{not; TRUE}.eval() == false
  }


  void testCompareOperatorAndNegate() {
    // Compare operator and negate
    assert bx.build{eq; $1; $1}.eval() == true
    assert bx.build{eq; $(1); $(1)}.eval() == true
    assert bx.build{eq; $(1.0); $(1.0)}.eval() == true
    assert bx.build{eq; $1; $2}.eval() == false
    assert bx.build{eq; $"abc"; $"abc"}.eval() == true
    assert bx.build{eq; $"abc"; $"abx"}.eval() == false
    assert bx.build{not; ${eq; $1; $1}}.eval() == false
    assert bx.build{not; ${eq; $1; $2}}.eval() == true
    assert bx.build{neq; $1; $2}.eval() == true
    assert bx.build{neq; $1; $1}.eval() == false
    assert bx.build{neq; $(1); $(1)}.eval() == false
    assert bx.build{neq; $(1.0); $(1.0)}.eval() == false
  }

  void testConditionalOperator() {
    // Conditional operator
    assert bx.build{IF; TRUE; $"it's true" }.eval() == "it's true"
    assert bx.build{IF; FALSE; $"it's true" }.eval() == false
    assert bx.build{IF; ${not; FALSE; }; $"it's true" }.eval() == "it's true"
    assert bx.build{IF; ${not; TRUE; }; $"it's true" }.eval() == false
    assert bx.build{IF; TRUE; $1; $2 }.eval() == 1
    assert bx.build{IF; FALSE; $1; $2 }.eval() == 2
  }

  void testNumericalCompareOperators() {
    // Numerical compare operators
    assert bx.build{lt; $1; $1}.eval() == (1 < 1)
    assert bx.build{lt; $1; $2}.eval() == (1 < 2)
    assert bx.build{lt; $2; $1}.eval() == (2 < 1)
    assert bx.build{le; $1; $1}.eval() == (1 <= 1)
    assert bx.build{le; $1; $2}.eval() == (1 <= 2)
    assert bx.build{le; $2; $1}.eval() == (2 <= 1)
    assert bx.build{gt; $1; $1}.eval() == (1 > 1)
    assert bx.build{gt; $1; $2}.eval() == (1 > 2)
    assert bx.build{gt; $2; $1}.eval() == (2 > 1)
    assert bx.build{ge; $1; $1}.eval() == (1 >= 1)
    assert bx.build{ge; $1; $2}.eval() == (1 >= 2)
    assert bx.build{ge; $2; $1}.eval() == (2 >= 1)
  }

  void testCheckEquality() {
    // Check equality
    assert bx.build{eq; ${quote; ${a; b}}; ${quote; ${a; b}}}.eval() == false
    assert bx.build{equal; ${quote; ${a; b}}; ${quote; ${a; b}}}.eval() == true
    assert bx.build{equal; $"abc"; $"abc"}.eval() == true
    assert bx.build{equal; $"abc"; $"abx"}.eval() == false
  }

  void testSymbolAndStringLiteral() {
    // symbol and string literal
    assert bx.build{quote; a}.eval().isSymbol == true
    assert bx.build{quote; $"a"}.eval().isSymbol == false
  }

  void testListOperation() {
    // List operation
    assert bx.build{car; ${quote; ${x; y}}}.eval() == 'x'
    assert bx.build{cdr; ${quote; ${x; y}}}.eval().toString() == '(y)'
    assert bx.build{cons; $1; $2}.eval().toString() == '(1 . 2)'
    assert bx.build{cons; $1; ${cons; $2; $3}}.eval().toString() == '(1 2 . 3)'
    assert bx.build{car;${cons; $1; $2}}.eval() == 1
    assert bx.build{cdr;${cons; $1; $2}}.eval() == 2
    assert bx.build{nil}.eval() == null
    assert bx.build{car; ${cdr; ${quote; ${$1; $2}}}}.eval() == 2
  }

  void testProgn() {
    // progn
    assert bx.build{progn; $1; $2; $3}.eval() == 3
  }

  void testLogicalOperators() {
    // Logical operators

    assert bx.build{and; $1; $2}.eval() == true
    assert bx.build{and; $1; nil}.eval() == false
    assert bx.build{and; nil; $1}.eval() == false
    assert bx.build{and; nil; nil}.eval() == false
    assert bx.build{and; $1; $2; $3}.eval() == true
    assert bx.build{and; nil; $2; $3}.eval() == false
    assert bx.build{and; $1; nil; $3}.eval() == false
    assert bx.build{and; $1; $2; nil}.eval() == false
    assert bx.build{and; nil; nil; $3}.eval() == false
    assert bx.build{and; $1; nil; nil}.eval() == false
    assert bx.build{and; nil; $2; nil}.eval() == false
    assert bx.build{and; nil; nil; nil}.eval() == false

    assert bx.build{and; FALSE; FALSE}.eval() == false
    assert bx.build{and; TRUE; FALSE}.eval() == false
    assert bx.build{and; TRUE; TRUE}.eval() == true

    assert bx.build{or; $1; $2}.eval() == true
    assert bx.build{or; $1; nil}.eval() == true
    assert bx.build{or; nil; $1}.eval() == true
    assert bx.build{or; nil; nil}.eval() == false
    assert bx.build{or; $1; $2; $3}.eval() == true
    assert bx.build{or; nil; $2; $3}.eval() == true
    assert bx.build{or; $1; nil; $3}.eval() == true
    assert bx.build{or; $1; $2; nil}.eval() == true
    assert bx.build{or; nil; nil; $3}.eval() == true
    assert bx.build{or; $1; nil; nil}.eval() == true
    assert bx.build{or; nil; $2; nil}.eval() == true
    assert bx.build{or; nil; nil; nil}.eval() == false

    assert bx.build{or; FALSE; FALSE}.eval() == false
    assert bx.build{or; TRUE; FALSE}.eval() == true
    assert bx.build{or; FALSE; TRUE}.eval() == true
    assert bx.build{or; TRUE; TRUE}.eval() == true

  }

  void testArithmeticOperators() {
    // Arithmetic operators

    assert bx.build{add; $1; $2}.eval() == 3
    assert bx.build{add; $1; $2; $3}.eval() == 6

    assert bx.build{${add; $(1.3); $(3.2)} }.eval() == 1.3+3.2
    assert bx.build{${add; $(1.3); $(3.2); $(4.2)} }.eval() == 1.3+3.2+4.2
    assert bx.build{${sub; $(1.3); $(3.2)} }.eval() == 1.3-3.2
    assert bx.build{${sub; $(1.3); $(3.2); $(4.2)} }.eval() == 1.3-3.2-4.2
    assert bx.build{${mul; $(1.3); $(3.2);} }.eval() == 1.3*3.2
    assert bx.build{${mul; $(1.3); $(3.2); $(4.2)} }.eval() == 1.3*3.2*4.2
    assert bx.build{${div; $(1.3); $(3.2)} }.eval() == 1.3/3.2
    assert bx.build{${div; $(1.3); $(3.2); $(4.2)} }.eval() == 1.3/3.2/4.2

  }

  void testArithmeticCompar() {
    // Arithmetic compare

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
  }

  void testVariableToValue() {
    // bind variable to value
    assert bx.build{${progn; ${setq; a; $77;}; a }}.eval() == 77
    assert bx.build{${progn; ${setq; a; $40;}; ${add; a; a} }}.eval() == 80
  }

  void testShareEnvironmentAmongEvaluations() {
    // share environment among evaluations
    def env = new Env()
    env.eval{setq; b; $3}
    assert env.eval{ b } == 3
  }
}
