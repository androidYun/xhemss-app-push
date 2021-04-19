package com.xhs.push.intercept;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class LoadPlatformHeadIntercept {
    public static LoadPlatformHeadIntercept getInstance() {
        return ViewHolderLoadPlatformHeadIntercept.LOADPLATFORMHEADINTERCEPT;
    }

    final static class ViewHolderLoadPlatformHeadIntercept {
        static final LoadPlatformHeadIntercept LOADPLATFORMHEADINTERCEPT = new LoadPlatformHeadIntercept();
    }

    private static HashSet<IBaseIntercept> mInterceptSet = new HashSet();

    static {
        mInterceptSet.add(new HuaWeiIntercept());
        mInterceptSet.add(new MiIntercept());
        mInterceptSet.add(new VivoIntercept());
        mInterceptSet.add(new OppoIntercept());
        mInterceptSet.add(new GeTuiIntercept());
    }

    private IBaseIntercept mIBaseIntercept = null;

    public IBaseIntercept loadPlatformHeadIntercept() {
        if (mIBaseIntercept == null) {
            IBaseIntercept previousIntercept= null;
            Iterator<IBaseIntercept> iterator = mInterceptSet.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                IBaseIntercept iBaseIntercept = iterator.next();
                if (i==0){
                    previousIntercept=iBaseIntercept;
                    mIBaseIntercept=previousIntercept;
                }else{
                    previousIntercept.setNextBaseIntercept(iBaseIntercept);
                    previousIntercept = iBaseIntercept;
                }
                i++;
            }
        }
        return mIBaseIntercept;
    }
}
