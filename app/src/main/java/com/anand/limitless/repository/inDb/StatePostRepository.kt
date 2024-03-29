/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.anand.limitless.repository.inDb

import com.anand.limitless.repository.Listing
import com.anand.limitless.vo.StateName


/**
 * Common interface shared by the different repository implementations.
 * Note: this only exists for sample purposes - typically an app would implement a repo once, either
 * network+db, or network-only
 */
interface StatePostRepository {
    fun postsOfSubreddit(subReddit: String, pageSize: Int): Listing<StateName>

    enum class Type {
        IN_MEMORY_BY_ITEM,
        IN_MEMORY_BY_PAGE,
        DB
    }
}