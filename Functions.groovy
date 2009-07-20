class Functions {

  private static registerClosureFunctions(map) {
    map.with {

      eq = { args, env ->
             assert args.size() == 2
             if (args[0] == null && args[1] == null) {
               return true
             }
             if ((args[0] == null && args[1] != null)
                 ||(args[0] != null && args[1] == null)
                 ) {
               return false
             }
             if (args[0].class != args[1].class) {
               return false
             }
             if (args[0] instanceof Number
                 || args[0] instanceof String) {
               return args[0] == args[1]
             }
             args[0].is(args[1])
      }

      IF = { args, env, no_automatic_eval_arg ->
        assert args.size() in 2..3
        def cond = args[0].eval(env)
        def thenPart = args[1]
        if (cond) {
          return thenPart.eval(env)
        }
        else if (args.size() == 3) {
          def elsePart = args[2]
          return elsePart.eval(env)
        }
        return false
      }

      quote = { args, env, no_automatic_eval_arg ->
        assert args.size() == 1
        return args[0]
      }

      TRUE = true

      FALSE = false

      nil = null

      car = { args, env ->
              assert args.size() == 1
              args[0].car }

      cdr = { args, env ->
              assert args.size() == 1
              args[0].cdr }

      cons = { args, env ->
               assert args.size() == 2
               new Cons(args[0], args[1]) }

      setq = { args, env, no_automatic_eval_arg ->
               assert args.size() == 2
               env[args[0]] = args[1].eval(env)
      }

      and =  { args, env ->
               def result = true
               args.each {
                 result = result && it
               }
               result
      }

      or =  { args, env ->
              def result = false
              args.each {
                result = result || it
              }
              result
      }

      progn = {args, env, no_automatic_eval_arg ->
               def last
               args.each {
                 last = it.eval(env)
               }
               last
      }

      equal = { args, env ->
                assert args.size() == 2
                args[0] == args[1]
      }

      lt = { args, env ->
             assert args.size() == 2
             args[0] < args[1]
      }

      le = { args, env ->
             lt.call(args, env) || equal.call(args, env)
      }

      gt = { args, env ->
             !le.call(args, env)
      }

      ge = { args, env ->
             !lt.call(args, env)
      }


      add = { args, env ->
              assert args.size() >= 2
              def result = 0
              args.each {
                result += it.eval(env)
              }
              result
      }

      sub = { args, env ->
              assert args.size() >= 2
              def result = args[0]
              args.cdr.each {
                result -= it.eval(env)
              }
              result
      }

      mul = { args, env ->
              assert args.size() >= 2
              def result = args[0]
              args.cdr.each {
                result *= it.eval(env)
              }
              result
      }

      div = { args, env ->
              assert args.size() >= 2
              def result = args[0]
              args.cdr.each {
                result /= it.eval(env)
              }
              result
      }

      defun = {args, env, no_automatic_eval_arg ->
               assert args.size() == 3
               def sym_lambda = LispBuilder.makeSymbol("lambda");
               def sym_quote = LispBuilder.makeSymbol("quote");
               def new_args = [args[0],
                               [sym_quote,
                                [ sym_lambda, args[1], args[2] ]  as LispList
                                ] as LispList
                               ] as LispList
               setq.call(new_args, env, "no_automatic_eval_arg")
      }

      // invoke Java(groovy) level method
      call = {args, env ->
              assert args.size() >= 2
              def new_args = []
              for (int i=2; i<args.size(); i++) {
                println "add - ${args[i]}"
                new_args += args[i]
              }
              args[0].invokeMethod(args[1], new_args.size()==0 ? null : new_args)
      }

      print = {args, env ->
               args.each {
                 print it
               }
      }

      println = {args, env ->
                 print.call(args, env)
                 println ""
      }
    }
  }

  private static registerLambdaFunctions(map) {
    def bx = new LispBuilder()
    map.with {

      not = bx.build{lambda
                     ${x}
                     ${IF; x; FALSE; TRUE}
      }

      neq = bx.build{lambda
             ${x; y}
             ${not; ${eq; x; y}}
      }

      nullp = bx.build {lambda
                        ${x}
                        ${eq; nil; x}
      }

      append = bx.build {lambda
                         ${a; b}
                         ${IF;
                           ${nullp; a}
                           b;
                           ${cons; ${car; a}; ${append; ${cdr; a}; b}}}
      }

      reverse = bx.build {lambda
                          ${x}
                          ${IF
                            ${nullp; x}
                            x
                            ${append
                              ${reverse
                                ${cdr; x}}
                              ${cons; ${car; x}; nil}}}
      }
    }
  }

  static registerFunctions(map) {
    registerClosureFunctions(map)
    registerLambdaFunctions(map)
  }

}

