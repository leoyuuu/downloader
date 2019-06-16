package com.leoyuu.libdownloader;

import android.support.annotation.Nullable;

/**
 *
 * Copyright 2019-2024 leoyuuu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * date 2019-06-16
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
public interface LdLogger {
    /**
     * ld downloader log info
     * @param msg log msg
     * @param t error info
     */
    void onLog(String msg, @Nullable Throwable t);
}
