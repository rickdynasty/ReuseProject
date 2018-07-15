package com.pasc.lib.webpage.behavior;

import com.pasc.lib.webpage.callback.CallBackFunction;

/*
 * Copyright (C) 2018 pasc Licensed under the Apache License, Version 1.0 (the "License");
 * @author chenshangyong872
 * @date 2018-07-15
 * @des行为处理器的抽象接口
 * @version 1.0
 */
public interface BehaviorHandler {
    void handler(String data, CallBackFunction function);
}
