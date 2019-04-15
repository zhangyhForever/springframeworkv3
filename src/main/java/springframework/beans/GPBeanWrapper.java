package springframework.beans;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/13
 */
public class GPBeanWrapper {

    private Object wrapperInstance;

    public GPBeanWrapper(Object instance){
        this.wrapperInstance = instance;
    }

    public Object getWrapperInstance(){
        return this.wrapperInstance;
    }

    public Class<?> getWrapperClass(){
        return this.wrapperInstance.getClass();
    }
}
