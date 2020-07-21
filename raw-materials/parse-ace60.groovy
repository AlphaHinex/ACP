new File('/Users/alphahinex/Desktop/ace60.txt').withReader {
    def lines = it.readLines()
    def splitIndex = []
    lines.each {
        if (it.matches(/.*[单多]选.*/)) {
            def idx = lines.indexOf(it)
            if (idx > 0) {
                splitIndex << lines.indexOf(it) - 1
            }
        }
    }

    def groups = []
    def start = 0
    splitIndex.each {
        groups << lines[start..it]
        start = it + 1
    }
    groups << lines[splitIndex[-1]..lines.size()-1]

    groups.each { group ->
//        def index = group[0].toString().replaceAll(/[^\d]/, '').toInteger()

        def questionStartAt = 0
        def questionEndAt = group.findIndexOf { l -> l.toString().startsWith('A.')} - 1

        def question = group[questionStartAt..questionEndAt].join()

        def choices = [:]
        def alphabet = ['a', 'b', 'c', 'd', 'e', 'f','g']
        alphabet.eachWithIndex { String entry, int i ->
            if (i + 1 < alphabet.size()) {
                def startAt = group.findIndexOf { l -> l.toString().startsWith(entry) }
                def endAt = group.findIndexOf { l -> l.toString().startsWith(alphabet[i + 1]) } - 1
                if (endAt < 0) {
                    endAt = group.findIndexOf { l -> l.toString().matches(/A:\w*/)} - 1
                }
                if (endAt >= startAt && startAt > -1) {
                    choices.put(entry.toUpperCase(), group[startAt..endAt].join())
                }
            }
        }

        def correctIndex = group.findIndexOf { l -> l.toString().matches(/A:.*/)}
        def correct = group[correctIndex].toString().replaceAll('A:', '').replaceAll(/[^A-Z]/, '')
        def answer = "'${choices.get(correct)}'"
        if (correct.size() > 1) {
            answer = []
            correct.toCharArray().each {
                answer << choices.get(it.toString())
            }
            answer = "['${answer.join('\',\'')}']"
        }

        def explanation = correctIndex + 1 >= group.size() ? '' : group[correctIndex+1..-1].join()
        println "{'question':'$question','choices':['${choices.values().join('\',\'')}'],correct:$answer,'explanation':'$explanation'},"
    }
}