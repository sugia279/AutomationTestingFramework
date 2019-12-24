package core.testdata_manager;

import java.util.ArrayList;

public class TestCase implements Cloneable {
    private String id;
    private String name;
    private String description;
    private String objectives;
    private String note;
    private boolean active;
    private String tag;
    private ArrayList<TestStep> _testSteps;

    public TestCase() {
        setId("");
        setName("");
        setDescription("");
        setNote("");
        setTag("");
        setActive(true);
        setObjectives("");
        set_testSteps(new ArrayList<TestStep>());
    }

    public TestCase(Object id,Object name, Object description) {
        setId(id);
        setName(name);
        setDescription(description);
        setNote("");
        setTag("");
        setActive(true);
        setObjectives("");
        set_testSteps(new ArrayList<TestStep>());
    }

    public String getId() {
        return id;
    }

    public void setId(Object id) {
        if(id instanceof String) {
            this.id = (String)id;
        }
        else
            this.id = "";
    }

    public String getName() {
        return name;
    }

    public void setName(Object name) {
        if(name instanceof String) {
            this.name = (String)name;
        }
        else
            this.name = "";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        if(description instanceof String) {
            this.description = (String)description;
        }
        else
            this.description = "";
    }

    public ArrayList<TestStep> get_testSteps() {
        return _testSteps;
    }

    public void set_testSteps(ArrayList<TestStep> _testSteps) {
        this._testSteps = _testSteps;
    }



    public String getNote() {
        return note;
    }

    public void setNote(Object note) {
        if(note instanceof String) {
            this.note = (String) note;
        }
        else
            this.note = "";
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(Object active) {
        if(active instanceof Boolean) {
            this.active = (boolean)active;
        }
        else
            this.active = true;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        if(tag instanceof String) {
            this.tag = (String)tag;
        }
        else
            this.tag = "";
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(Object objectives) {
        if(objectives instanceof String) {
            this.objectives = (String)objectives;
        }
        else
            this.objectives = "";
    }

    public void clear() {
        id ="";
        name = "";
        description = "";
        active = true;
        tag ="";
        objectives ="";
        for (TestStep ts : _testSteps) {
            ts.clear();
        }
        _testSteps.clear();
    }

    public Object getParamValueFromTestStep(int stepOrder, String paramName){
        return get_testSteps().get(stepOrder - 1).getTestParams().get(paramName);
    }
}
