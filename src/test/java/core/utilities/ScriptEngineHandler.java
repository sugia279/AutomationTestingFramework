package core.utilities;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ScriptEngineHandler {

    public static Object getVariableFromScript(String engineName, String script){
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName(engineName);
        try {
            return engine.eval(script);
        } catch (ScriptException e) {
            return e;
        }
    }
}
