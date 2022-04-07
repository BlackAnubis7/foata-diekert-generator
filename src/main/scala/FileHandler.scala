import java.io.FileWriter
import scala.io.Source

object FileHandler {
  /** Ścieżka pliku z danymi wejściowymi */
  val IN: String = "src/main/resources/test2.txt"

  /** Ścieżka pliku do którego wygenerowany zostanie graf */
  val OUT: String = "src/main/resources/graph2.txt"

  /**
   * Pobiera dane z pliku w formacie:
   * <pre>
   *   Alfabet (znaki oddzielone spacjami)
   *   Słowo
   *   Produkcje...
   * </pre>
   * @return krotka (Lista symboli alfabetu, Lista znaków w słowie, Lista produkcji)
   */
  def getData: (Array[String], Array[String], Array[String]) = {
    val src = Source.fromFile(IN)
    val lines = src.getLines()
    val A: Array[String] = lines.next().split(" ")
    val w: Array[String] = lines.next().split("")
    val prods: Array[String] = lines.toArray
    src.close()
    (A, w, prods)
  }

  /**
   * Wpisuje do pliku graf w formacie DOT
   * @param word słowo (posłuży do generacji etykiet grafu)
   * @param edges lista krotek (A, B), reprezentujących krawędź skierowaną A -> B
   */
  def graphToFile(word: Array[String], edges: Array[(Int, Int)]): Unit = {
    val writer: FileWriter = new FileWriter(OUT)

    writer.write("digraph g{")
    edges.foreach(t => writer.write("\n" + t._1 + " -> " + t._2))
    word.indices.foreach(i => writer.write("\n" + i + "[label=" + word(i) + "]"))
    writer.write("\n}")

    writer.close()
  }

}
