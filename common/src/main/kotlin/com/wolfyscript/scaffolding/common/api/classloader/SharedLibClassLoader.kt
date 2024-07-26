package com.wolfyscript.scaffolding.common.api.classloader

import java.net.URL
import java.net.URLClassLoader

class SharedLibClassLoader(urls: Array<out URL>?, parent: ClassLoader?) : URLClassLoader(urls, parent) {




}