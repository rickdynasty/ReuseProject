package com.pasc.lib.base.util;

import java.util.Collection;

/**
 * Created by duyuan797 on 18/1/17.
 */

public class CollectionUtils {

    public static boolean isEmpty(Collection c) {
        return c == null || c.isEmpty();
    }

    public static boolean isNotEmpty(Collection c) {
        return c != null && !c.isEmpty();
    }

}
