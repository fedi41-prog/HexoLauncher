package com.fedi4.hexolauncher.appsorter

sealed class AppTreeNode
    (
    open val name: String
            ) {
        class Folder(
            override val name: String,
            val children: MutableList<AppTreeNode> = mutableListOf()
        ) : AppTreeNode(name)
    
        class App(
            override val name: String,
            val packageName: String
        ) : AppTreeNode(name)
    }