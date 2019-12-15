new File('/Users/alphahinex/github/fork/JavaScript_QUIZ/raw-materials/721-all.txt').withReader {
    def lines = it.readLines()
    def splitIndex = []
    lines.each {
        if (it.matches(/-*\d{3}\.txt.*/)) {
            splitIndex << lines.indexOf(it)
        }
    }

    def groups = []
    def start = 0
    splitIndex.each {
        groups << lines[start..it]
        start = it + 1
    }
    groups.each { group ->
        def index = group[-1].toString().replaceAll(/[^\d]/, '').toInteger()

        def questionStartAt = group.findIndexOf { l -> l.toString().contains('721') } + 1
        if (group[questionStartAt].toString().startsWith('断点续答保护中')) {
            questionStartAt++
        }
        def questionEndAt = group.findIndexOf { l -> l.toString().startsWith('A')} - 1

        def question = group[questionStartAt..questionEndAt].join()

        def choices = [:]
        def alphabet = ['A', 'B', 'C', 'D', 'E', 'F']
        alphabet.eachWithIndex { String entry, int i ->
            if (i + 1 < alphabet.size()) {
                def startAt = group.findIndexOf { l -> l.toString().startsWith(entry) }
                def endAt = group.findIndexOf { l -> l.toString().startsWith(alphabet[i + 1]) } - 1
                if (endAt < 0) {
                    endAt = group.findIndexOf { l -> l.toString().matches(/^答[案家].*/)} - 1
                }
                if (endAt >= startAt && startAt > -1) {
                    choices.put(entry, group[startAt..endAt].join())
                }
            }
        }

        def correctIndex = group.findIndexOf { l -> l.toString().matches(/^答[案家].*/)}
        def correct = group[correctIndex].toString().replaceAll(/[^A-Z]/, '')
        def answer = "'${choices.get(correct)}'"
        if (correct.size() > 1) {
            answer = []
            correct.toCharArray().each {
                answer << choices.get(it.toString())
            }
            answer = "['${answer.join('\',\'')}']"
        }

        def explanation = group[correctIndex+1..-2].join()
        println "{'question':'$index: $question','choices':['${choices.values().join('\',\'')}'],correct:$answer,'explanation':'$explanation'},"
    }
}