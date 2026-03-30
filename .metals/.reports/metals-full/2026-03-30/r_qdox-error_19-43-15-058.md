error id: file:///C:/Users/User/OneDrive/Desktop/Buteelt/Biy%20daalt/flashcard-system/src/main/java/mn/num/flashcard/App.java
file:///C:/Users/User/OneDrive/Desktop/Buteelt/Biy%20daalt/flashcard-system/src/main/java/mn/num/flashcard/App.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[2,1]

error in qdox parser
file content:
```java
offset: 30
uri: file:///C:/Users/User/OneDrive/Desktop/Buteelt/Biy%20daalt/flashcard-system/src/main/java/mn/num/flashcard/App.java
text:
```scala
// App.java-ийн main дотор:
L@@ist<Card> cards = loadCards(cmd.getArgs()[0]); // Файлаас унших
int totalCorrectInRound = 0;

for (Card card : cards) {
    // --invertCards идэвхтэй бол асуулт хариултыг солих [cite: 10]
    String displayQuestion = cmd.hasOption("invertCards") ? card.getAnswer() : card.getQuestion();
    String expectedAnswer = cmd.hasOption("invertCards") ? card.getQuestion() : card.getAnswer();

    System.out.println("Question: " + displayQuestion);
    Scanner scanner = new Scanner(System.in);
    String userAnswer = scanner.nextLine();

    if (userAnswer.equalsIgnoreCase(expectedAnswer)) {
        System.out.println("Correct!");
        card.recordAttempt(true);
        totalCorrectInRound++;
    } else {
        System.out.println("Wrong! Answer was: " + expectedAnswer);
        card.recordAttempt(false);
    }
}

// Амжилтуудыг шалгах [cite: 23, 24, 25]
if (totalCorrectInRound == cards.size()) {
    System.out.println("Achievement Unlocked: CORRECT!");
}
// Бусад амжилтуудыг Card-ын attemptCount, correctCount-аар шалгана.
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:49)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:99)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:560)
	scala.meta.internal.metals.Indexer.$anonfun$reindexWorkspaceSources$3(Indexer.scala:691)
	scala.meta.internal.metals.Indexer.$anonfun$reindexWorkspaceSources$3$adapted(Indexer.scala:688)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:630)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:628)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1313)
	scala.meta.internal.metals.Indexer.reindexWorkspaceSources(Indexer.scala:688)
	scala.meta.internal.metals.MetalsLspService.$anonfun$onChange$2(MetalsLspService.scala:940)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
	scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
	java.base/java.lang.Thread.run(Thread.java:1575)
```
#### Short summary: 

QDox parse error in file:///C:/Users/User/OneDrive/Desktop/Buteelt/Biy%20daalt/flashcard-system/src/main/java/mn/num/flashcard/App.java