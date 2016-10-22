//>= Functions
package com.craftinginterpreters.lox;

import java.util.List;

class LoxFunction implements Callable {
  private final Stmt.Function declaration;
  private final Environment closure;
//>= Classes
  private final boolean isInitializer;
//>= Functions

/*>= Functions < Classes
LoxFunction(Stmt.Function declaration, Environment closure) {
*/
//>= Classes
  LoxFunction(Stmt.Function declaration, Environment closure,
              boolean isInitializer) {
//>= Functions
    this.declaration = declaration;
    this.closure = closure;
//>= Classes
    this.isInitializer = isInitializer;
//>= Functions
  }

//>= Classes
  LoxFunction bind(LoxInstance self) {
    Environment environment = closure.enterScope();
    environment.define("this", self);
    return new LoxFunction(declaration, environment, isInitializer);
  }

//>= Functions
  @Override
  public String toString() {
    return declaration.name.text;
  }

  @Override
  public int requiredArguments() {
    return declaration.parameters.size();
  }

  @Override
  public Object call(Interpreter interpreter, List<Object> arguments) {
    Object result = null;

    try {
      Environment environment = closure.enterScope();
      for (int i = 0; i < declaration.parameters.size(); i++) {
        environment.define(declaration.parameters.get(i).text,
            arguments.get(i));
      }

      interpreter.executeBody(declaration.body, environment);
    } catch (Return returnValue) {
      result = returnValue.value;
    }

/*>= Functions < Classes
    return result;
*/
//>= Classes
    return isInitializer ? closure.getAt(0, "this") : result;
//>= Functions
  }
}