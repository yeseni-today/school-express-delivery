package com.delivery.common.persis;


import com.delivery.common.action.Action;
import com.delivery.common.action.ActionType;
import com.delivery.common.action.DefaultAction;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import static com.delivery.common.persis.PersisContance.*;
import java.util.Map;


/**
 * Created by finderlo on 2017/4/7.
 */
public class PersisActionBuilder {

    @NotNull
    private PersistentType type;

    @Nullable
    private Object value;

    private Map<String,String> attribute;

    public PersisActionBuilder type(PersistentType type){
        this.type = type;
        return this;
    }

    public PersisActionBuilder value(Object o){
        this.value = o;
        return this;
    }

    public PersisActionBuilder add(String key,String value){
        attribute.put(key, value);
        return this;
    }

    private Action builder(){
        DefaultAction action = new DefaultAction();
        action.setType(ActionType.PERSISTENT);
        action.getAttributes().put(PERSIS_TYPE,type);
        if (type.equals(PersistentType.SAVE) || type.equals(PersistentType.UPDATE) || type.equals(PersistentType.DELETE)) {
            if (value == null) throw new IllegalStateException("持久层保持|更新|删除操作对象为空");
            action.getAttributes().put(PERSIS_OBJECT,value);
        }else if (type.equals(PersistentType.FIND)){
            action.getAttributes().put(PERSIS_FIND_ATTR,attribute);
        }
        return action;
    }

}
