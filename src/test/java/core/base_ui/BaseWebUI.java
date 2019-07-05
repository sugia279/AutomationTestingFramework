package core.base_ui;

import core.base_action.WebAction;

public class BaseWebUI<M> {
    private BaseWebUIMap map;
    protected WebAction webAction;
    public BaseWebUI(M m, WebAction wAction) {
        webAction = wAction;
        map = (BaseWebUIMap) m;
        map.setWebAction(wAction);
        map.initElementMap();
    }

    public M Map() {
        return (M)map;
    }

    public WebAction getWebAction() {
        return webAction;
    }

}
