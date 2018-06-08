import java.io.File

fun main(args: Array<String>) {
    val rows = File("input.txt").readLines()

    //Create a list with all the nodes
    val nodeInfo = rows.map{ it.split(" ").take(2)}
    val nodes = nodeInfo.map { it -> Node(it.get(0), it.get(1).removeSurrounding("(", ")").toInt())}

    //Create map with parent as key and children as value
    val parentalInfo = rows.map { it.split("->") }.filter { it.size > 1 }
    val parentMap = parentalInfo.associateBy({ it[0].split(" ").first() }, { it[1].split(",").map { it.trim() } })

    //Setup parent/child within the nodes
    for ((key, value) in parentMap){
        val root = nodes.find { it.name == key }!!
        value.forEach{ child -> root.addChild(nodes.find { it.name == child}!!)}
    }

    val rootNode = findRoot(nodes.first())
    println(rootNode.name)
    setTotalWeight(rootNode)
    val faultyNode = findFaultyChild(rootNode)!!
    println(calculateDiff(faultyNode))
}

fun calculateDiff(faultyNode: Node): Int {
    val childrenOfParent = faultyNode.parent!!.children.map { it.totalWeight }
    val correctWeight =  if(childrenOfParent[0] != faultyNode.totalWeight) childrenOfParent[0] else childrenOfParent[1]
    return correctWeight - (faultyNode.totalWeight - faultyNode.weight)
}


fun findRoot(potential: Node): Node {
    return if (potential.parent == null) potential else findRoot(potential.parent!!)
}


fun setTotalWeight(root: Node): Int {
    if (root.children.isEmpty()) {
        root.totalWeight = root.weight
        return root.weight
    }

    for (child in root.children) {
        root.totalWeight += setTotalWeight(child)
    }
    return root.totalWeight
}

fun findFaultyChild(root: Node): Node? {
    if (root.children.isEmpty()) {
        return null
    }

    val childMap = root.children.groupBy { it.totalWeight }
    if (childMap.size == 1) {       //all children have same total weight
        return root
    }

    val faulty = findFaultyChild(childMap.filterValues { it.size == 1 }.values.single()[0])
    return faulty ?: root
}




