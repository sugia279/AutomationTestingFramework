package core.uibase;

import core.actionbase.WebAction;

public class BaseWebUI<M,V> {
    private BaseWebUIMap map;
    private BaseWebUIValidator<M> validator;
    protected WebAction webAction;
    public BaseWebUI(M m, V v, WebAction wAction) {
        webAction = wAction;
        map = (BaseWebUIMap) m;
        map.setWebAction(wAction);
        map.initElementMap();
        validator = (BaseWebUIValidator<M>)v;
        validator.setMap(m);
    }

    public M Map() {
        return (M)map;
    }

    public V Validator() {
        return (V)validator;
    }

    public WebAction getWebAction() {
        return webAction;
    }

}
