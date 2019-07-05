package saucedemo_webuitesting.uiview.controls;

import core.base_action.WebAction;
import core.base_ui.BaseWebUI;

public class UIControl<M> extends BaseWebUI<M> {

    public UIControl(M m, WebAction action)
    {
        super(m,action);
    }
}
