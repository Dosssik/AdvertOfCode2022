package days

import java.io.File as FileStd

private const val PREFIX_NAVIGATION = "cd"
private const val PREFIX_COMMAND = "$"
private const val PREFIX_DIR = "dir"

private const val COMMAND_NAVIGATE_UP = "cd .."
private const val COMMAND_NAVIGATE_ROOT = "cd /"

private const val COMMAND_LIST = "ls"

private const val MAX_DIR_SIZE = 100_000

private var currentDir = Dir(
    name = "ROOT",
    subDirs = emptyList(),
    files = emptyList(),
    level = 0,
    parent = null
)

fun main() {
    val output = FileStd("src/main/kotlin/days/input/input_day_7.txt").readText().lines()

    var position = 0
    while (position <= output.lastIndex) {

        val line = output[position]

        if (line.startsWith(PREFIX_COMMAND).not()) {
            throw IllegalStateException("Not a command! current line: $line")
        }
        val command = line.drop(2) // drop "$ "
        when {
            command.startsWith(COMMAND_LIST) -> {
                val content = mutableListOf<String>()
                var contentScanPosition = position + 1
                while (
                    contentScanPosition != output.lastIndex + 1 &&
                    output[contentScanPosition].startsWith(PREFIX_COMMAND).not()
                ) {
                    content.add(output[contentScanPosition])
                    contentScanPosition++
                }
                if (content.isNotEmpty()) {
                    processContent(content)
                    position = contentScanPosition
                }
            }
            command.startsWith(PREFIX_NAVIGATION) -> {
                executeNavigation(command)
                position++
            }
        }
    }

    println()
    println("File tree building is done!")

    navigateToRoot()
    val visitedNodes = mutableListOf<Dir>()
    visitNode(currentDir, visitedNodes)

    val cache = hashMapOf<String, Int>()
    val dirsWithFilesSizeLessThanLimit = visitedNodes.map { dir ->
        val filesSizeSum = dir.files.sumOf { file -> file.size }
        dir to filesSizeSum
    }.filter { (_, filesSize) -> filesSize < MAX_DIR_SIZE }

    val answerForFirst = dirsWithFilesSizeLessThanLimit.map { (dir, filesSize) ->
        dir.name to calculateDirSizeWithChildren(dir, cache)
    }
        .filter { (name, size) ->
            size < MAX_DIR_SIZE
        }.sumOf { it.second }

    println("Answer for task1: $answerForFirst")

    val allFilesSize = calculateDirSizeWithChildren(currentDir, emptyMap())
    println("all files size = $allFilesSize")
    val spaceLeft = 70_000_000 - allFilesSize
    println("space left = $spaceLeft")
    val spaceToClean = 30_000_000 - spaceLeft
    println("space to clean = $spaceToClean")

    val dirNamesWithSizes = visitedNodes.map { dir ->
        dir.name to calculateDirSizeWithChildren(dir, cache)
    }

    val answerForSecond = dirNamesWithSizes.map { (_, size) -> size }
        .sorted()
        .apply {
            forEach { println("Dir size: $it") }
        }
        .first { size -> size > spaceToClean }

    println("Answer for task2: $answerForSecond")
}

private fun calculateDirSizeWithChildren(dir: Dir, cache: Map<String, Int>): Int {
    val key = dir.name + dir.level
    if (cache.contains(key)) return cache[key]!!

    val filesSize = dir.files.sumOf { it.size }
    val dirsSize = dir.subDirs.map { calculateDirSizeWithChildren(it, cache) }.sum()


    return dirsSize + filesSize
}

private fun visitNode(dir: Dir, visitedNodes: MutableList<Dir>) {
    val padding = StringBuilder().apply {
        repeat(dir.level) {
            append(" ")
        }
    }.toString()

    println(padding + "- ${dir.name} (dir)")
    visitedNodes.add(dir)
    dir.files.forEach {
        println("-- ${it.name} (file, size=${it.size})")
    }
    dir.subDirs.forEach { visitNode(it, visitedNodes) }
}

private fun processContent(content: List<String>) {
    val files = mutableListOf<File>()
    val dirsNames = mutableListOf<String>()
    content.forEach { item ->
        if (item.startsWith(PREFIX_DIR)) {
            val dirName = item.split(" ").last()
            dirsNames.add(dirName)
        } else {
            val (size, name) = item.split(" ")
            files.add(File(name, size.toInt()))
        }
    }
    currentDir.files = files
    val existedDirsNames = currentDir.subDirs.map { it.name }
    val dirsToAdd = dirsNames.filter { dirName -> existedDirsNames.contains(dirName).not() }.map { dirName ->
        Dir(
            name = dirName,
            subDirs = emptyList(),
            files = emptyList(),
            level = currentDir.level + 1,
            parent = currentDir
        )
    }
    currentDir.subDirs = currentDir.subDirs + dirsToAdd
}

private fun executeNavigation(command: String) {
    when (command) {
        COMMAND_NAVIGATE_ROOT -> navigateToRoot()
        COMMAND_NAVIGATE_UP -> {
            if (currentDir.parent != null) {
                currentDir = currentDir.parent!!
            }
        }
        else -> {
            val destinationName = command.drop(3) // drop "cd " and keep dir name
            val alreadyExistedDir = currentDir.subDirs.find { it.name == destinationName }
            val destinationDir = if (alreadyExistedDir != null) {
                alreadyExistedDir
            } else {
                Dir(
                    name = destinationName,
                    subDirs = emptyList(),
                    files = emptyList(),
                    level = currentDir.level + 1,
                    parent = currentDir
                )
            }
            currentDir = destinationDir
        }

    }
}

private fun navigateToRoot() {
    var dir = currentDir
    while (dir.parent != null) {
        dir = dir.parent!!
    }
    currentDir = dir
}

private data class Dir(
    val name: String,
    var subDirs: List<Dir>,
    var files: List<File>,
    var level: Int,
    var parent: Dir?
)

private data class File(
    val name: String,
    val size: Int
)