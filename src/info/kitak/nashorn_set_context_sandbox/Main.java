package info.kitak.nashorn_set_context_sandbox;

import jdk.nashorn.api.scripting.NashornScriptEngine;

import javax.script.*;

/**
 * Created by kitak on 7/3/16.
 */
public class Main {
    public static void main(String args[]) {

        ScriptEngineManager manager = new ScriptEngineManager();
        NashornScriptEngine engine = (NashornScriptEngine) manager.getEngineByName("nashorn");

        Object helloFunction;
        CompiledScript compiled;

        try {
            compiled = engine.compile("var i = 0; function hello() { i += 1; return i;}");
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }

        for (int i=0; i < 3; i++) {
            try {
                helloFunction = compiled.eval();
                manager = new ScriptEngineManager();
                engine = (NashornScriptEngine) manager.getEngineByName("nashorn");
                ScriptContext newContext = new SimpleScriptContext();
                newContext.setBindings(engine.createBindings(), ScriptContext.ENGINE_SCOPE);
                Bindings engineScope = newContext.getBindings(ScriptContext.ENGINE_SCOPE);
                engineScope.put("hello", helloFunction);
                engine.setContext(newContext);

                System.out.println(System.identityHashCode(engine));
                Object result = engine.invokeFunction("hello");
                System.out.println(String.valueOf(result));
            } catch (Exception e) {
                throw new IllegalStateException("invoke failed", e);
            }
        }
    }
}
