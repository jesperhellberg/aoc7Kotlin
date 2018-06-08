class Node (name: String, weight: Int){
    val name: String = name
    val weight: Int = weight
    var totalWeight = weight
    var children: MutableList<Node> = mutableListOf()
    var parent: Node? = null

    fun addChild(child: Node) {
        children.add(child)
        child.parent = this
    }

}