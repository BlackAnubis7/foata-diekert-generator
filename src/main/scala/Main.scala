object Main {
  def main(args: Array[String]): Unit = {
    val data = FileHandler.getData
    val A: Array[String] = data._1
    val w: Array[String] = data._2
    val prods: Array[String] = data._3
    val P = Util.label(A, prods)
    val G = Diekert.startGraph(w, P)

    println(Util.genPairs(A).filter(Dependencies.isD(P, _)).mkString("D = { sym {", ", ", "} }"))
    println(Util.genPairs(A).filterNot(Dependencies.isD(P, _)).mkString("I = { sym {", ", ", "} }"))
    println("Klasy na podstawie slowa: " + Foata.printFoata(A, w, P))
    println("<graf wygenerowany do pliku " + FileHandler.OUT + ">")
    println("Klasy na podstawie grafu: " + Foata.printFoata(Foata.foataFromGraph(w, G)))

    FileHandler.graphToFile(w, Diekert.graphEdges(G))
  }
}
